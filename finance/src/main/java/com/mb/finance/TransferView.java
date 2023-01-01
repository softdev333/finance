package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.IncomeType;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.IncomeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
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

@Route(value = "transfer", layout = MainLayout.class)
@PageTitle("Finance : Transfer Money")
public class TransferView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    ExpenseService expenseService;

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    IncomeService incomeService;

    H2 pageTitle = new H2("Transfer Money");

    ComboBox<String> transferFrom = new ComboBox<String>("Account to transfer from");

    ComboBox<String> transferTo = new ComboBox<String>("Account to transfer to");
    
    TextField amountTextField = new TextField("Enter Amount");

    Button submitButton = new Button("Transfer");

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {

	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    public TransferView(BankAccountService bankAccountService, ExpenseService expenseService,
	    IncomeService incomeService) {
	String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
	List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);
	List<String> bankAccountNumbers = bankAccounts.stream().map(e -> e.getAccountNumber())
		.collect(Collectors.toList());

	transferFrom.setItems(bankAccountNumbers);
	transferTo.setItems(bankAccountNumbers);

	submitButton.addClickListener(event -> {
	    try {
		transferAmount(bankAccountService, expenseService, incomeService);
	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	});

	HorizontalLayout hl = new HorizontalLayout(transferFrom, transferTo);

	add(pageTitle, hl, amountTextField,submitButton);
    }

    private void transferAmount(BankAccountService bankAccountService, ExpenseService expenseService,
	    IncomeService incomeService) throws Exception {
	String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
	
	Income income = new Income();
	income.setAmount(new BigDecimal(amountTextField.getValue()));
	income.setIncomeDate(LocalDate.now());
	income.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
	income.setIncomeType(IncomeType.CONVERSION);
	income.setDepositedIn(transferTo.getValue());
	
	Expense expense = new Expense();
	expense.setUserId((String) VaadinSession.getCurrent().getAttribute(USER_ID));
	expense.setAmount(new BigDecimal(amountTextField.getValue()));
	expense.setExpenseType(ExpenseType.CONVERSION);
	expense.setExpenseDate(LocalDate.now());
	expense.setWithdrawnFrom(transferFrom.getValue());
	
	BankAccount accountToTransferFrom = bankAccountService.findByUserIdAndAccountNumber(userId, transferFrom.getValue());
	BigDecimal updatedBalance = accountToTransferFrom.getBalance().subtract(new BigDecimal(amountTextField.getValue()));
	accountToTransferFrom.setBalance(updatedBalance);
	
	BankAccount accountToTransferTo = bankAccountService.findByUserIdAndAccountNumber(userId, transferTo.getValue());
	updatedBalance = accountToTransferTo.getBalance().add(new BigDecimal(amountTextField.getValue()));
	accountToTransferTo.setBalance(updatedBalance);
	
	bankAccountService.saveBankAccount(accountToTransferTo);
	bankAccountService.saveBankAccount(accountToTransferFrom);
	incomeService.addNewIncome(income);
	expenseService.addExpense(expense);

    }

}
