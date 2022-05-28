package com.mb.finance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		if (StringUtils.isEmpty(expense.getUserId())) {
			throw new Exception("No User Found");
		}

		if (expense.getAmount().compareTo(BigDecimal.ZERO) == -1) {
			throw new Exception("Amount cannot be negative");
		}

		if (expense.getExpenseDate() == null) {
			expense.setExpenseDate(LocalDate.now());
		}

		expense.setCreationDate(LocalDate.now());
		expense = expenseRepository.save(expense);

		return expense;
	}

	@Override
	public List<Expense> getExpensesByUserId(String userId) {

		return expenseRepository.findByUserIdOrderByExpenseDateDesc(userId);
	}
	
	@Override
	public List<Expense> getExpensesByUserId(String userId,Pageable pageable) {

		return expenseRepository.findByUserIdOrderByExpenseDateDesc(userId,pageable);
	}

	@Override
	public BigDecimal getAllExpensesForCurrentMonthForUserId(String userId, LocalDate expenseDate) {
		List<Expense> totalExpenseForUser = expenseRepository.findByUserIdOrderByExpenseDateDesc(userId);

		List<Expense> resultExpense = totalExpenseForUser.stream()
				.filter(expense -> expense.getExpenseDate().getMonthValue() == expenseDate.getMonthValue()
						&& expense.getExpenseDate().getYear() == expenseDate.getYear())
				.collect(Collectors.toList());
		BigDecimal resultBigDecimal = new BigDecimal(0);

		for (Expense expense : resultExpense) {
			resultBigDecimal = resultBigDecimal.add(expense.getAmount());
		}

		return resultBigDecimal;
	}

	@Override
	public BigDecimal getTotalExpenseByUserId(String userId) {

		List<Expense> allExpenses = expenseRepository.findByUserIdOrderByExpenseDateDesc(userId);
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

}
