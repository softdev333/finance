package com.mb.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.finance.entities.StagedData;

public interface StagedDataRepository extends JpaRepository<StagedData, String> {
	
	List<StagedData> findByUserId(String userId);

}
