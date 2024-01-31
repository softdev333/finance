package com.mb.finance.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.mb.finance.config.AssetType;
import com.mb.finance.config.QuantityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ASSET")
public class Asset {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    String id;

    @Column(name = "USER_ID")
    String userId;
    
    @Column(name = "NAME")
    String name;

    @Column(name = "COST_PRICE")
    BigDecimal costPrice = BigDecimal.ZERO;

    @Column(name = "ASSET_TYPE")
    @Enumerated(EnumType.STRING)
    AssetType assetType;

    @Column(name = "COMMENTS")
    String comments;

    @Column(name = "CREATION_DATE")
    LocalDate creationDate;

    @Column(name = "PURCHASE_DATE")
    LocalDate purchaseDate;

    @Column(name = "QUANTITY_TYPE")
    @Enumerated(EnumType.STRING)
    QuantityType quantityType;

    @Column(name = "QUANTITY")
    BigDecimal quantity = BigDecimal.ZERO;

    @Transient
    BigDecimal currentPrice = BigDecimal.ZERO;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public BigDecimal getCostPrice() {
	return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
	this.costPrice = costPrice;
    }

    public AssetType getAssetType() {
	return assetType;
    }

    public void setAssetType(AssetType assetType) {
	this.assetType = assetType;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public LocalDate getCreationDate() {
	return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
	this.creationDate = creationDate;
    }

    public LocalDate getPurchaseDate() {
	return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
	this.purchaseDate = purchaseDate;
    }

    public QuantityType getQuantityType() {
	return quantityType;
    }

    public void setQuantityType(QuantityType quantityType) {
	this.quantityType = quantityType;
    }

    public BigDecimal getQuantity() {
	return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
	this.quantity = quantity;
    }

    public BigDecimal getCurrentPrice() {
	return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
	this.currentPrice = currentPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
