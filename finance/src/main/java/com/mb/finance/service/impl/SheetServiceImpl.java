package com.mb.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.Sheet;
import com.mb.finance.repository.SheetsRepository;
import com.mb.finance.service.SheetService;

@Service
public class SheetServiceImpl implements SheetService {

    @Autowired
    SheetsRepository sheetsRepository;

    @Override
    public Sheet saveSheet(Sheet sheet) {
	return sheetsRepository.save(sheet);
    }

    @Override
    public Sheet findByUserId(String userId) {
	return sheetsRepository.findByUserId(userId);
    }

}
