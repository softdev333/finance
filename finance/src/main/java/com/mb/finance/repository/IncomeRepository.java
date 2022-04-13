package com.mb.finance.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {

	List<Income> findByUserIdOrderByIncomeDateDesc(String userId);
	
	List<Income> findByUserIdOrderByIncomeDateDesc(String userId, Pageable pageable);
	
}
