package com.mb.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String>{

	List<BankAccount> findByUserId(String userId);
	
}
