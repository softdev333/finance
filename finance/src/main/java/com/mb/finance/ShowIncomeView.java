package com.mb.finance;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.entities.Income;
import com.mb.finance.service.IncomeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.mb.finance.config.Constants.USER_ID;

@Route(value = "showincome", layout = MainLayout.class)
@PageTitle("Finance : Income")
public class ShowIncomeView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	IncomeService incomeService;

	H2 pageTitle = new H2("All Income");

	TextField totalIncomeField = new TextField("Total Income for current month");

	Grid<Income> incomeGrid = new Grid<>(Income.class);

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public ShowIncomeView(IncomeService incomeService) {
		incomeGrid.setColumns("amount", "incomeType", "incomeDate", "incomeOccurance", "depositedIn", "comments");
		updateGridColumnSize();
		incomeGrid.setWidth("70%");
		incomeGrid.addComponentColumn(item -> createRemoveButton(item, incomeService)).setHeader("Action");

		updateTotalIncome(incomeService);
		totalIncomeField.setReadOnly(true);
		totalIncomeField.setWidth("20%");

		updateIncomeGrid(incomeService);
		add(pageTitle, totalIncomeField, incomeGrid);
	}

	private Button createRemoveButton(Income item, IncomeService incomeService) {
		Span content = new Span("You sure, you want to delete this ?");

		NativeButton buttonInside = new NativeButton("No");
		NativeButton buttonInside2 = new NativeButton("Yes");

		Notification notification = new Notification(content, buttonInside2, buttonInside);
		notification.setDuration(10000);
		notification.setPosition(Position.MIDDLE);

		buttonInside.addClickListener(event -> notification.close());

		buttonInside2.addClickListener(event -> {
			incomeService.deleteIncome(item);
			updateIncomeGrid(incomeService);
			updateTotalIncome(incomeService);
			notification.close();
			Notification successNotification = new Notification("Income successfully deleted", 5000, Position.MIDDLE);
			successNotification.open();
		});

		Button button = new Button("Delete", clickEvent -> {
			notification.open();
		});
		return button;
	}

	public void updateGridColumnSize() {
		incomeGrid.getColumnByKey("amount").setAutoWidth(true);
		incomeGrid.getColumnByKey("incomeType").setAutoWidth(true);
		incomeGrid.getColumnByKey("incomeDate").setAutoWidth(true);
		incomeGrid.getColumnByKey("incomeOccurance").setAutoWidth(true);
		incomeGrid.getColumnByKey("depositedIn").setAutoWidth(true);
		incomeGrid.getColumnByKey("comments").setAutoWidth(true);
	}

	public void updateIncomeGrid(IncomeService incomeService) {
		incomeGrid.setItems(
				incomeService.getAllIncomeByUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID)));
		updateGridColumnSize();
	}

	public void updateTotalIncome(IncomeService incomeService) {
		totalIncomeField.setValue(incomeService.getAllIncomeForCurrentMonthForAUser((String) VaadinSession.getCurrent().getAttribute(USER_ID), LocalDate.now()).toPlainString());
	}

}
