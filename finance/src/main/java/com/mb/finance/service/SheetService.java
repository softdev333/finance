package com.mb.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.Sheet;
import com.mb.finance.repository.SheetsRepository;

@Service
public class SheetService {

	@Autowired
	SheetsRepository sheetsRepository;

	public Sheet saveSheet(Sheet sheet) {
		return sheetsRepository.save(sheet);
	}

	public Sheet findByUserId(String userId) {
		return sheetsRepository.findByUserId(userId);
	}

}
