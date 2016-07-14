package com.dynamicsext.module.ies.vo;

import java.util.Date;

import com.dynamicsext.module.ies.util.CommonUtil;

public class AccountStatementDetailsVO {

	private String invoiceNumber;
	private Date invoiceDate;
	private Date dueDate;
	private String details;
	private Double balance;
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getDebitInHtmlFormat() {
		return balance > 0 ? CommonUtil.convertAmountInHtmlFormat(balance) : "";
	}
	public String getCreditInHtmlFormat() {
		return balance < 0 ? CommonUtil.convertAmountInHtmlFormat(Math.abs(balance)) : "";
	}
	public Double getBalance() {
		return balance;
	}
	public String getBalanceInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(balance);
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
