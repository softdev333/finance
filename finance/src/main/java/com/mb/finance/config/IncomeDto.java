package com.mb.finance.config;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeDto {

	String userId;

	BigDecimal amount;

	IncomeType incomeType;

	String comments;

	LocalDate incomeDate;

	Occurance incomeOccurance;

	String depositedIn;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public IncomeType getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(IncomeType incomeType) {
		this.incomeType = incomeType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDate getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(LocalDate incomeDate) {
		this.incomeDate = incomeDate;
	}

	public Occurance getIncomeOccurance() {
		return incomeOccurance;
	}

	public void setIncomeOccurance(Occurance incomeOccurance) {
		this.incomeOccurance = incomeOccurance;
	}

	public String getDepositedIn() {
		return depositedIn;
	}

	public void setDepositedIn(String depositedIn) {
		this.depositedIn = depositedIn;
	}

}
