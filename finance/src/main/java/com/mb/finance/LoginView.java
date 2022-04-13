package com.mb.finance;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.entities.Income;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.time.LocalDate;

@Route(value = "login")
@PageTitle("Finance : Login")
public class LoginView extends VerticalLayout {

	@Autowired
	UserService userService;
	
	@Autowired
	IncomeService incomeService;

	public LoginView(UserService userService) {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		H2 loginWelcome = new H2("Login");

		TextField usernameField = new TextField();
		usernameField.setPlaceholder("Username");
		PasswordField passwordField = new PasswordField();
		passwordField.setPlaceholder("Password");

		Button submitButton = new Button("Submit");

		Button loginRedirect = new Button("New User ? Sign Up here");
		loginRedirect.addClickListener(event -> {
			UI.getCurrent().navigate("registration");
		});

		submitButton.addClickListener(event -> {
			String userId = userService.authenticate(usernameField.getValue(), passwordField.getValue());
			if (!StringUtils.isEmpty(userId)) {
					
				VaadinSession.getCurrent().setAttribute(USER_ID, userId);
				UI.getCurrent().navigate("showbalancesheet");
			} else {
				Notification errorNotification = new Notification("Wrong Credentials ! Try Again");
				errorNotification.setDuration(2000);
				errorNotification.open();
				usernameField.setValue("");
				passwordField.setValue("");
			}
		});

		add(new H1("Finance Portal"), loginWelcome, usernameField, passwordField, submitButton, loginRedirect);
	}

}
