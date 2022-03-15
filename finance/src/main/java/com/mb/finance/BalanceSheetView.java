package com.mb.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.TransactionDto;
import com.mb.finance.config.TransactionType;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.IncomeService;
import com.vaadin.flow.component.grid.Grid;
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

import static com.mb.finance.config.Constants.USER_ID;

@Route(value = "showbalancesheet", layout = MainLayout.class)
@PageTitle("Finance : Balance Sheet")
public class BalanceSheetView extends VerticalLayout implements BeforeEnterObserver {

	@Autowired
	IncomeService incomeService;

	@Autowired
	ExpenseService expenseService;

	H2 pageTitle = new H2("Balance Sheet");

	TextField netWealth = new TextField("Net Wealth");

	Grid<TransactionDto> balanceSheetGrid = new Grid<>(TransactionDto.class);

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
			Notification errorNotification = new Notification("No User. Proceed to login", 3000, Position.MIDDLE);
			errorNotification.open();
			arg0.rerouteTo(LoginView.class);
		}
	}

	public BalanceSheetView( ExpenseService expenseService,
			IncomeService incomeService) {
		balanceSheetGrid.setColumns("amount", "transactionType", "transactionDate", "transactionDestination", "comments");
		updateGridColumnSize();
		balanceSheetGrid.setWidth("70%");

		updateBalanceSheetGrid(expenseService, incomeService);

		netWealth.setReadOnly(true);
		updateNetWealth(expenseService, incomeService);

		add(pageTitle, netWealth, balanceSheetGrid);
	}

	public void updateGridColumnSize() {
		balanceSheetGrid.getColumnByKey("amount").setAutoWidth(true);
		balanceSheetGrid.getColumnByKey("transactionType").setAutoWidth(true);
		balanceSheetGrid.getColumnByKey("transactionDate").setAutoWidth(true);
		balanceSheetGrid.getColumnByKey("transactionDestination").setAutoWidth(true);
		balanceSheetGrid.getColumnByKey("comments").setAutoWidth(true);
	}

	public void updateBalanceSheetGrid(ExpenseService expenseService, IncomeService incomeService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		
		List<TransactionDto> transactionList = new ArrayList<TransactionDto>();
		
		//get all income and convert to transactionDto
		List<Income> incomeList = incomeService.getAllIncomeByUserId(userId);
		incomeList.forEach(e->{
			TransactionDto transaction = new TransactionDto();
			transaction.setAmount(e.getAmount());
			transaction.setComments(e.getComments());
			transaction.setCreationDate(e.getCreationDate());
			transaction.setTransactionDate(e.getIncomeDate());
			transaction.setTransactionDestination(e.getDepositedIn());
			transaction.setTransactionOccurance(e.getIncomeOccurance());
			transaction.setTransactionType(TransactionType.INCOME);
			transaction.setUserId(e.getUserId());
			
			transactionList.add(transaction);
		});
		
		//get all expenses and convert to transactionDto
		List<Expense> expenseList = expenseService.getExpensesByUserId(userId);
		expenseList.forEach(e->{
			TransactionDto transaction = new TransactionDto();
			transaction.setAmount(e.getAmount());
			transaction.setComments(e.getComments());
			transaction.setCreationDate(e.getCreationDate());
			transaction.setTransactionDate(e.getExpenseDate());
			transaction.setTransactionDestination(e.getWithdrawnFrom());
			transaction.setTransactionOccurance(e.getExpenseOccurance());
			transaction.setTransactionType(TransactionType.INCOME);
			transaction.setUserId(e.getUserId());
			
			transactionList.add(transaction);
		});
		
		//add all transactionDtos to list and add it to grid
		balanceSheetGrid.setItems(transactionList);
		
		updateGridColumnSize();
	}

	public void updateNetWealth(ExpenseService expenseService, IncomeService incomeService) {
		BigDecimal totalIncomeForUser = incomeService
				.getTotalIncomeByUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
		BigDecimal totalExpenseForUser = expenseService
				.getTotalExpenseByUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));

		if (totalExpenseForUser == null) {
			totalExpenseForUser = new BigDecimal(0);
		}
		if (totalIncomeForUser == null) {
			totalIncomeForUser = new BigDecimal(0);
		}
		netWealth.setValue(totalIncomeForUser.subtract(totalExpenseForUser).toPlainString());
	}

}
