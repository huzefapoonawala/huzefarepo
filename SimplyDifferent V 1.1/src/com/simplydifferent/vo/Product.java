package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="product_master")
public class Product {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="PRODUCT_ID") private Integer id;
	@Column(name="BRAND_ID") private Integer brandId;
	@Column(name="PRODUCT_NAME") private String productName;
	@Column(name="PURCHASE_PRICE") private Float purchasePrice;
	@Column(name="VAT") private Float vat;
	@Column(name="UNIT_TYPE") private String unitType;
	@Column(name="REORDER_LIMIT") private Float reorderLimit;
	@Column(name="CREATION_DATE") private Date creationDate;
	@Version @Column(name="MODIFIED_DATE") private Date modifiedDate;
	@Column(name="IN_STOCK", insertable=false, updatable=false) private Double stock;
	
	@Transient private Brand brand;
	@Transient private String brandName;
	@Transient private Double stockValue;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Float getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Float getVat() {
		return vat;
	}
	public void setVat(Float vat) {
		this.vat = vat;
	}
	@JSON(serialize=false,deserialize=false)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@JSON(serialize=false,deserialize=false)
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public Float getReorderLimit() {
		return reorderLimit;
	}
	public void setReorderLimit(Float reorderLimit) {
		this.reorderLimit = reorderLimit;
	}
	public Double getStock() {
		return stock;
	}
	public void setStock(Double stock) {
		this.stock = stock;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Double getStockValue() {
		return stockValue;
	}
	public void setStockValue(Double stockValue) {
		this.stockValue = stockValue;
	}
}
