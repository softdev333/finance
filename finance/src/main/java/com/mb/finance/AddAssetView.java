package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.mb.finance.config.AssetType;
import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.Occurance;
import com.mb.finance.config.QuantityType;
import com.mb.finance.entities.Asset;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.service.AssetService;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.ExpenseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "addasset", layout = MainLayout.class)
@PageTitle("Finance : Add Asset")
public class AddAssetView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {

	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No User. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    H2 pageTitle = new H2("Asset");

    TextField nameField = new TextField("Enter Asset Name");

    TextField costField = new TextField("Enter Total Cost");

    ComboBox<AssetType> assetTypeBox = new ComboBox<AssetType>("Type of Asset");

    ComboBox<QuantityType> quantityTypeBox = new ComboBox<QuantityType>("Type of Quantity");

    TextField quantityField = new TextField("Enter Quantity of Asset");

    TextArea commentsTextArea = new TextArea("Enter comments");

    DatePicker assetDatePicker = new DatePicker("Date of Purchase");

    ComboBox<String> withdrawnFromOptionsField = new ComboBox<String>("How did you pay ?");

    Button submitButton = new Button("Submit");

    public AddAssetView(AssetService assetService, BankAccountService bankAccountService,
	    ExpenseService expenseService) {

	assetTypeBox.setItems(AssetType.values());
	quantityTypeBox.setItems(QuantityType.values());

	commentsTextArea.setWidth("40%");
	commentsTextArea.getStyle().set("minHeight", "150px");

	nameField.setWidth("60%");
	costField.setWidth("60%");
	quantityField.setWidth("60%");
	assetTypeBox.setWidth("40%");
	quantityTypeBox.setWidth("40%");

	String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
	List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);
	List<String> bankAccountNumbers = bankAccounts.stream().map(e -> e.getAccountNumber())
		.collect(Collectors.toList());

	withdrawnFromOptionsField.setItems(bankAccountNumbers);

	submitButton.addClickListener(event -> {
	    createAssetObject(assetService, expenseService);
	});

	HorizontalLayout hl1 = new HorizontalLayout(nameField, assetTypeBox, quantityTypeBox, quantityField);
	HorizontalLayout hl2 = new HorizontalLayout(costField, withdrawnFromOptionsField, assetDatePicker);

	add(pageTitle, hl1, hl2, commentsTextArea, submitButton);
    }

    private void createAssetObject(AssetService assetService, ExpenseService expenseService) {

	Asset asset = new Asset();
	asset.setName(nameField.getValue());
	asset.setAssetType(assetTypeBox.getValue());
	asset.setComments(commentsTextArea.getValue());
	asset.setCostPrice(new BigDecimal(costField.getValue()));
	asset.setCreationDate(LocalDate.now());
	asset.setPurchaseDate(assetDatePicker.getValue());
	asset.setQuantity(new BigDecimal(quantityField.getValue()));
	asset.setQuantityType(quantityTypeBox.getValue());
	asset.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));

	Expense expense = new Expense();
	expense.setAmount(new BigDecimal(costField.getValue()));
	expense.setComments(commentsTextArea.getValue());
	expense.setCreationDate(LocalDate.now());
	expense.setExpenseDate(assetDatePicker.getValue());
	expense.setExpenseOccurance(Occurance.OCCASSIONAL);
	expense.setExpenseType(ExpenseType.ASSET);
	expense.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
	expense.setWithdrawnFrom(withdrawnFromOptionsField.getValue());

	try {
	    assetService.saveAsset(asset);
	    expenseService.addExpense(expense);

	    setDefaultValues();

	    Notification successNotification = new Notification("Asset Added successfully", 5000, Position.MIDDLE);
	    successNotification.open();
	    
	} catch (Exception e) {
	    Notification errorNotification = new Notification("Error in Adding Asset", 5000, Position.MIDDLE);
	    errorNotification.open();
	    e.printStackTrace();
	}
    }

    public void setDefaultValues() {
	nameField.setValue("");
	costField.setValue("");
	quantityField.setValue("");
	commentsTextArea.setValue("");
	assetTypeBox.setValue(null);
	quantityTypeBox.setValue(null);
	assetDatePicker.setValue(null);
	withdrawnFromOptionsField.setValue(null);
    }

}
