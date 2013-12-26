package com.simplydifferent.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sales_receipts")
public class SalesReceipt {

	@Id @Column(name="SR_ID") private int id;
	@Column(name="INVOICE_ID") private int invoiceId;
	@Column(name="RECEIVED_AMOUNT") private double receivedAmount;
	@Column(name="RECEIVED_DATE") private Date receivedDate;
	@Column(name="PAYMENT_TYPE") private String paymentType;
	@Column(name="PAYMENT_REF_NUMBER") private String paymentRefNumber;
	@Column(name="PAYMENT_BANK_NAME") private String paymentBankName;
	@Column(name="CREATION_DATE") private Date creationDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public double getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentRefNumber() {
		return paymentRefNumber;
	}
	public void setPaymentRefNumber(String paymentRefNumber) {
		this.paymentRefNumber = paymentRefNumber;
	}
	public String getPaymentBankName() {
		return paymentBankName;
	}
	public void setPaymentBankName(String paymentBankName) {
		this.paymentBankName = paymentBankName;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
