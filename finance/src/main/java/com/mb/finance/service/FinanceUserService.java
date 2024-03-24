package com.mb.finance.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.FinanceUser;
import com.mb.finance.repository.UserRepository;

@Service
public class FinanceUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	IncomeService incomeService;

	@Autowired
	ExpenseService expenseService;

	@Autowired
	BankAccountService bankAccountService;

	public FinanceUser saveFinanceUser(FinanceUser user) {
		return userRepository.save(user);
	}

	public Optional<FinanceUser> getUser(String userId) {
		return userRepository.findById(userId);
	}

	public Optional<FinanceUser> getUserByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}

	public Long authenticate(String userId, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		FinanceUser user = userRepository.findByUserId(userId).get();
		if (user.checkPassword(password))
			return user.getId();
		else
			return null;
	}

}
