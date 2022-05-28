package com.mb.finance.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Id;

@Entity
@Table(name  = "USER")
public class User {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
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

	public void setPassword(String password)
	{
		this.passwordSalt = RandomStringUtils.random(32);
		this.passwordHash = DigestUtils.sha256Hex(passwordSalt+password);
	}

	public boolean checkPassword(String password)
	{
		return this.passwordHash.equals(DigestUtils.sha256Hex(this.passwordSalt+password));
	}
	
	public String getUserInformationForFile()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("FIRST NAME|LAST NAME|USER NAME|EMAIL|PASSWORD SALT\n");
		sb.append(firstName+"|"+lastName+"|"+userName+"|"+email+"|"+passwordSalt+"\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName
				+ ", passwordHash=" + passwordHash + ", email=" + email + ", passwordSalt=" + passwordSalt + "]";
	}
	
	
}
