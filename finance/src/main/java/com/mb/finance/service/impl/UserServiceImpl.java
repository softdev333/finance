package com.mb.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.Income;
import com.mb.finance.entities.User;
import com.mb.finance.repository.UserRepository;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    IncomeService incomeService;

    @Autowired
    ExpenseService expenseService;

    @Autowired
    BankAccountService bankAccountService;

    @Override
    public User saveUser(User user) {
	return userRepository.save(user);
    }

    @Override
    public User getUser(String userId) {
	return userRepository.findById(userId).get();
    }

    @Override
    public String authenticate(String username, String password) {
	User user = userRepository.findByUserName(username);
	if (user.checkPassword(password))
	    return user.getId();
	else
	    return null;
    }

}
