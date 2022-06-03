package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mb.finance.entities.Income;
import com.mb.finance.service.IncomeService;
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

@Route(value = "showincome", layout = MainLayout.class)
@PageTitle("Finance : Income")
public class ShowIncomeView extends VerticalLayout implements BeforeEnterObserver {
	
	public static final String INCOME_PAGE_NUMBER= "incomePageNumber";

	@Autowired
	IncomeService incomeService;

	H2 pageTitle = new H2("All Income");

	TextField totalIncomeField = new TextField("Total Income for current month");

	Grid<Income> incomeGrid = new Grid<>(Income.class);
	
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

	public ShowIncomeView(IncomeService incomeService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		VaadinSession.getCurrent().setAttribute(INCOME_PAGE_NUMBER, 0);
		incomeGrid.setColumns("amount", "incomeType", "incomeDate", "incomeOccurance", "depositedIn", "comments");
		updateGridColumnSize();
		incomeGrid.setWidth("70%");
		incomeGrid.addComponentColumn(item -> createRemoveButton(item, incomeService)).setHeader("Action");

		updateTotalIncome(incomeService);
		totalIncomeField.setReadOnly(true);
		totalIncomeField.setWidth("20%");

		Pageable pageable = PageRequest.of(0, 20);
		List<Income> incomeList = incomeService.getAllIncomeByUserId(userId,pageable);
		updateIncomeGrid(incomeList);
		
		currentButton.addClickListener(event -> {
			Pageable latestPageable = PageRequest.of(0, 20);
			List<Income> updatedIncomeList = incomeService.getAllIncomeByUserId(userId, latestPageable);
			updateIncomeGrid(updatedIncomeList);
		});
		
		arrowLeftButton.addClickListener(event -> {
			Integer incomePageNumber = (Integer) VaadinSession.getCurrent().getAttribute(INCOME_PAGE_NUMBER);
			Integer updatedIncomePageNumber = incomePageNumber - 1;
			updatedIncomePageNumber = updatedIncomePageNumber < 0 ? 0 : updatedIncomePageNumber;
			Pageable latestPageable = PageRequest.of(updatedIncomePageNumber , 20);
			List<Income> updatedIncomeList = incomeService.getAllIncomeByUserId(userId, latestPageable);
			updateIncomeGrid(updatedIncomeList);
			VaadinSession.getCurrent().setAttribute(INCOME_PAGE_NUMBER, updatedIncomePageNumber);
		});
		
		arrowRightButton.addClickListener(event -> {
			Integer incomePageNumber = (Integer) VaadinSession.getCurrent().getAttribute(INCOME_PAGE_NUMBER);
			Integer updatedIncomePageNumber = incomePageNumber + 1;
			Pageable latestPageable = PageRequest.of(updatedIncomePageNumber < 0 ? 0 : updatedIncomePageNumber , 20);
			List<Income> updatedIncomeList = incomeService.getAllIncomeByUserId(userId, latestPageable);
			updateIncomeGrid(updatedIncomeList);
			VaadinSession.getCurrent().setAttribute(INCOME_PAGE_NUMBER, updatedIncomePageNumber < 0 ? 0 : updatedIncomePageNumber);
		});

		HorizontalLayout hl1 = new HorizontalLayout();
		hl1.add(currentButton,arrowLeftButton,arrowRightButton);
		
		VerticalLayout vl1 = new VerticalLayout();
		vl1.add(hl1,incomeGrid);
		
		add(pageTitle, totalIncomeField, vl1);
	}

	private Button createRemoveButton(Income item, IncomeService incomeService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		Span content = new Span("You sure, you want to delete this ?");

		NativeButton buttonInside = new NativeButton("No");
		NativeButton buttonInside2 = new NativeButton("Yes");

		Notification notification = new Notification(content, buttonInside2, buttonInside);
		notification.setDuration(10000);
		notification.setPosition(Position.MIDDLE);

		buttonInside.addClickListener(event -> notification.close());

		buttonInside2.addClickListener(event -> {
			incomeService.deleteIncome(item);
			Integer incomePageNumber = (Integer) VaadinSession.getCurrent().getAttribute(INCOME_PAGE_NUMBER);
			Pageable latestPageable = PageRequest.of(incomePageNumber < 0 ? 0 : incomePageNumber , 20);
			List<Income> incomeList = incomeService.getAllIncomeByUserId(userId,latestPageable);
			updateIncomeGrid(incomeList);
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

	public void updateIncomeGrid(List<Income> incomeList) {
		incomeGrid.setItems(incomeList);
		updateGridColumnSize();
	}

	public void updateTotalIncome(IncomeService incomeService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		totalIncomeField.setValue(incomeService.getAllIncomeForCurrentMonthForUser(userId, LocalDate.now()).toPlainString());
	}

}
