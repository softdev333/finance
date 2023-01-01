package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.entities.BankAccount;
import com.mb.finance.service.BankAccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "addbankaccount", layout = MainLayout.class)
@PageTitle("Finance : Add Expense")
public class AddBankAccountView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    BankAccountService bankAccountService;

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {

	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No User. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    H2 pageTitle = new H2("Bank Account");

    TextField nameField = new TextField("Enter Bank Name");

    TextField balanceField = new TextField("Enter Starting Balance");

    TextField accountNumberField = new TextField("Enter Account Number");

    Button submitButton = new Button("Submit");

    public AddBankAccountView(BankAccountService bankAccountService) {

	submitButton.addClickListener(event -> {
	    addBankAccount(bankAccountService);
	});

	add(pageTitle, nameField, accountNumberField, balanceField, submitButton);
    }

    public void addBankAccount(BankAccountService bankAccountService) {
	try {
	    BankAccount bankAccount = new BankAccount();
	    bankAccount.setAccountNumber(accountNumberField.getValue());
	    bankAccount.setBalance(new BigDecimal(balanceField.getValue()));
	    bankAccount.setBankName(nameField.getValue());
	    bankAccount.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));

	    bankAccountService.saveBankAccount(bankAccount);

	    Notification successNotification = new Notification("New Account Created", 5000, Position.MIDDLE);
	    successNotification.open();
	    setDefaultValue();
	} catch (Exception e) {
	    e.printStackTrace();
	    Notification errorNotification = new Notification("Error in adding new bank account");
	    errorNotification.setDuration(3000);
	    errorNotification.setPosition(Position.MIDDLE);
	    errorNotification.open();
	}
    }

    public void setDefaultValue() {
	nameField.setValue("");
	balanceField.setValue("");
	accountNumberField.setValue("");
    }

}
