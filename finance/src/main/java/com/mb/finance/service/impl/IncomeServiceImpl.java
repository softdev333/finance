package com.mb.finance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mb.finance.config.IncomeType;
import com.mb.finance.entities.Income;
import com.mb.finance.repository.IncomeRepository;
import com.mb.finance.service.IncomeService;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    IncomeRepository incomeRepository;

    @Override
    @Transactional
    public Income addNewIncome(Income income) throws Exception {
	if (StringUtils.isEmpty(income.getUserId())) {
	    throw new Exception("No User Found");
	}

	if (income.getAmount().compareTo(BigDecimal.ZERO) == -1) {
	    throw new Exception("Amount cannot be negative");
	}

	income.setIncomeDate(income.getIncomeDate() != null ? income.getIncomeDate() : LocalDate.now());
	income.setCreationDate(LocalDate.now());
	return incomeRepository.save(income);
    }

    @Override
    public List<Income> getAllIncomeByUserId(String userId) {
	List<IncomeType> incomeTypesToExclude = Arrays.asList(IncomeType.CONVERSION);
	return incomeRepository.findByUserIdAndIncomeTypeNotInOrderByIncomeDateDesc(userId, incomeTypesToExclude);
    }

    @Override
    public List<Income> getAllIncomeByUserId(String userId, Pageable pageable) {
	List<IncomeType> incomeTypesToExclude = Arrays.asList(IncomeType.CONVERSION);
	return incomeRepository.findByUserIdAndIncomeTypeNotInOrderByIncomeDateDesc(userId, incomeTypesToExclude, pageable);
    }

    @Override
    public BigDecimal getAllIncomeForCurrentMonthForUser(String userId, LocalDate currentDate) {
	List<IncomeType> incomeTypesToExclude = Arrays.asList(IncomeType.CONVERSION);
	List<Income> totalIncomeForUser = incomeRepository.findByUserIdAndIncomeTypeNotInOrderByIncomeDateDesc(userId, incomeTypesToExclude);

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
	List<IncomeType> incomeTypesToExclude = Arrays.asList(IncomeType.CONVERSION);
	List<Income> allIncome = incomeRepository.findByUserIdAndIncomeTypeNotInOrderByIncomeDateDesc(userId, incomeTypesToExclude);
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

    @Override
    public void saveAllIncome(List<Income> incomeList) {
	incomeRepository.saveAll(incomeList);
    }

}
