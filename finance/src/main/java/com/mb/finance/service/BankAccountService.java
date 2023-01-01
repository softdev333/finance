package com.mb.finance.service;

import java.util.List;

import com.mb.finance.entities.BankAccount;

public interface BankAccountService {

    BankAccount saveBankAccount(BankAccount bankAccount);

    List<BankAccount> getAllAccountsForUserId(String userId);
    
    BankAccount findByUserIdAndAccountNumber(String userId, String bankAccountNumber);

}
