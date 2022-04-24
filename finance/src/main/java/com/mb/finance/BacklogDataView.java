package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.config.Processed;
import com.mb.finance.config.TransactionType;
import com.mb.finance.entities.BacklogData;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.service.BacklogDataService;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "backlogdataservice", layout = MainLayout.class)
@PageTitle("Finance : Load Backlog Data")
public class BacklogDataView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	BacklogDataService backlogDataService;

	@Autowired
	ExpenseService expenseService;

	@Autowired
	IncomeService incomeService;

	@Autowired
	UserService userService;

	Grid<BacklogData> backlogDataGrid = new Grid<>(BacklogData.class);

	TextArea workspace = new TextArea("Workspace");

	Button transactionFormButton = new Button("Generate Transaction Form");

	ComboBox<Processed> processedBox = new ComboBox<>("Processed ?");

	Button submitFormButton = new Button("Submit Form");

	ComboBox<TransactionType> transactionTypeBox = new ComboBox<>("Transaction Type");

	TextField amountTextField = new TextField("Amount");

	TextField transactionSourceField = new TextField("Source of the Transaction");

	TextField transactionTypeField = new TextField("Type of Income or Expense");

	DatePicker transactionDate = new DatePicker("Transaction Date");

	ComboBox<Occurance> occuranceBox = new ComboBox<>("Occurance");

	TextArea commentsArea = new TextArea("Comments");

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public BacklogDataView(BacklogDataService backlogDataService, ExpenseService expenseService,
			IncomeService incomeService, UserService userService) {

		initialSettings();

		backlogDataGrid.setSelectionMode(SelectionMode.SINGLE);
		backlogDataGrid.asSingleSelect().addValueChangeListener(e -> {
			if (e.isFromClient()) {
				BacklogData backlogData = e.getValue();
				workspace.setValue(backlogData.getRecord());
			}
		});
		backlogDataGrid.setSizeFull();

		updateBacklogDataGrid(backlogDataService);

		processedBox.addValueChangeListener(event -> {
			if (event.isFromClient()) {
				updateBacklogDataGrid(backlogDataService);
				Processed processed = event.getValue();
				List<BacklogData> gridData = backlogDataGrid.getDataProvider().fetch(new Query<>())
						.collect(Collectors.toList());
				List<BacklogData> updatedData = null;

				if (processed == Processed.PROCESSED) {
					updatedData = gridData.stream().filter(e -> e.getIsProcessed() == true)
							.collect(Collectors.toList());
				} else if (processed == Processed.UNPROCESSED) {
					updatedData = gridData.stream().filter(e -> e.getIsProcessed() == false)
							.collect(Collectors.toList());
				} else {
					updatedData = gridData;
				}

				backlogDataGrid.setItems(updatedData);
			}
		});

		VerticalLayout transactionFormLayout = new VerticalLayout();
		transactionFormLayout.setVisible(false);

		transactionFormButton.addClickListener(event -> {
			transactionFormButton.setVisible(false);
			HorizontalLayout hl1 = new HorizontalLayout(amountTextField, transactionTypeBox);
			HorizontalLayout hl2 = new HorizontalLayout(transactionSourceField, transactionTypeField);
			HorizontalLayout hl3 = new HorizontalLayout(occuranceBox, transactionDate);

			transactionFormLayout.add(hl1, hl2, hl3, commentsArea, submitFormButton);

			transactionFormLayout.setVisible(true);
		});

		submitFormButton.addClickListener(event -> {
			BacklogData bData = backlogDataGrid.asSingleSelect().getValue();
			submitFormMethod(bData, backlogDataService, expenseService, incomeService);
		});

		VerticalLayout vLayout1 = new VerticalLayout(backlogDataGrid);

		VerticalLayout vLayout2 = new VerticalLayout(transactionFormButton, workspace, transactionFormLayout);

		HorizontalLayout hLayout = new HorizontalLayout(vLayout1, vLayout2);
		hLayout.setSizeFull();

		Button loadEmailButton = new Button("Load Emails");
		loadEmailButton.addClickListener(event -> {
			String teString = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
			userService.loadEmailData(teString);
			updateBacklogDataGrid(backlogDataService);
		});

		add(new H2("Backlog Data"),new H4("To load Emails, please send your expense or income emails to your email id with subject as FINANCE"), loadEmailButton, processedBox, hLayout);
	}

	public void submitFormMethod(BacklogData bData, BacklogDataService backlogDataService,
			ExpenseService expenseService, IncomeService incomeService) {
		if (transactionTypeBox.getValue() == TransactionType.EXPENSE) {
			Expense expense = new Expense();
			expense.setAmount(new BigDecimal(amountTextField.getValue()));
			expense.setComments(commentsArea.getValue());
			expense.setCreationDate(LocalDate.now());
			expense.setExpenseDate(transactionDate.getValue());
			expense.setWithdrawnFrom(transactionSourceField.getValue());
			expense.setExpenseOccurance(occuranceBox.getValue());
			expense.setExpenseType(ExpenseType.valueOf(transactionTypeField.getValue().toUpperCase()));
			expense.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));

			try {
				expenseService.addExpense(expense, bData.getCreationDate());
			} catch (Exception e) {
				e.printStackTrace();
			}



		} else {
			Income income = new Income();
			income.setAmount(new BigDecimal(amountTextField.getValue()));
			income.setComments(commentsArea.getValue());
			income.setCreationDate(LocalDate.now());
			income.setIncomeDate(transactionDate.getValue());
			income.setDepositedIn(transactionSourceField.getValue());
			income.setIncomeOccurance(occuranceBox.getValue());
			income.setIncomeType(IncomeType.valueOf(transactionTypeField.getValue().toUpperCase()));
			income.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));

			try {
				incomeService.addNewIncome(income, bData.getCreationDate());
			} catch (Exception e) {
				e.printStackTrace();
			}

			backlogDataService.deleteBacklogData(bData);
		}

		updateBacklogDataGrid(backlogDataService);
	}

	public void initialSettings() {
		
		transactionTypeField.setHelperText("EXPENSE: OUTING,CLOTHES,MEDICINE,RENT,\nMISCELLANEOUS,FOOD,TRAVELLING,STOCKS,OTHERS \nINCOME: JOB,STOCKS,OTHERS" );

		transactionTypeBox.setItems(TransactionType.values());
		occuranceBox.setItems(Occurance.values());
		commentsArea.setWidth("80%");
		commentsArea.getStyle().set("minHeight", "100px");

		backlogDataGrid.setColumns("creationDate", "isProcessed", "record");
		backlogDataGrid.setWidth("70%");
		backlogDataGrid.getStyle().set("minHeight", "450px");
		backlogDataGrid.getColumnByKey("record").setAutoWidth(true);
		backlogDataGrid.getColumnByKey("isProcessed").setAutoWidth(true);
		backlogDataGrid.getColumnByKey("creationDate").setAutoWidth(true);

		workspace.setWidth("70%");
		workspace.getStyle().set("minHeight", "150px");

		processedBox.setItems(Processed.values());
	}

	public void updateBacklogDataGrid(BacklogDataService backlogDataService) {
		backlogDataGrid.setItems(
				backlogDataService.getBacklogDataByUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID)));
	}

}
