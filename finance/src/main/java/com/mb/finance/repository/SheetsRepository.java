package com.mb.finance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.finance.entities.Sheet;

@Repository
public interface SheetsRepository extends JpaRepository<Sheet, UUID> {

    Sheet findByUserId(String userId);

}
