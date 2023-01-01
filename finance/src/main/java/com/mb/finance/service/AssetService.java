package com.mb.finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.Asset;
import com.mb.finance.repository.AssetRepository;

@Service
public class AssetService {

    @Autowired
    AssetRepository assetRepository;

    public List<Asset> findByUserId(String userId) {
	return assetRepository.findByUserId(userId);
    }

    public Asset saveAsset(Asset asset) throws Exception {
	if (StringUtils.isBlank(asset.getUserId())) {
	    throw new Exception("No User Found");
	}

	if (asset.getCostPrice().compareTo(BigDecimal.ZERO) == -1) {
	    throw new Exception("Amount cannot be negative");
	}

	if (asset.getPurchaseDate() == null) {
	    asset.setPurchaseDate(LocalDate.now());
	}

	return assetRepository.save(asset);
    }

}
