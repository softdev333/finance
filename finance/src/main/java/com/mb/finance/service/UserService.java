package com.mb.finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.config.ExpenseDto;
import com.mb.finance.config.IncomeDto;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.FinanceUser;
import com.mb.finance.entities.Income;

@Service
public class UserService {

	@Autowired
	FinanceUserService financeUserService;

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	IncomeService incomeService;

	@Autowired
	ExpenseService expenseService;

	@Transactional
	public FinanceUser saveUser(FinanceUser user) {
		FinanceUser financeUser = financeUserService.saveFinanceUser(user);

		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountNumber("CASH");
		bankAccount.setUserId(user.getUserId());
		bankAccount.setBankName("CASH");
		bankAccount.setBalance(BigDecimal.ZERO);

		bankAccountService.saveBankAccount(bankAccount);

		return financeUser;
	}

	@Transactional
	public void addIncome(IncomeDto incomeDto) throws Exception {

		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUser(incomeDto.getUserId());
		if (StringUtils.isBlank(incomeDto.getUserId()) && optionalFinanceUser.isEmpty()) {
			throw new Exception("User doesnt exist");
		}

		if (StringUtils.isBlank(incomeDto.getDepositedIn())) {
			throw new Exception("Bank Account cannot be empty");
		}

		BankAccount bankAccount = bankAccountService.findByUserIdAndAccountNumber(incomeDto.getUserId(),
				incomeDto.getDepositedIn());
		if (!Objects.nonNull(bankAccount)) {
			throw new Exception("Bank Account doesnt exist");
		}

		LocalDate currentDate = LocalDate.now();

		bankAccount.setBalance(bankAccount.getBalance().add(incomeDto.getAmount()));

		bankAccountService.saveBankAccount(bankAccount);

		Income income = new Income();
		income.setAmount(incomeDto.getAmount());
		income.setComments(incomeDto.getComments());
		income.setCreationDate(currentDate);
		income.setDepositedIn(incomeDto.getDepositedIn());
		income.setIncomeDate(incomeDto.getIncomeDate());
		income.setIncomeOccurance(incomeDto.getIncomeOccurance());
		income.setIncomeType(incomeDto.getIncomeType());
		income.setUserId(incomeDto.getUserId());

		incomeService.addNewIncome(income);

	}

	@Transactional
	public void addExpense(ExpenseDto expenseDto) throws Exception {

		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUser(expenseDto.getUserId());
		if (StringUtils.isBlank(expenseDto.getUserId()) && optionalFinanceUser.isEmpty()) {
			throw new Exception("User doesnt exist");
		}

		if (StringUtils.isBlank(expenseDto.getWithdrawnFrom())) {
			throw new Exception("Bank Account cannot be empty");
		}

		BankAccount bankAccount = bankAccountService.findByUserIdAndAccountNumber(expenseDto.getUserId(),
				expenseDto.getWithdrawnFrom());
		if (!Objects.nonNull(bankAccount)) {
			throw new Exception("Bank Account doesnt exist");
		}

		LocalDate currentDate = LocalDate.now();

		bankAccount.setBalance(bankAccount.getBalance().subtract(expenseDto.getAmount()));

		bankAccountService.saveBankAccount(bankAccount);

		Expense expense = new Expense();
		expense.setAmount(expenseDto.getAmount());
		expense.setComments(expenseDto.getComments());
		expense.setCreationDate(currentDate);
		expense.setWithdrawnFrom(expenseDto.getWithdrawnFrom());
		expense.setExpenseDate(expenseDto.getExpenseDate());
		expense.setExpenseOccurance(expenseDto.getExpenseOccurance());
		expense.setExpenseType(expenseDto.getExpenseType());
		expense.setUserId(expenseDto.getUserId());

		expenseService.addExpense(expense);

	}

}
