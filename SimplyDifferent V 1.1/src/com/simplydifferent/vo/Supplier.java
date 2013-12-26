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
@Table(name="supplier_master")
public class Supplier {

	private Integer id;
	private String supplierName;
	private String supplierAddress;
	private String supplierContact;
	private String supplierEmail;
	private String supplierWebsite;
	private Date creationDate;
	private Date modifiedDate;
	private String vatNumber;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SUPPLIER_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="SUPPLIER_NAME")
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name="SUPPLIER_ADDRESS")
	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	@Column(name="SUPPLIER_NUMBER")
	public String getSupplierContact() {
		return supplierContact;
	}

	public void setSupplierContact(String supplierNumber) {
		this.supplierContact = supplierNumber;
	}

	@Column(name="SUPPLIER_EMAIL")
	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	@Column(name="SUPPLIER_WEBSITE")
	public String getSupplierWebsite() {
		return supplierWebsite;
	}

	public void setSupplierWebsite(String supplierWebsite) {
		this.supplierWebsite = supplierWebsite;
	}

	@Column(name="CREATION_DATE")
	@JSON(serialize=false,deserialize=false)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name="MODIFIED_DATE")
	@Version
	@JSON(serialize=false,deserialize=false)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name="VAT_NUMBER")
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
}
