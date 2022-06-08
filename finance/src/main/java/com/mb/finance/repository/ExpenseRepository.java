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

	List<Expense> findByUserIdOrderByExpenseDateDesc(String userId);
	
	List<Expense> findByUserIdOrderByExpenseDateDesc(String userId, Pageable pageable);
	
	List<Expense> findByUserIdAndExpenseDateBetween(String userId, LocalDate startDate, LocalDate endDate);
	
	//find by 3 parameters: User ID, Expense Date Between 2 dates, ExpenseType which are not in the given list
	List<Expense> findByUserIdAndExpenseTypeNotInAndExpenseDateBetween(String userId, List<ExpenseType> expenseTypes, LocalDate startDate, LocalDate endDate);
	
}
