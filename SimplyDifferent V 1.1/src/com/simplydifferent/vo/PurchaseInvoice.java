package com.simplydifferent.vo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="purchase_invoice")
public class PurchaseInvoice {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="INVOICE_ID") private Integer invoiceId;
	@Column(name="INVOICE_NUMBER") private String invoiceNumber;
	@Column(name="INVOICE_DATE") private Date invoiceDate;
	@Column(name="TOTAL_AMOUNT") private Double totalAmount;
	@Transient private String invoiceDetails;
	@Column(name="SUPPLIER_ID") private Integer supplierId;
	@Column(name="CREATION_DATE") private Date creationDate;
	@Version @Column(name="MODIFIED_DATE") private Date modifiedDate;
	
	@Transient private String supplierName;
	@Transient private Boolean isInvoiceNumberValid;
	@Transient private Double vatAmount;
	@Transient private Double netAmount;
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	@JSON(format="dd-MMM-yyyy")
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(String invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	
	@Override
	public String toString() {
		String toString = super.toString();
		try {
			toString = BeanUtils.describe(this).toString();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toString;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	@JSON(serialize=false,deserialize=false)
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@JSON(serialize=false,deserialize=false)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Boolean getIsInvoiceNumberValid() {
		return isInvoiceNumberValid;
	}
	public void setIsInvoiceNumberValid(Boolean isInvoiceNumberValid) {
		this.isInvoiceNumberValid = isInvoiceNumberValid;
	}
	public Double getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
}
