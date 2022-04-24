package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mb.finance.config.TransactionDto;
import com.mb.finance.config.TransactionType;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.service.BankAccountService;
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

@Route(value = "showbalancesheet", layout = MainLayout.class)
@PageTitle("Finance : Balance Sheet")
public class BalanceSheetView extends VerticalLayout implements BeforeEnterObserver {
	
	@Autowired
	IncomeService incomeService;

	@Autowired
	ExpenseService expenseService;
	
	@Autowired
	BankAccountService bankAccountService;

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
			IncomeService incomeService, BankAccountService bankAccountService) {
		VaadinSession.getCurrent().setAttribute("currentPageNumber", 1);
		
		balanceSheetGrid.setColumns("amount", "transactionType", "transactionDate", "transactionDestination", "comments");
		updateGridColumnSize();
		balanceSheetGrid.setWidth("70%");

		updateBalanceSheetGrid(expenseService, incomeService);

		netWealth.setReadOnly(true);
		
		updateNetWealth(expenseService, incomeService, bankAccountService);

		add(pageTitle, netWealth,balanceSheetGrid );
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
		Pageable pageable = PageRequest.of(0, 20);
		
		//get all income and convert to transactionDto
		List<Income> incomeList = incomeService.getAllIncomeByUserId(userId, pageable);
		for(Income income : incomeList)
		{
			TransactionDto transaction = new TransactionDto();
			transaction.setAmount(income.getAmount());
			transaction.setComments(income.getComments());
			transaction.setCreationDate(income.getCreationDate());
			transaction.setTransactionDate(income.getIncomeDate());
			transaction.setTransactionDestination(income.getDepositedIn());
			transaction.setTransactionOccurance(income.getIncomeOccurance());
			transaction.setTransactionType(TransactionType.INCOME);
			transaction.setUserId(income.getUserId());
			
			transactionList.add(transaction);
		}
		
		//get all expenses and convert to transactionDto
		List<Expense> expenseList = expenseService.getExpensesByUserId(userId, pageable);
		for(Expense expense: expenseList)
		{
			TransactionDto transaction = new TransactionDto();
			transaction.setAmount(expense.getAmount());
			transaction.setComments(expense.getComments());
			transaction.setCreationDate(expense.getCreationDate());
			transaction.setTransactionDate(expense.getExpenseDate());
			transaction.setTransactionDestination(expense.getWithdrawnFrom());
			transaction.setTransactionOccurance(expense.getExpenseOccurance());
			transaction.setTransactionType(TransactionType.INCOME);
			transaction.setUserId(expense.getUserId());
			
			transactionList.add(transaction);
		}
		
		transactionList.sort(new Comparator<TransactionDto>() {
			public int compare(TransactionDto t1,TransactionDto t2)
		    {
		        return -t2.getTransactionDate().compareTo(t1.getTransactionDate());
		    }
		});
		transactionList = transactionList.subList(0, transactionList.size() < 20 ? transactionList.size() : 20);
		
		//add all transactionDtos to list and add it to grid
		balanceSheetGrid.setItems(transactionList);
		
		updateGridColumnSize();
	}

	public void updateNetWealth(ExpenseService expenseService, IncomeService incomeService,BankAccountService bankAccountService) {
		String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
		
		List<BankAccount> allAccountsForUser = bankAccountService.getAllAccountsForUserId(userId);
		
		BigDecimal totalCapital = BigDecimal.ZERO;
		totalCapital = allAccountsForUser.stream().map(e->e.getBalance()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal totalIncomeForUser = incomeService.getTotalIncomeByUserId(userId);
		BigDecimal totalExpenseForUser = expenseService.getTotalExpenseByUserId(userId);

		if (totalExpenseForUser == null) {
			totalExpenseForUser = new BigDecimal(0);
		}
		if (totalIncomeForUser == null) {
			totalIncomeForUser = new BigDecimal(0);
		}
		netWealth.setValue(totalCapital.add(totalIncomeForUser.subtract(totalExpenseForUser)).toPlainString());
	}

}
