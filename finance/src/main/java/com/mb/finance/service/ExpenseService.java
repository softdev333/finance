package com.mb.finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mb.finance.entities.Expense;

public interface ExpenseService {

    Expense addExpense(Expense expense) throws Exception;

    List<Expense> getExpensesByUserId(String userId);

    List<Expense> getExpensesByUserId(String userId, Pageable pageable);

    BigDecimal getAllExpensesForCurrentMonthForUser(String userId, LocalDate expenseDate);

    BigDecimal getTotalExpenseByUserId(String userId);

    Boolean deleteExpense(Expense expense);

    void saveAllExpenses(List<Expense> expenseList);

    BigDecimal getAverageDailySpend(String userId, LocalDate currentDate);

}
