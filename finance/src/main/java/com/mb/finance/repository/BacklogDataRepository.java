package com.mb.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.finance.entities.BacklogData;

public interface BacklogDataRepository extends JpaRepository<BacklogData, String> {
	
	List<BacklogData> findByUserId(String userId);

}
