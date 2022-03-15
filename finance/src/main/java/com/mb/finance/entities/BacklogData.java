package com.mb.finance.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BACKLOG_DATA")
public class BacklogData {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	@Column(name = "USER_ID")
	String userId;

	@Column(name = "RECORD")
	String record;
	
	@Column(name = "CREATION_DATE")
	LocalDate creationDate;
	
	@Column(name = "PROCESSED")
	Boolean isProcessed;
	
	@Column(name = "PROCESSED_DATE")
	LocalDate processedDate;

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

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public LocalDate getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(LocalDate processedDate) {
		this.processedDate = processedDate;
	}
	
}
