package com.mb.finance;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.entities.Expense;
import com.mb.finance.service.ExpenseService;
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

@Route(value = "showexpense", layout = MainLayout.class)
@PageTitle("Finance : Expenses")
public class ShowExpenseView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	ExpenseService expenseService;

	H2 pageTitle = new H2("All Expenses");

	TextField totalExpenseField = new TextField("Total Expense for current month");

	Grid<Expense> expenseGrid = new Grid<>(Expense.class);

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public ShowExpenseView(ExpenseService expenseService) {
		expenseGrid.setColumns("amount", "expenseType", "expenseDate", "expenseOccurance", "withdrawnFrom", "comments");
		expenseGrid.setWidth("70%");
		updateGridColumnSize();

		expenseGrid.addComponentColumn(item -> createRemoveButton(item, expenseService)).setHeader("Action");

		updateTotalExpense(expenseService);
		totalExpenseField.setReadOnly(true);
		totalExpenseField.setWidth("20%");

		updateExpenseGrid(expenseService);
		add(pageTitle, totalExpenseField, expenseGrid);
	}

	private Button createRemoveButton(Expense item, ExpenseService expenseService) {
		Span content = new Span("You sure, you want to delete this ?");

		NativeButton buttonInside = new NativeButton("No");
		NativeButton buttonInside2 = new NativeButton("Yes");

		Notification notification = new Notification(content, buttonInside2, buttonInside);
		notification.setDuration(10000);
		notification.setPosition(Position.MIDDLE);

		buttonInside.addClickListener(event -> notification.close());

		buttonInside2.addClickListener(event -> {
			expenseService.deleteExpense(item);
			updateExpenseGrid(expenseService);
			updateTotalExpense(expenseService);
			notification.close();
			Notification successNotification = new Notification("Expense successfully deleted", 5000, Position.MIDDLE);
			successNotification.open();
		});

		Button button = new Button("Delete", clickEvent -> {
			notification.open();
		});
		return button;
	}

	public void updateGridColumnSize() {
		expenseGrid.getColumnByKey("amount").setAutoWidth(true);
		expenseGrid.getColumnByKey("expenseType").setAutoWidth(true);
		expenseGrid.getColumnByKey("expenseDate").setAutoWidth(true);
		expenseGrid.getColumnByKey("expenseOccurance").setAutoWidth(true);
		expenseGrid.getColumnByKey("withdrawnFrom").setAutoWidth(true);
		expenseGrid.getColumnByKey("comments").setAutoWidth(true);
	}

	public void updateExpenseGrid(ExpenseService expenseService) {
		expenseGrid.setItems(
				expenseService.getExpensesByUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID)));
		updateGridColumnSize();
	}

	public void updateTotalExpense(ExpenseService expenseService) {
		totalExpenseField
				.setValue(expenseService
						.getAllExpensesForCurrentMonthForUserId(
								(String) VaadinSession.getCurrent().getAttribute(USER_ID), LocalDate.now())
						.toPlainString());
	}

}
