package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mb.finance.entities.Expense;
import com.mb.finance.service.ExpenseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "showexpense", layout = MainLayout.class)
@PageTitle("Finance : Expenses")
public class ShowExpenseView extends VerticalLayout implements BeforeEnterObserver {
	
	public static final String EXPENSE_PAGE_NUMBER= "expensePageNumber";

	@Autowired
	ExpenseService expenseService;

	H2 pageTitle = new H2("All Expenses");

	TextField totalExpenseField = new TextField("Total Expense for current month");

	Grid<Expense> expenseGrid = new Grid<>(Expense.class);
	
	Button currentButton = new Button("Latest");
	
	Button arrowLeftButton = new Button("Newer", new Icon(VaadinIcon.ARROW_LEFT));

	Button arrowRightButton = new Button("Past", new Icon(VaadinIcon.ARROW_RIGHT));

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public ShowExpenseView(ExpenseService expenseService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		VaadinSession.getCurrent().setAttribute(EXPENSE_PAGE_NUMBER, 0);
		expenseGrid.setColumns("amount", "expenseType", "expenseDate", "expenseOccurance", "withdrawnFrom", "comments");
		expenseGrid.setWidth("70%");
		updateGridColumnSize();

		expenseGrid.addComponentColumn(item -> createRemoveButton(item, expenseService)).setHeader("Action");

		updateTotalExpense(expenseService);
		totalExpenseField.setReadOnly(true);
		totalExpenseField.setWidth("20%");

		Pageable latestPageable = PageRequest.of(0, 20);
		List<Expense> expenseList = expenseService.getExpensesByUserId(userId, latestPageable);
		updateExpenseGrid(expenseList);
		
		HorizontalLayout hl1 = new HorizontalLayout();
		hl1.add(currentButton,arrowLeftButton,arrowRightButton);
		
		VerticalLayout vl1 = new VerticalLayout();
		vl1.add(hl1,expenseGrid);
		
		add(pageTitle, totalExpenseField, vl1);
	}

	private Button createRemoveButton(Expense item, ExpenseService expenseService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		Span content = new Span("You sure, you want to delete this ?");

		NativeButton buttonInside = new NativeButton("No");
		NativeButton buttonInside2 = new NativeButton("Yes");

		Notification notification = new Notification(content, buttonInside2, buttonInside);
		notification.setDuration(10000);
		notification.setPosition(Position.MIDDLE);

		buttonInside.addClickListener(event -> notification.close());

		buttonInside2.addClickListener(event -> {
			expenseService.deleteExpense(item);
			Integer expensePageNumber = (Integer) VaadinSession.getCurrent().getAttribute(EXPENSE_PAGE_NUMBER);
			Pageable latestPageable = PageRequest.of(expensePageNumber < 0 ? 0 : expensePageNumber , 20);
			List<Expense> expenseList = expenseService.getExpensesByUserId(userId, latestPageable);
			updateExpenseGrid(expenseList);
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

	public void updateExpenseGrid(List<Expense> expenseList) {
		expenseGrid.setItems(expenseList);
		updateGridColumnSize();
	}

	public void updateTotalExpense(ExpenseService expenseService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		totalExpenseField.setValue(expenseService.getAllExpensesForCurrentMonthForUserId(userId, LocalDate.now()).toPlainString());
	}

}
