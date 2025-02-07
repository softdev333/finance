package com.mb.finance.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.FinanceUser;
import com.mb.finance.repository.FinanceUserRepository;
import com.mb.finance.websecurity.UserInfoDetails;

@Service
public class FinanceUserService implements UserDetailsService {

	@Autowired
	FinanceUserRepository financeUserRepository;

	@Autowired
	private PasswordEncoder encoder;

	public FinanceUser saveFinanceUser(FinanceUser user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return financeUserRepository.save(user);
	}

	public Optional<FinanceUser> getUser(String userId) {
		return financeUserRepository.findById(userId);
	}

	public Optional<FinanceUser> getUserByUserId(String userId) {
		return financeUserRepository.findByUserId(userId);
	}

	/*
	 * public Long authenticate(String userId, String password) throws
	 * NoSuchAlgorithmException, InvalidKeySpecException { FinanceUser user =
	 * userRepository.findByUserId(userId).get(); if (user.checkPassword(password))
	 * return user.getId(); else return null; }
	 */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<FinanceUser> userDetail = financeUserRepository.findByUserId(username);
		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

}
