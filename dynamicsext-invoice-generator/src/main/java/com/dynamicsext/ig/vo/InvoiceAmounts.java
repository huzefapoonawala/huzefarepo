package com.dynamicsext.ig.vo;

import com.dynamicsext.ig.util.NumberFormatter;

public class InvoiceAmounts {

	private double subTotalAmount;
	private double taxAmount;
	private double grandTotalAmount;
	private Tender tender;
	public double getSubTotalAmount() {
		return subTotalAmount;
	}
	public void setSubTotalAmount(double subTotalAmount) {
		this.subTotalAmount = subTotalAmount;
	}
	public double getGrandTotalAmount() {
		return grandTotalAmount;
	}
	public void setGrandTotalAmount(double grandTotalAmount) {
		this.grandTotalAmount = grandTotalAmount;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public String getAmountWithCurency(double amount) {
		return (amount < 0 ? "-" : "")+"$"+NumberFormatter.getInstance().getNumberFormat().format(Math.abs(amount));
	}
	public Tender getTender() {
		return tender;
	}
	public void setTender(Tender tender) {
		this.tender = tender;
	}
}
