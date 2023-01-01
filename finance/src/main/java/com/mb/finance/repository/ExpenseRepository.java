package com.mb.finance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.entities.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    List<Expense> findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(String userId, List<ExpenseType> expenseTypes);

    List<Expense> findByUserIdAndExpenseTypeNotInOrderByExpenseDateDesc(String userId, List<ExpenseType> expenseTypes, Pageable pageable);

    List<Expense> findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(String userId, List<ExpenseType> expenseTypes, LocalDate startDate, LocalDate endDate);

}
