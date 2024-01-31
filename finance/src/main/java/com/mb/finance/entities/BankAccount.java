package com.mb.finance.entities;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    String id;

    @Column(name = "BANK_NAME")
    String bankName;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "ACCOUNT_NUMBER")
    String accountNumber;

    @Column(name = "BALANCE")
    BigDecimal balance = BigDecimal.ZERO;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getBankName() {
	return bankName;
    }

    public void setBankName(String bankName) {
	this.bankName = bankName;
    }

    public String getAccountNumber() {
	return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
	return balance;
    }

    public void setBalance(BigDecimal balance) {
	this.balance = balance;
    }

    @Override
    public String toString() {
	return "BankAccount [bankName=" + bankName + ", userId=" + userId + ", accountNumber=" + accountNumber
		+ ", balance=" + balance + "]";
    }

}
