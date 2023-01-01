package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.entities.Sheet;
import com.mb.finance.service.SheetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "sheetpanel", layout = MainLayout.class)
@PageTitle("Finance : Sheet Panel")
public class SheetsPanelView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    SheetService sheetService;

    TextArea sheetClientSecretJson = new TextArea("Sheets Client Secret Json");

    TextField sheetIdField = new TextField("Enter Google Sheet Id");
    IntegerField rowNumberField = new IntegerField("Enter Row number");

    Button submitButton = new Button("Submit");

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {
	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    public SheetsPanelView(SheetService sheetService) {
	H2 pageTitle = new H2("Google Sheets Data Panel");

	submitButton.addClickListener(event -> {

	    String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);

	    try {

		Sheet sheet = sheetService.findByUserId(userId);
		if (sheet == null) {
		    sheet = new Sheet();
		}
		sheet.setUserId(userId);
		sheet.setRowNumber(rowNumberField.getValue() == null ? 0 : rowNumberField.getValue());
		sheet.setSheetId(sheetIdField.getValue());
		sheet.setSheetsClientSecretJsonString(sheetClientSecretJson.getValue());

		sheetService.saveSheet(sheet);

		Notification successNotification = new Notification("Sheets Data Added successfully", 5000,
			Position.MIDDLE);
		successNotification.open();

	    } catch (Exception e) {
		Notification errorNotification = new Notification("Error in Adding Sheets Data", 5000, Position.MIDDLE);
		errorNotification.open();

		e.printStackTrace();
	    }

	});

	add(pageTitle, sheetClientSecretJson, sheetIdField, rowNumberField, submitButton);
    }

}
