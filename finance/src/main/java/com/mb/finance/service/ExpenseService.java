package com.mb.finance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.repository.ExpenseRepository;

@Service
public class ExpenseService {

	@Autowired
	ExpenseRepository expenseRepository;

	@Transactional
	public Expense addExpense(Expense expense) throws Exception {

		if (StringUtils.isBlank(expense.getUserId())) {
			throw new Exception("No User Found");
		}

		if (expense.getAmount().compareTo(BigDecimal.ZERO) == -1) {
			throw new Exception("Amount cannot be negative");
		}

		if (expense.getExpenseDate() == null) {
			expense.setExpenseDate(LocalDate.now());
		}

		expense.setCreationDate(LocalDate.now());
		return expenseRepository.save(expense);
	}

	public List<Expense> getExpensesByUserId(String userId) {
		List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
		return expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId, expenseTypesToExclude);
	}

	public List<Expense> getExpensesByUserId(String userId, Pageable pageable) {
		List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
		return expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId, expenseTypesToExclude,
				pageable);
	}

	public BigDecimal getAllExpensesForCurrentMonthForUser(String userId, LocalDate currentDate) {

		List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
		LocalDate start = currentDate.withDayOfMonth(1);
		LocalDate end = currentDate.withDayOfMonth(currentDate.getMonth().length(currentDate.isLeapYear()));
		List<Expense> expenses = expenseRepository.findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(userId,
				expenseTypesToExclude, start, end);

		BigDecimal resultBigDecimal = BigDecimal.ZERO;

		for (Expense expense : expenses) {
			resultBigDecimal = resultBigDecimal.add(expense.getAmount());
		}

		return resultBigDecimal;
	}

	public BigDecimal getTotalExpenseByUserId(String userId) {

		List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
		List<Expense> allExpenses = expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId,
				expenseTypesToExclude);
		BigDecimal result = new BigDecimal(0);

		for (Expense expense : allExpenses) {
			result = result.add(expense.getAmount());
		}

		return result;
	}

	public Boolean deleteExpense(Expense expense) {
		expenseRepository.delete(expense);
		return true;
	}

	public void saveAllExpenses(List<Expense> expenseList) {
		expenseRepository.saveAll(expenseList);
	}

	public BigDecimal getAverageMonthlySpend(String userId, LocalDate currentDate) {
		List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
		LocalDate start = currentDate.withDayOfMonth(1);
		LocalDate end = currentDate.withDayOfMonth(currentDate.getMonth().length(currentDate.isLeapYear()));

		List<Expense> expenses = expenseRepository.findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(userId,
				expenseTypesToExclude, start, end);

		BigDecimal resultBigDecimal = BigDecimal.ZERO;

		for (Expense expense : expenses) {
			resultBigDecimal = resultBigDecimal.add(expense.getAmount());
		}

		long daysBetween = ChronoUnit.DAYS.between(start, currentDate) + 1;

		return resultBigDecimal.divide(new BigDecimal(daysBetween), 2, RoundingMode.HALF_DOWN);
	}
	
	public List<Expense> deleteExpense(String userId, List<String> ids) throws Exception {
		List<Expense> expenses = new ArrayList<>();
		List<Expense> expenses2 = expenseRepository.findAll();
		for (String id : ids) {
			Expense expense = expenseRepository.findById(id).get();
			if (Objects.isNull(expense)) {
				throw new Exception("Id from provided list doesnt belong to the provided User");
			}
			else 
			{
				expenses.add(expense);
			}
		}
		expenseRepository.deleteAllById(ids);
		
		return expenses;
	}

}
