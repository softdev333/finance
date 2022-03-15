package com.mb.finance.service;

import java.util.List;

import com.mb.finance.entities.BacklogData;

public interface BacklogDataService {

	BacklogData saveBacklogData(BacklogData backlogData);
	
	List<BacklogData> getBacklogDataByUserId(String userId);
	
	BacklogData deleteBacklogData(BacklogData backlogData);
	
}
