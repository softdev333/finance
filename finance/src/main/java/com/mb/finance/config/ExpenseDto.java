package com.mb.finance.config;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDto {

	String userId;

	BigDecimal amount;

	ExpenseType expenseType;

	String comments;

	LocalDate expenseDate;

	Occurance expenseOccurance;

	String withdrawnFrom;

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

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDate getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}

	public Occurance getExpenseOccurance() {
		return expenseOccurance;
	}

	public void setExpenseOccurance(Occurance expenseOccurance) {
		this.expenseOccurance = expenseOccurance;
	}

	public String getWithdrawnFrom() {
		return withdrawnFrom;
	}

	public void setWithdrawnFrom(String withdrawnFrom) {
		this.withdrawnFrom = withdrawnFrom;
	}

}
