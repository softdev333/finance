package com.mb.finance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.FinanceUser;

@Repository
public interface UserRepository extends JpaRepository<FinanceUser, String> {

	Optional<FinanceUser> findById(String id);

	Optional<FinanceUser> findByUserId(String userId);

}
