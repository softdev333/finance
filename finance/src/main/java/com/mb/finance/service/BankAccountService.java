package com.mb.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.BankAccount;
import com.mb.finance.repository.BankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	BankAccountRepository bankAccountRepository;

	public BankAccount saveBankAccount(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	public List<BankAccount> getAllAccountsForUserId(String userId) {
		return bankAccountRepository.findByUserId(userId);
	}

	public BankAccount findByUserIdAndAccountNumber(String userId, String accountNumber) {
		return bankAccountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
	}

}
