package com.mb.finance.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	@Column(name = "FIRST_NAME")
	String firstName;

	@Column(name = "LAST_NAME")
	String lastName;

	@Column(name = "USERNAME")
	String userName;

	@Column(name = "PASSWORD_HASH")
	String passwordHash;

	@Column(name = "EMAIL")
	String email;

	@Column(name = "PASSWORD_SALT")
	String passwordSalt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//TO DO - change this
	public void setPassword(String password) {
		this.passwordSalt = RandomStringUtils.random(32);
		this.passwordHash = passwordSalt + password;
	}

	public boolean checkPassword(String password) {
		return this.passwordHash.equals(this.passwordSalt + password);
	}

	public String getUserInformationForFile() {
		StringBuilder sb = new StringBuilder();
		sb.append("FIRST NAME|LAST NAME|USER NAME|EMAIL|PASSWORD SALT\n");
		sb.append(firstName + "|" + lastName + "|" + userName + "|" + email + "|" + passwordSalt + "\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName
				+ ", passwordHash=" + passwordHash + ", email=" + email + ", passwordSalt=" + passwordSalt + "]";
	}

}
