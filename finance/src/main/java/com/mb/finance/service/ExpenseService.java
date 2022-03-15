package com.mb.finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mb.finance.entities.Expense;

public interface ExpenseService {

	Expense addExpense(Expense expense, LocalDate backlogDataCreationDate) throws Exception ;
	
	List<Expense> getExpensesByUserId(String userId);
	
	BigDecimal getAllExpensesForCurrentMonthForUserId(String userId, LocalDate expenseDate);
	
	BigDecimal getTotalExpenseByUserId(String userId);
	
	Boolean deleteExpense(Expense expense);
	
}
