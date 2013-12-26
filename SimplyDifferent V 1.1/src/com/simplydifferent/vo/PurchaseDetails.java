package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="purchase_details")
public class PurchaseDetails {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
	@Column(name="PRODUCT_ID") private Integer productId;
	@Column(name="BATCH_NUMBER") private String batchNumber;
	@Column(name="EXPIRY_DATE") private Date expiryDate;
	@Column(name="PURCHASE_PRICE") private Float purchasePrice;
	@Column(name="VAT") private Float vat;
	private Float quantity;
	@Column(name="INVOICE_ID") private Integer invoiceId;
	
	@Transient private Integer brandId;
	@Transient private String brandName;
	@Transient private String productName;
	@Transient private String unitType;
	
	public PurchaseDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	public Float getQuantity() {
		return quantity;
	}
	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}
