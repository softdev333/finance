package com.mb.finance.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.mb.finance.config.IncomeType;
import com.mb.finance.config.Occurance;

import javax.persistence.Id;

@Entity
@Table(name  = "INCOME")
public class Income {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	@Column(name = "USER_ID")
	String userId;
	
	@Column(name = "AMOUNT")
	BigDecimal amount = BigDecimal.ZERO;
	
	@Column(name = "INCOME_TYPE")
	@Enumerated(EnumType.STRING)
	IncomeType incomeType;
	
	@Column(name = "INCOME_COMMENTS")
	String comments;
	
	@Column(name = "CREATION_DATE")
	LocalDate creationDate;
	
	@Column(name = "INCOME_DATE")
	LocalDate incomeDate;
	
	@Column(name = "OCCURANCE")
	@Enumerated(EnumType.STRING)
	Occurance incomeOccurance;
	
	@Column(name = "DEPOSITED_IN")
	String depositedIn;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
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

	@Override
	public String toString() {
		return "Income [amount=" + amount + ", incomeType=" + incomeType + ", comments=" + comments + ", creationDate="
				+ creationDate + ", incomeDate=" + incomeDate + ", incomeOccurance=" + incomeOccurance
				+ ", depositedIn=" + depositedIn + "]";
	}
	
	
}
