package com.mb.finance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.entities.BacklogData;
import com.mb.finance.entities.Income;
import com.mb.finance.repository.IncomeRepository;
import com.mb.finance.service.BacklogDataService;
import com.mb.finance.service.IncomeService;

@Service
public class IncomeServiceImpl implements IncomeService {

	@Autowired
	IncomeRepository incomeRepository;
	
	@Autowired
	BacklogDataService backlogDataService;

	@Override
	@Transactional
	public Income addNewIncome(Income income, LocalDate backlogDataCreationDate) throws Exception {
		if (StringUtils.isEmpty(income.getUserId())) {
			throw new Exception("No User Found");
		}

		if (income.getAmount().compareTo(BigDecimal.ZERO) == -1) {
			throw new Exception("Amount cannot be negative");
		}

		if (income.getIncomeDate() == null) {
			income.setIncomeDate(LocalDate.now());
		}

		income.setCreationDate(LocalDate.now());
		income = incomeRepository.save(income);
		
		BacklogData backlogData = new BacklogData();
		backlogData.setUserId(income.getUserId());
		backlogData.setRecord(income.toString());
		backlogData.setCreationDate(backlogDataCreationDate!=null ? backlogDataCreationDate : LocalDate.now());
		backlogData.setProcessedDate(LocalDate.now());
		backlogData.setIsProcessed(true);
		
		backlogDataService.saveBacklogData(backlogData);

		return income;
	}

	@Override
	public List<Income> getAllIncome() {
		return incomeRepository.findAll();
	}

	@Override
	public List<Income> getAllIncomeByUserId(String userId) {
		return incomeRepository.findByUserId(userId);
	}

	@Override
	public BigDecimal getAllIncomeForCurrentMonthForAUser(String userId, LocalDate currentDate) {
		List<Income> totalIncomeForUser = incomeRepository.findByUserId(userId);

		List<Income> resultIncomes = totalIncomeForUser.stream()
				.filter(income -> income.getIncomeDate().getMonthValue() == currentDate.getMonthValue()
						&& income.getIncomeDate().getYear() == currentDate.getYear())
				.collect(Collectors.toList());
		BigDecimal resultBigDecimal = new BigDecimal(0);

		for (Income income : resultIncomes) {
			resultBigDecimal = resultBigDecimal.add(income.getAmount());
		}

		return resultBigDecimal;
	}

	@Override
	public BigDecimal getTotalIncomeByUserId(String userId) {

		List<Income> allIncome = incomeRepository.findByUserId(userId);
		BigDecimal result = new BigDecimal(0);
		for (Income income : allIncome) {
			result = result.add(income.getAmount());
		}

		return result;
	}

	@Override
	public Boolean deleteIncome(Income income) {
		incomeRepository.delete(income);
		return true;
	}

}
