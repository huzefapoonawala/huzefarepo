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
import org.hibernate.annotations.Formula;

@Entity
@Table(name="sales_invoice")
public class SalesInvoice {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="INVOICE_ID") private Integer invoiceId;
	@Column(name="INVOICE_NUMBER") private String invoiceNumber;
	@Column(name="INVOICE_DATE") private Date invoiceDate;
	@Column(name="TOTAL_AMOUNT") private Double totalAmount;
	@Transient private String invoiceDetails;
	@Column(name="CUSTOMER_ID") private Integer customerId;
	@Column(name="PO_NUMBER") private String poNumber;
	@Column(name="CREATION_DATE") private Date creationDate;
	@Version @Column(name="MODIFIED_DATE") private Date modifiedDate;
	@Column(name="TOTAL_AMOUNT_RECEIVED") private Double receivedAmount;
	@Formula(value="TOTAL_AMOUNT-TOTAL_AMOUNT_RECEIVED") @Column(updatable=false) private Double balanceAmount;
	
	@Transient private Boolean isInvoiceNumberValid;
	@Transient private String customInvoiceNumber;
	@Transient private String customerName;
	
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
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
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(String invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
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
	public Boolean getIsInvoiceNumberValid() {
		return isInvoiceNumberValid;
	}
	public void setIsInvoiceNumberValid(Boolean isInvoiceNumberValid) {
		this.isInvoiceNumberValid = isInvoiceNumberValid;
	}
	public String getCustomInvoiceNumber() {
		return customInvoiceNumber;
	}
	public void setCustomInvoiceNumber(String customInvoiceNumber) {
		this.customInvoiceNumber = customInvoiceNumber;
	}
	public Double getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
}
