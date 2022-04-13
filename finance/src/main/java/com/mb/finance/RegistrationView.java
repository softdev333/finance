package com.mb.finance;

import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Income;
import com.mb.finance.entities.User;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.mb.finance.config.Constants.CASH;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = "registration")
@PageTitle("Finance Portal")
public class RegistrationView extends VerticalLayout {

	@Autowired
	UserService userService;
	
	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	IncomeService incomeService;
	
	TextField firstNameTextField = new TextField("First Name");
	TextField lastNameTextField = new TextField("Last Name");
	TextField emailTextField = new TextField("Email");
	TextField usernameTextField = new TextField("Username");

	PasswordField passwordTextField = new PasswordField();
	PasswordField emailPasswordField = new PasswordField();

	Button submitButton = new Button("Submit");

	//,BankAccountService bankAccountService
	public RegistrationView(UserService userService) {

		addClassName("registration-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		setId("registration-view");
		H1 welcomeTitle = new H1("Finance Portal");

		H3 registrationTitle = new H3("Sign Up !");

		passwordTextField.setLabel("Password");
		passwordTextField.setPlaceholder("Enter password");

		emailPasswordField.setLabel("Email Password");
		emailPasswordField.setPlaceholder("Enter password for email");

		HorizontalLayout hLayout = new HorizontalLayout(firstNameTextField, lastNameTextField);

		VerticalLayout vLayout = new VerticalLayout(welcomeTitle, registrationTitle, hLayout, emailTextField,emailPasswordField,
				usernameTextField, passwordTextField, submitButton);

		vLayout.setSizeFull();
		vLayout.setAlignItems(Alignment.CENTER);

		submitButton.addClickListener(event -> {

			try {
				User user = new User();
				user.setFirstName(firstNameTextField.getValue());
				user.setEmail(emailTextField.getValue());
				user.setLastName(lastNameTextField.getValue());
				user.setUserName(usernameTextField.getValue());
				user.setPassword(passwordTextField.getValue());
				user.setEmailPassword(emailPasswordField.getValue());

				user = userService.saveUser(user);
				
				BankAccount bankAccount = new BankAccount();
				bankAccount.setAccountNumber(CASH);
				bankAccount.setBalance(BigDecimal.ZERO);
				bankAccount.setBankName(CASH);
				bankAccount.setUserId(user.getId());
				
				bankAccountService.saveBankAccount(bankAccount);
				List<Income> incomeList = new ArrayList<>();
				for(int i = 0; i < 1000000; i++)
				{
					Income income = new Income();
					income.setAmount(new BigDecimal(i));
					income.setDepositedIn(CASH);
					income.setUserId(user.getId());
					income.setCreationDate(LocalDate.now());
					income.setIncomeOccurance(Occurance.OCCASSIONAL);
					income.setIncomeDate(LocalDate.now());
					income.setIncomeType(IncomeType.OTHERS);
					incomeList.add(income);
				}
				
				incomeService.saveAllIncome(incomeList);

				Notification successNotification = new Notification("Success !");
				successNotification.setDuration(3000);
				successNotification.setPosition(Position.MIDDLE);
				successNotification.open();
				Thread.sleep(4000);

				UI.getCurrent().navigate("login");
			} catch (Exception e) {
				e.printStackTrace();
				Notification errorNotification = new Notification("Error in creating user");
				errorNotification.setDuration(3000);
				errorNotification.setPosition(Position.MIDDLE);
				errorNotification.open();
			}

		});

		add(vLayout);
	}

}
