package com.mb.finance.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Id;

@Entity
@Table(name = "STAGING_DATA")
public class StagedData {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	@Column(name = "USER_ID")
	String userId;
	
	@Column(name = "CREATION_DATE")
	LocalDate creationDate;

	@Column(name = "RECIEVED_DATE")
	LocalDate recievedDate;
	
	@Column(name = "DATA")
	String data;

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

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(LocalDate recievedDate) {
		this.recievedDate = recievedDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
		
}
