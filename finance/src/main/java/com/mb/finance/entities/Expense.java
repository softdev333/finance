package com.mb.finance.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.Occurance;

import javax.persistence.Id;

@Entity
@Table(name = "EXPENSE")
public class Expense {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	@Column(name = "USER_ID")
	String userId;
	
	@Column(name = "AMOUNT")
	BigDecimal amount = BigDecimal.ZERO;
	
	@Column(name = "EXPENSE_TYPE")
	ExpenseType expenseType;
	
	@Column(name = "EXPENSE_COMMENTS")
	String comments;
	
	@Column(name = "CREATION_DATE")
	LocalDate creationDate;
	
	@Column(name = "EXPENSE_DATE")
	LocalDate expenseDate;
	
	@Column(name = "OCCURANCE")
	Occurance expenseOccurance;
	
	@Column(name = "WITHDRAWM_FROM")
	String withdrawnFrom;

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

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
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

	@Override
	public String toString() {
		return "Expense [amount=" + amount + ", expenseType=" + expenseType + ", comments=" + comments
				+ ", creationDate=" + creationDate + ", expenseDate=" + expenseDate + ", expenseOccurance="
				+ expenseOccurance + ", withdrawnFrom=" + withdrawnFrom + "]";
	}
	
	
	
}
