package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="product_batch_stock_details")
public class ProductBatchStock {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
	@Column(name="PRODUCT_ID") private Integer productId;
	@Column(name="BATCH_NUMBER") private String batchNumber;
	@Column(name="EXPIRY_DATE") private Date expiryDate;
	private Double quantity;
	@Column(name="CREATION_DATE") private Date creationDate;
	@Version @Column(name="MODIFIED_DATE") private Date modifiedDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	@JSON(format="dd-MMM-yyyy")
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
}
