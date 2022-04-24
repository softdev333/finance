package com.mb.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.BankAccount;
import com.mb.finance.repository.BankAccountRepository;
import com.mb.finance.service.BankAccountService;

@Service
public class BankAccountServiceImpl implements BankAccountService {
	
	@Autowired
	BankAccountRepository bankAccountRepository;

	@Override
	public BankAccount saveBankAccount(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	@Override
	public List<BankAccount> getAllAccountsForUserId(String userId) {		
		return bankAccountRepository.findByUserId(userId);
	}

}
