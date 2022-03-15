package com.mb.finance;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.Occurance;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.mb.finance.config.Constants.USER_ID;

@Route(value = "addexpense", layout = MainLayout.class)
@PageTitle("Finance : Add Expense")
public class AddExpenseView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	ExpenseService expenseService;
	
	@Autowired
	BankAccountService bankAccountService;

	H2 pageTitle = new H2("Add Expense");

	TextField amountTextField = new TextField("Enter Amount");

	ComboBox<String> withdrawnFromOptionsField = new ComboBox<String>("How did you pay ?");

	TextArea commentsTextArea = new TextArea("Enter comments");

	ComboBox<ExpenseType> expenseTypeBox = new ComboBox<ExpenseType>("Type of Expense");

	ComboBox<Occurance> expenseOccurenceBox = new ComboBox<Occurance>("Occurence of Expense");

	DatePicker expenseDatePicker = new DatePicker("Set date of Expense");

	Button submitButton = new Button("Add Expense");
	
	RadioButtonGroup<String> radioGroupHelperText = new RadioButtonGroup<>();
	

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {

		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public AddExpenseView(ExpenseService expenseService, BankAccountService bankAccountService) {

		expenseTypeBox.setItems(ExpenseType.values());
		expenseOccurenceBox.setItems(Occurance.values());

		commentsTextArea.setWidth("40%");
		commentsTextArea.getStyle().set("minHeight", "150px");

		amountTextField.setWidth("60%");
		expenseTypeBox.setWidth("40%");
		
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);
		List<String> bankAccountNumbers = bankAccounts.stream().map(e->e.getAccountNumber()).collect(Collectors.toList());
		
		withdrawnFromOptionsField.setItems(bankAccountNumbers);

		submitButton.addClickListener(event -> {
			createExpenseObject(expenseService);
		});

		HorizontalLayout hl1 = new HorizontalLayout(amountTextField, expenseTypeBox);
		HorizontalLayout hl2 = new HorizontalLayout(expenseOccurenceBox, withdrawnFromOptionsField, expenseDatePicker);

		add(pageTitle, hl1, hl2, commentsTextArea, submitButton);
	}

	public void setDefaultValues() {
		
		radioGroupHelperText.setLabel("Paid in Cash");
		radioGroupHelperText.setItems("Yes", "No");
		
		amountTextField.setValue("");
		commentsTextArea.setValue("");
		expenseTypeBox.setValue(null);
		expenseOccurenceBox.setValue(null);
		expenseDatePicker.setValue(null);
		withdrawnFromOptionsField.setValue(null);
	}

	public void createExpenseObject(ExpenseService expenseService) {
		Expense expense = new Expense();
		expense.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
		expense.setAmount(new BigDecimal(amountTextField.getValue()));
		expense.setComments(commentsTextArea.getValue());
		expense.setExpenseOccurance(expenseOccurenceBox.getValue());
		expense.setExpenseType(expenseTypeBox.getValue());
		expense.setExpenseDate(expenseDatePicker.getValue());
		expense.setWithdrawnFrom(withdrawnFromOptionsField.getValue());

		try {
			expenseService.addExpense(expense, null);

			setDefaultValues();

			Notification successNotification = new Notification("Expense Added successfully", 5000, Position.MIDDLE);
			successNotification.open();

		} catch (Exception e) {

			Notification errorNotification = new Notification("Error in Adding Expense", 5000, Position.MIDDLE);
			errorNotification.open();

			e.printStackTrace();
		}

	}

}
