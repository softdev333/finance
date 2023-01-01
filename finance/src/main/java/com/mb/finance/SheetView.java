package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.config.TransactionDto;
import com.mb.finance.config.TransactionType;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.entities.Sheet;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.SheetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "sheet", layout = MainLayout.class)
@PageTitle("Finance : Sheet")
public class SheetView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    SheetService sheetService;

    @Autowired
    ExpenseService expenseService;

    @Autowired
    IncomeService incomeService;

    private String APPLICATION_NAME = "Finance Portal";

    Button currentButton = new Button("Process Sheets Data");

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {
	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    public SheetView(SheetService sheetService, ExpenseService expenseService, IncomeService incomeService)
	    throws IOException, GeneralSecurityException {
	H2 pageTitle = new H2("Google Sheets Data");

	String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
	Sheet sheet = sheetService.findByUserId(userId);
	Sheets sheets = getGoogleSheets(sheet);

	String spreadsheet_id = sheet.getSheetId();

	currentButton.addClickListener(event -> {
	    try {

		// rowNumber by default will start with 0 i.e. 0 rows read, and 1st row will
		// have row headers
		// int rowNum = sheet.getRowNumber()+2;
		int rowNum = sheet.getRowNumber();
		rowNum = rowNum == 0 ? rowNum + 2 : rowNum + 1;
		int startingRowNum = rowNum;

		// we will process the records in a batch of 100
		String range = "finance" + "!A" + rowNum + ":G" + (rowNum + 100);

		ValueRange response = sheets.spreadsheets().values().get(spreadsheet_id, range).execute();
		List<List<Object>> rowList = response.getValues();
		if (rowList == null || rowList.isEmpty()) {
		    Notification notification = new Notification("Nothing to process", 5000, Position.MIDDLE);
		    notification.open();
		} else {
		    List<Income> incomeList = new ArrayList<>();
		    List<Expense> expenseList = new ArrayList<>();

		    for (List<Object> row : rowList) {
			// check if there is data or not, if not, break the loop
			if (row.get(1) == null) {
			    break;
			}
			// convert the data to appropriate transactionDto
			TransactionDto transactionDto = convertDataToTransactionDto(row, userId);

			// convert the transaction to appropriate entity and add it to the list
			convertTransactionAndAddToList(transactionDto, incomeList, expenseList);

			// increment rowNumber read by 1
			rowNum = rowNum + 1;
		    }

		    // save all the incomes and expenses
		    incomeService.saveAllIncome(incomeList);
		    expenseService.saveAllExpenses(expenseList);

		    List<List<Object>> updateList = new ArrayList<>();

		    for (int i = startingRowNum; i < rowNum; i++) {
			updateList.add(Arrays.asList("yes"));
		    }

		    ValueRange body = new ValueRange().setValues(updateList);
		    String updateRange = "finance!H" + startingRowNum + ":H" + (rowNum - 1);
		    sheets.spreadsheets().values().update(spreadsheet_id, updateRange, body)
			    .setValueInputOption("USER_ENTERED").execute();

		    // save the updated rowNumber
		    sheet.setRowNumber(rowNum - 1);
		    sheetService.saveSheet(sheet);
		}

	    } catch (IOException e) {
		e.printStackTrace();
	    }

	});

	add(pageTitle, currentButton);

    }

    private void convertTransactionAndAddToList(TransactionDto transactionDto, List<Income> incomeList,
	    List<Expense> expenseList) {
	if (transactionDto.getTransactionType() == TransactionType.EXPENSE) {
	    Expense expense = new Expense();
	    expense.setAmount(transactionDto.getAmount());
	    expense.setComments(transactionDto.getComments());
	    expense.setCreationDate(LocalDate.now());
	    expense.setExpenseDate(transactionDto.getTransactionDate());
	    expense.setWithdrawnFrom(transactionDto.getTransactionEndPoint());
	    expense.setExpenseOccurance(transactionDto.getTransactionOccurance());
	    expense.setExpenseType(ExpenseType.valueOf(transactionDto.getTransactionOn()));
	    expense.setUserId(transactionDto.getUserId());

	    expenseList.add(expense);
	} else {
	    Income income = new Income();
	    income.setAmount(transactionDto.getAmount());
	    income.setComments(transactionDto.getComments());
	    income.setCreationDate(LocalDate.now());
	    income.setIncomeDate(transactionDto.getTransactionDate());
	    income.setDepositedIn(transactionDto.getTransactionEndPoint());
	    income.setIncomeOccurance(transactionDto.getTransactionOccurance());
	    income.setIncomeType(IncomeType.valueOf(transactionDto.getTransactionOn()));
	    income.setUserId(transactionDto.getUserId());

	    incomeList.add(income);
	}
    }

    private TransactionDto convertDataToTransactionDto(List<Object> row, String userId) {

	TransactionDto transactionDto = new TransactionDto();

	String dateObjects = (String) row.get(0);
	String[] dateParts = dateObjects.split("/");
	LocalDate transactionDate = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]),
		Integer.parseInt(dateParts[0]));
	transactionDto.setTransactionDate(transactionDate);

	String transactionTypeString = (String) row.get(1);
	TransactionType transactionType = transactionTypeString.toLowerCase().equals("income") ? TransactionType.INCOME
		: TransactionType.EXPENSE;
	transactionDto.setTransactionType(transactionType);

	String transactionOn = (String) row.get(2);
	transactionOn = transactionOn.toUpperCase();
	transactionDto.setTransactionOn(transactionOn);

	String transactionOccurance = (String) row.get(3);
	transactionOccurance = transactionOccurance.toUpperCase();
	transactionDto.setTransactionOccurance(Occurance.valueOf(transactionOccurance));

	String comments = (String) row.get(4);
	transactionDto.setComments(comments);

	String amountString = (String) row.get(5);
	BigDecimal amount = new BigDecimal(amountString);
	transactionDto.setAmount(amount);

	String transactionEndPoint = (String) row.get(6);
	transactionDto.setTransactionEndPoint(transactionEndPoint);

	transactionDto.setCreationDate(LocalDate.now());
	transactionDto.setUserId(userId);

	return transactionDto;
    }

    private Credential authorize(Sheet sheet) throws IOException, GeneralSecurityException {
	String jsonString = sheet.getSheetsClientSecretJsonString();
	InputStream is = new ByteArrayInputStream(jsonString.getBytes());

	GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
		new InputStreamReader(is));

	List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
	GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets,
		scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
			.setAccessType("offline").build();
	Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	return credential;

    }

    public Sheets getGoogleSheets(Sheet sheet) throws IOException, GeneralSecurityException {
	Credential credential = authorize(sheet);
	return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
		credential).setApplicationName(APPLICATION_NAME).build();
    }

}
