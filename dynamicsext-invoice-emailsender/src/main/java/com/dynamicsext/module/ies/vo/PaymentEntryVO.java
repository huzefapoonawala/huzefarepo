package com.dynamicsext.module.ies.vo;

import java.util.Date;

import com.dynamicsext.module.ies.util.CommonUtil;

public class PaymentEntryVO {

	private Long transactionNumber;
	private Date invoiceDate;
	private Date dueDate;
	private Double invoiceAmount;
	private Double balanceDue;
	private Double payment;
	public Long getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(Long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public String getInvoiceDateInHtmlFormat() {
		return CommonUtil.convertDateInHtmlFormat(invoiceDate);
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public String getDueDateInHtmlFormat() {
		return CommonUtil.convertDateInHtmlFormat(dueDate);
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	public String getInvoiceAmountInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(invoiceAmount);
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Double getBalanceDue() {
		return balanceDue;
	}
	public String getBalanceDueInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(balanceDue);
	}
	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}
	public Double getPayment() {
		return payment;
	}
	public String getPaymentInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(payment);
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
}
