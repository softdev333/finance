package com.mb.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.StagedData;
import com.mb.finance.repository.StagedDataRepository;
import com.mb.finance.service.StagedDataService;

@Service
public class StagedDataServiceImpl implements StagedDataService {

	@Autowired
	StagedDataRepository stagedDataRepository;
	
	@Override
	public List<StagedData> findByUserId(String userId) {
		return stagedDataRepository.findByUserId(userId);
	}

	@Override
	public StagedData saveData(StagedData stagedData) {
		return stagedDataRepository.save(stagedData);
	}

}
