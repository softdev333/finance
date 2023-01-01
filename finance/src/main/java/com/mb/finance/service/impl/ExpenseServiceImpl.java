package com.mb.finance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.entities.Expense;
import com.mb.finance.repository.ExpenseRepository;
import com.mb.finance.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Override
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

    @Override
    public List<Expense> getExpensesByUserId(String userId) {
	List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
	return expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId, expenseTypesToExclude);
    }

    @Override
    public List<Expense> getExpensesByUserId(String userId, Pageable pageable) {
	List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
	return expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId, expenseTypesToExclude, pageable);
    }

    @Override
    public BigDecimal getAllExpensesForCurrentMonthForUser(String userId, LocalDate currentDate) {

	List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
	LocalDate start = currentDate.withDayOfMonth(1);
	LocalDate end = currentDate.withDayOfMonth(currentDate.getMonth().length(currentDate.isLeapYear()));
	List<Expense> expenses = expenseRepository.findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(userId, expenseTypesToExclude,start, end);

	BigDecimal resultBigDecimal = BigDecimal.ZERO;

	for (Expense expense : expenses) {
	    resultBigDecimal = resultBigDecimal.add(expense.getAmount());
	}

	return resultBigDecimal;
    }

    @Override
    public BigDecimal getTotalExpenseByUserId(String userId) {

	List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
	List<Expense> allExpenses = expenseRepository.findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(userId, expenseTypesToExclude);
	BigDecimal result = new BigDecimal(0);

	for (Expense expense : allExpenses) {
	    result = result.add(expense.getAmount());
	}

	return result;
    }

    @Override
    public Boolean deleteExpense(Expense expense) {
	expenseRepository.delete(expense);
	return true;
    }

    @Override
    public void saveAllExpenses(List<Expense> expenseList) {
	expenseRepository.saveAll(expenseList);
    }

    @Override
    public BigDecimal getAverageDailySpend(String userId, LocalDate currentDate) {
	List<ExpenseType> expenseTypesToExclude = Arrays.asList(ExpenseType.CONVERSION);
	LocalDate start = currentDate.withDayOfMonth(1);
	LocalDate end = currentDate.withDayOfMonth(currentDate.getMonth().length(currentDate.isLeapYear()));

	List<Expense> expenses = expenseRepository.findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(userId, expenseTypesToExclude,start, end);

	BigDecimal resultBigDecimal = BigDecimal.ZERO;
	
	for (Expense expense : expenses) {
	    resultBigDecimal = resultBigDecimal.add(expense.getAmount());
	}

	long daysBetween = ChronoUnit.DAYS.between(start, currentDate) + 1;

	return resultBigDecimal.divide(new BigDecimal(daysBetween), 2, RoundingMode.HALF_DOWN);
    }

}
