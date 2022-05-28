package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Income;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.IncomeService;
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

@Route(value = "addincome", layout = MainLayout.class)
@PageTitle("Finance : Add Income")
public class AddIncomeView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	IncomeService incomeService;
	
	@Autowired
	BankAccountService bankAccountService;

	H2 pageTitle = new H2("Add Income");

	TextField amountTextField = new TextField("Enter Amount");

	ComboBox<String> depositedInOptionsField = new ComboBox<String>("Where did you deposit it ?");

	TextArea commentsTextArea = new TextArea("Enter comments");

	ComboBox<IncomeType> incomeTypeBox = new ComboBox<IncomeType>("Type of income");

	ComboBox<Occurance> incomeOccurenceBox = new ComboBox<Occurance>("Occurence of income");

	DatePicker incomeDatePicker = new DatePicker("Income Date");

	Button submitButton = new Button("Add Income");

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public AddIncomeView(BankAccountService bankAccountService) {

		incomeTypeBox.setItems(IncomeType.values());
		incomeTypeBox.setWidth("35%");
		incomeOccurenceBox.setItems(Occurance.values());
		incomeOccurenceBox.setWidth("35%");
		
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);
		List<String> bankAccountNumbers = bankAccounts.stream().map(e->e.getAccountNumber()).collect(Collectors.toList());
		
		depositedInOptionsField.setItems(bankAccountNumbers);

		incomeDatePicker.setWidth("30%");

		commentsTextArea.setWidth("30%");
		commentsTextArea.getStyle().set("minHeight", "125px");

		submitButton.addClickListener(event -> {
			createIncomeObject();
		});

		HorizontalLayout h1 = new HorizontalLayout(amountTextField, depositedInOptionsField);

		HorizontalLayout h2 = new HorizontalLayout(incomeTypeBox, incomeOccurenceBox, incomeDatePicker);

		add(pageTitle, h1, h2, commentsTextArea, submitButton);
	}

	public void createIncomeObject() {
		Income income = new Income();
		income.setAmount(new BigDecimal(amountTextField.getValue()));
		income.setIncomeDate(incomeDatePicker.getValue());
		income.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
		income.setIncomeType(incomeTypeBox.getValue());
		income.setComments(commentsTextArea.getValue());
		income.setIncomeOccurance(incomeOccurenceBox.getValue());
		income.setDepositedIn(depositedInOptionsField.getValue());

		try {
			incomeService.addNewIncome(income);
			Notification successNotification = new Notification("Income Added successfully", 5000, Position.MIDDLE);
			successNotification.open();
			setDefaultValue();

		} catch (Exception e) {
			Notification errorNotification = new Notification("Error in Adding Income", 5000, Position.MIDDLE);
			errorNotification.open();

			e.printStackTrace();
		}
	}

	public void setDefaultValue() {
		amountTextField.setValue("");
		depositedInOptionsField.setValue(null);
		commentsTextArea.setValue("");
		incomeTypeBox.setValue(null);
		incomeOccurenceBox.setValue(null);
		incomeDatePicker.setValue(LocalDate.now());
	}

}
