package com.mb.finance.websecurity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mb.finance.entities.FinanceUser;

public class UserInfoDetails implements UserDetails {

	private String username; // Changed from 'name' to 'username' for clarity
	private String password;
	private List<GrantedAuthority> authorities;

	public UserInfoDetails(FinanceUser financeUser) {
		this.username = financeUser.getUserId(); // Assuming 'name' is used as 'username'
		this.password = financeUser.getPassword();
		this.authorities = List.of(financeUser.getRoles().split(",")).stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Implement your logic if you need this
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Implement your logic if you need this
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Implement your logic if you need this
	}

	@Override
	public boolean isEnabled() {
		return true; // Implement your logic if you need this
	}
}
