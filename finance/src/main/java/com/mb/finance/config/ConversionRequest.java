package com.mb.finance.config;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ConversionRequest {

	String userId;

	BigDecimal amount;

	String comments;

	LocalDate conversionDate;

	String withdrawnFrom;

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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDate getConversionDate() {
		return conversionDate;
	}

	public void setConversionDate(LocalDate conversionDate) {
		this.conversionDate = conversionDate;
	}

	public String getWithdrawnFrom() {
		return withdrawnFrom;
	}

	public void setWithdrawnFrom(String withdrawnFrom) {
		this.withdrawnFrom = withdrawnFrom;
	}

	public String getDepositedIn() {
		return depositedIn;
	}

	public void setDepositedIn(String depositedIn) {
		this.depositedIn = depositedIn;
	}

}
