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
@Table(name="sales_details")
public class SalesDetails {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
	@Column(name="PRODUCT_ID") private Integer productId;
	@Column(name="BATCH_NUMBER") private String batchNumber;
	@Column(name="SALES_PRICE") private Float salesPrice;
	@Column(name="VAT") private Float vat;
	private Float quantity;
	@Column(name="INVOICE_ID") private Integer invoiceId;
	@Column(name="ADDITIONAL_DETAILS") private String additionalDetails;
	@Column(name="EXPIRY_DATE") private Date expiryDate;
	@Column(name="CREATION_DATE",insertable=false,updatable=false) private Date creationDate;
	
	@Transient private String brandName;
	@Transient private String productName;
	@Transient private String unitType;
	
	public SalesDetails() {
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public Float getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Float salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getAdditionalDetails() {
		return additionalDetails;
	}
	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
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
	
	@Override
	public boolean equals(Object obj) {
		boolean isValid = super.equals(obj);
		if (!isValid && obj != null && obj.getClass() == this.getClass()) {
			SalesDetails sd = (SalesDetails) obj;
			if (sd.getProductId() != null && this.getProductId() != null && sd.getProductId().intValue() == this.getProductId().intValue()) {
				isValid = true;
			}
			if (isValid && sd.getSalesPrice() != null && this.getSalesPrice() != null && sd.getSalesPrice().floatValue() == this.getSalesPrice().floatValue()) {
				isValid = true;
			}
			if (isValid && sd.getVat() != null && this.getVat() != null && sd.getVat().floatValue() == this.getVat().floatValue()) {
				isValid = true;
			}
		}
		return isValid;
	}
}
