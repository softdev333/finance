package com.mb.finance.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mb.finance.entities.Income;

public interface IncomeService {

	Income addNewIncome(Income income) throws Exception;
	
	List<Income> getAllIncome();
	
	BigDecimal getTotalIncomeByUserId(String userId);
	
	List<Income> getAllIncomeByUserId(String userId);
	
	List<Income> getAllIncomeByUserId(String userId, Pageable pageable);
	
	BigDecimal getAllIncomeForCurrentMonthForUser(String userId, LocalDate currentDate);
	
	Boolean deleteIncome(Income income);
	
	void saveAllIncome(List<Income> incomeList);
}
