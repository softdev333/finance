package com.mb.finance.entities;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "finance_user")
public class FinanceUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "FIRST_NAME")
	String firstName;

	@Column(name = "LAST_NAME")
	String lastName;

	@Column(name = "USER_ID")
	String userId;

	@Column(name = "PASSWORD")
	String password;

	@Column(name = "EMAIL")
	String email;

	@Column(name = "CREATION_DATE")
	LocalDate creationDate;

	@Column(name = "LAST_MODIFIED_DATE")
	LocalDate lastModifiedDate;
	
	@Column(name = "ROLES")
    String roles;

	@PrePersist
	public void beforePersist() {
		this.lastModifiedDate = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	/*
	 * public void setPassword(String password) throws NoSuchAlgorithmException,
	 * InvalidKeySpecException { SecureRandom random = new SecureRandom(); byte[]
	 * byteSalt = new byte[16]; random.nextBytes(byteSalt);
	 * 
	 * KeySpec spec = new PBEKeySpec(password.toCharArray(), byteSalt, 65536, 128);
	 * SecretKeyFactory factory =
	 * SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); byte[] byteHash =
	 * factory.generateSecret(spec).getEncoded(); String passwordSalt =
	 * Base64.getEncoder().encodeToString(byteSalt); String passwordHash =
	 * Base64.getEncoder().encodeToString(byteHash);
	 * 
	 * this.passwordSalt = passwordSalt; this.passwordHash = passwordHash; }
	 */

	/*
	 * public boolean checkPassword(String password) throws
	 * NoSuchAlgorithmException, InvalidKeySpecException {
	 * 
	 * byte[] byteSalt = Base64.getDecoder().decode(this.passwordSalt); KeySpec spec
	 * = new PBEKeySpec(password.toCharArray(), byteSalt, 65536, 128);
	 * SecretKeyFactory factory =
	 * SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); byte[] byteHash =
	 * factory.generateSecret(spec).getEncoded(); String enteredPasswordHashed =
	 * Base64.getEncoder().encodeToString(byteHash);
	 * 
	 * boolean validPassword = this.passwordHash.equals(enteredPasswordHashed);
	 * return validPassword; }
	 */

}
