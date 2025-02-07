package com.mb.finance.service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.config.ConversionRequest;
import com.mb.finance.config.ExpenseDto;
import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.IncomeDto;
import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;
import com.mb.finance.config.UserRegistrationDto;
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
	public FinanceUser saveUser(UserRegistrationDto userRegistrationDto)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		FinanceUser newUser = new FinanceUser();
		newUser.setEmail(userRegistrationDto.getEmail());
		newUser.setFirstName(userRegistrationDto.getFirstName());
		newUser.setLastName(userRegistrationDto.getLastName());
		newUser.setUserId(userRegistrationDto.getUserId());
		newUser.setPassword(userRegistrationDto.getPassword());
		newUser.setRoles("ROLE_USER");

		FinanceUser financeUser = financeUserService.saveFinanceUser(newUser);

		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountNumber("CASH");
		bankAccount.setUserId(newUser.getUserId());
		bankAccount.setBankName("CASH");
		bankAccount.setBalance(BigDecimal.ZERO);
		bankAccount.setCreationDate(LocalDate.now());
		bankAccountService.saveBankAccount(bankAccount);

		return financeUser;
	}

	/*
	 * public Boolean login(LoginDto request) throws NoSuchAlgorithmException,
	 * InvalidKeySpecException { Long id =
	 * financeUserService.authenticate(request.getUserId(), request.getPassword());
	 * if (id != null) { return true; } return null; }
	 */

	@Transactional
	public void addIncome(IncomeDto incomeDto) throws Exception {

		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUserByUserId(incomeDto.getUserId());
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

		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUserByUserId(expenseDto.getUserId());
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

	@Transactional
	public void deleteIncome(String userId, Map<String, Object> requestMap) throws Exception {
		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUserByUserId(userId);
		if (StringUtils.isBlank(userId) && optionalFinanceUser.isEmpty()) {
			throw new Exception("User doesnt exist");
		}

		List<Income> incomeList = new ArrayList<>();
		if (requestMap.get("incomes") instanceof List) {

			incomeList = incomeService.deleteIncome(userId, (List<String>) requestMap.get("incomes"));

		} else if (requestMap.get("incomes") instanceof String) {
			incomeList = incomeService.deleteIncome(userId, Arrays.asList((String) requestMap.get("incomes")));
		}

		List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);

		for (Income income : incomeList) {
			BankAccount bankAccount = bankAccounts.stream()
					.filter(e -> e.getAccountNumber().equals(income.getDepositedIn())).findAny().get();
			bankAccount.setBalance(bankAccount.getBalance().subtract(income.getAmount()));
		}

		bankAccountService.saveBankAccounts(bankAccounts);

	}

	@Transactional
	public void deleteExpense(String userId, Map<String, Object> requestMap) throws Exception {
		Optional<FinanceUser> optionalFinanceUser = financeUserService.getUserByUserId(userId);
		if (StringUtils.isBlank(userId) && optionalFinanceUser.isEmpty()) {
			throw new Exception("User doesnt exist");
		}

		List<Expense> expenses = new ArrayList<>();
		if (requestMap.get("expenses") instanceof List) {

			expenses = expenseService.deleteExpense(userId, (List<String>) requestMap.get("expenses"));

		} else if (requestMap.get("expenses") instanceof String) {
			expenses = expenseService.deleteExpense(userId, Arrays.asList((String) requestMap.get("expenses")));
		}

		List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);

		for (Expense expense : expenses) {
			BankAccount bankAccount = bankAccounts.stream()
					.filter(e -> e.getAccountNumber().equals(expense.getWithdrawnFrom())).findAny().get();
			bankAccount.setBalance(bankAccount.getBalance().add(expense.getAmount()));
		}

		bankAccountService.saveBankAccounts(bankAccounts);

	}

	@Transactional
	public void convert(ConversionRequest conversionRequest) throws Exception {
		if (StringUtils.isBlank(conversionRequest.getUserId())
				|| StringUtils.isBlank(conversionRequest.getWithdrawnFrom())
				|| StringUtils.isBlank(conversionRequest.getDepositedIn())
				|| (conversionRequest.getAmount().compareTo(BigDecimal.ZERO) == -1)) {
			throw new Exception("Invalid request");
		}

		LocalDate currentLocalDate = LocalDate.now();

		BankAccount withdrawnBA = bankAccountService.findByUserIdAndAccountNumber(conversionRequest.getUserId(),
				conversionRequest.getWithdrawnFrom());
		BankAccount depositedBA = bankAccountService.findByUserIdAndAccountNumber(conversionRequest.getUserId(),
				conversionRequest.getDepositedIn());

		Expense expense = new Expense();
		expense.setAmount(conversionRequest.getAmount());
		expense.setComments("transfer");
		expense.setCreationDate(currentLocalDate);
		expense.setExpenseDate(conversionRequest.getConversionDate());
		expense.setExpenseOccurance(Occurance.OCCASSIONAL);
		expense.setExpenseType(ExpenseType.CONVERSION);
		expense.setUserId(conversionRequest.getUserId());
		expense.setWithdrawnFrom(conversionRequest.getWithdrawnFrom());

		expenseService.addExpense(expense);

		withdrawnBA.setBalance(withdrawnBA.getBalance().subtract(conversionRequest.getAmount()));

		Income income = new Income();
		income.setAmount(conversionRequest.getAmount());
		income.setComments("transfer");
		income.setCreationDate(currentLocalDate);
		income.setIncomeDate(conversionRequest.getConversionDate());
		income.setIncomeOccurance(Occurance.OCCASSIONAL);
		income.setIncomeType(IncomeType.CONVERSION);
		income.setUserId(conversionRequest.getUserId());
		income.setDepositedIn(conversionRequest.getDepositedIn());

		incomeService.addNewIncome(income);

		depositedBA.setBalance(depositedBA.getBalance().add(conversionRequest.getAmount()));

		bankAccountService.saveBankAccount(depositedBA);
		bankAccountService.saveBankAccount(withdrawnBA);
	}

	public void delete(String transactionType, String id) {

	}

	public BigDecimal getBalanceCurrentMonth(String userId) {

		LocalDate currentLocalDate = LocalDate.now();
		BigDecimal currentMonthIncome = incomeService.getAllIncomeForCurrentMonthForUser(userId, currentLocalDate);
		BigDecimal currentMonthExpense = expenseService.getAllExpensesForCurrentMonthForUser(userId, currentLocalDate);

		return currentMonthIncome.subtract(currentMonthExpense);

	}

	public BigDecimal getTotalBalance(String userId) {

		BigDecimal totalIncome = BigDecimal.ZERO;

		List<BankAccount> bankAccountList = bankAccountService.getAllAccountsForUserId(userId);
		for (BankAccount bankAccount : bankAccountList) {
			totalIncome = totalIncome.add(bankAccount.getBalance());
		}

		return totalIncome;

	}
}
