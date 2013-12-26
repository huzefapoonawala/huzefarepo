package com.simplydifferent.entity;

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
@Table(name="variant_master")
public class VariantMaster {

	private Integer id;
	private Integer productId;
	private String variantName;
	private Float purchaseDiscount;
	private Float vat;
	private Date creationDate;
	private Date modifiedDate;
	
	@Column(name="VARIANT_ID")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="PRODUCT_ID")
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	@Column(name="VARIANT_NAME")
	public String getVariantName() {
		return variantName;
	}
	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}
	
	@Column(name="PURCHASE_DISCOUNT")
	public Float getPurchaseDiscount() {
		return purchaseDiscount;
	}
	public void setPurchaseDiscount(Float purchaseDiscount) {
		this.purchaseDiscount = purchaseDiscount;
	}
	
	@Column(name="VAT")
	public Float getVat() {
		return vat;
	}
	public void setVat(Float vat) {
		this.vat = vat;
	}
	
	@JSON(deserialize=false,serialize=false)
	@Column(name="CREATION_DATE")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@JSON(deserialize=false,serialize=false)
	@Column(name="MODIFIED_DATE")
	@Version
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
