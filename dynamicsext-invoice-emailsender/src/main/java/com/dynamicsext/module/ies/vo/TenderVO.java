package com.dynamicsext.module.ies.vo;

import com.dynamicsext.module.ies.util.CommonUtil;

public class TenderVO {

	private int transactionNumber;
	private String description;
	private double amount;
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public String getAmountInDisplayFormat() {
		return CommonUtil.convertAmountInHtmlFormat(amount);
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
