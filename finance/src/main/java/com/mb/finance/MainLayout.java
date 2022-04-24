package com.mb.finance;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends AppLayout implements RouterLayout {

	public MainLayout() {
		createHeader();
		createDrawer();
	}

	private void createHeader() {
		H1 logo = new H1("Finance");
		logo.addClassName("logo");

		Button logoutButton = new Button("Logout");
		logoutButton.addClickListener(event -> {
			VaadinSession.getCurrent().getSession().invalidate();
			VaadinSession.getCurrent().close();
			UI.getCurrent().getPage().setLocation("login");
		});
		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);

		header.expand(logo);
		header.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		header.setWidth("100%");
		header.addClassName("header");

		addToNavbar(header);

	}

	private void createDrawer() {

		RouterLink incomeAddLink = new RouterLink("Add Income", AddIncomeView.class);
		
		RouterLink allIncomeLink = new RouterLink("Show All Income", ShowIncomeView.class);

		RouterLink expenseAddLink = new RouterLink("Add Expense", AddExpenseView.class);

		RouterLink allExpenseLink = new RouterLink("Show All Expense", ShowExpenseView.class);

		RouterLink balanceSheetLink = new RouterLink("Show Balance Sheet", BalanceSheetView.class);

		RouterLink backlogDataServiceLink = new RouterLink("Backlog Data", BacklogDataView.class);
		
		RouterLink bankAccountAddLink = new RouterLink("Add Bank Account", AddBankAccountView.class);

		addToDrawer(new VerticalLayout(balanceSheetLink, allIncomeLink, allExpenseLink, incomeAddLink, expenseAddLink, backlogDataServiceLink, bankAccountAddLink));
	}

}
