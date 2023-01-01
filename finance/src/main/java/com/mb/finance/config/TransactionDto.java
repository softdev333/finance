package com.mb.finance.config;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

    String userId;

    BigDecimal amount = BigDecimal.ZERO;

    TransactionType transactionType;

    String comments;

    LocalDate creationDate;

    LocalDate transactionDate;

    String transactionOn;

    Occurance transactionOccurance;

    String transactionEndPoint;

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

    public TransactionType getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
	this.transactionType = transactionType;
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

    public LocalDate getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
	this.transactionDate = transactionDate;
    }

    public Occurance getTransactionOccurance() {
	return transactionOccurance;
    }

    public void setTransactionOccurance(Occurance transactionOccurance) {
	this.transactionOccurance = transactionOccurance;
    }

    public String getTransactionEndPoint() {
	return transactionEndPoint;
    }

    public void setTransactionEndPoint(String transactionEndPoint) {
	this.transactionEndPoint = transactionEndPoint;
    }

    public String getTransactionOn() {
	return transactionOn;
    }

    public void setTransactionOn(String transactionOn) {
	this.transactionOn = transactionOn;
    }

}
