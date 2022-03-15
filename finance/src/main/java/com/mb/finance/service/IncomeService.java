package com.mb.finance.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mb.finance.entities.Income;

public interface IncomeService {

	Income addNewIncome(Income income, LocalDate backlogDataCreationDate) throws Exception;
	
	List<Income> getAllIncome();
	
	BigDecimal getTotalIncomeByUserId(String userId);
	
	List<Income> getAllIncomeByUserId(String userId);
	
	BigDecimal getAllIncomeForCurrentMonthForAUser(String userId, LocalDate currentDate);
	
	Boolean deleteIncome(Income income);
	
}
