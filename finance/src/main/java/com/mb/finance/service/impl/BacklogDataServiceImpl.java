package com.mb.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.BacklogData;
import com.mb.finance.repository.BacklogDataRepository;
import com.mb.finance.service.BacklogDataService;

@Service
public class BacklogDataServiceImpl implements BacklogDataService {

	@Autowired
	BacklogDataRepository backlogDataRepository;

	@Override
	public BacklogData saveBacklogData(BacklogData backlogData) {
		return backlogDataRepository.save(backlogData);
	}

	@Override
	public List<BacklogData> getBacklogDataByUserId(String userId) {
		return backlogDataRepository.findByUserId(userId);
	}

	@Override
	public BacklogData deleteBacklogData(BacklogData backlogData) {

		backlogDataRepository.delete(backlogData);
		return null;
	}

}
