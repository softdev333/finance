package com.mb.finance.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name  = "Sheet")
public class Sheet {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	@Column(name = "USER_ID")
	String userId;
	
	@Column(name = "ROW_NO")
	Integer rowNumber;

	@Lob
	@Column(columnDefinition = "text", name = "GOOGLE_SHEETS_CLIENT_SECRET_JSON")
	String sheetsClientSecretJsonString;
	
	@Column(name = "SHEET_ID")
	String sheetId;
	

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

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getSheetsClientSecretJsonString() {
		return sheetsClientSecretJsonString;
	}

	public void setSheetsClientSecretJsonString(String sheetsClientSecretJsonString) {
		this.sheetsClientSecretJsonString = sheetsClientSecretJsonString;
	}

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	
	
}
