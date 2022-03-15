package com.mb.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

	List<Expense> findByUserId(String userId);
	
}
