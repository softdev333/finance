package com.mb.finance.service;

import com.mb.finance.entities.Sheet;

public interface SheetService {

	Sheet saveSheet(Sheet sheet);

	Sheet findByUserId(String userId);
	
}
