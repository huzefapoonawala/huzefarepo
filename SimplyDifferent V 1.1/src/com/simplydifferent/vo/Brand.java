package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="brand_master")
public class Brand {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="BRAND_ID") private int brandId;
	@Column(name="BRAND_NAME") private String brandName;
	@Column(name="DESCRIPTION") private String description;
	@Column(name="CREATION_DATE") private Date creationDate;
	@Version @Column(name="MODIFIED_DATE") private Date modifiedDate;
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
