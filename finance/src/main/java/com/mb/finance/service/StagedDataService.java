package com.mb.finance.service;

import java.util.List;

import com.mb.finance.entities.StagedData;

public interface StagedDataService {

	List<StagedData> findByUserId(String userId);
	
	StagedData saveData(StagedData stagedData);
	
}
