package com.mb.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {

    List<Asset> findByUserId(String userId);

}
