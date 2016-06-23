package com.dynamicsext.module.ies.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.dynamicsext.module.ies.util.CommonUtil;

public class TransactionVO {

	private int transactionNumber;
	private String accountNumber;
	private Date transactionDate; 
	private String cashier;
	private Double grandTotal;
	private Double salesTax;
	private String billToName;
	private String billToCompany;
	private String billToAddress;
	private String billToCity;
	private String billToState;
	private String billToZip;
	private String billToPhone;
	private String reference;
	private String comment;
	private String shipToName;
	private String shipToCompany;
	private String shipToAddress;
	private String shipToCity;
	private String shipToState;
	private String shipToZip;
	private String shipToPhone;
	private String emailAddress;
	private Boolean isOptForEmail;
	private Integer orderType;
	
	public int getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}
	
	public String getGrandTotalInDisplayFormat() {
		return CommonUtil.convertAmountInHtmlFormat(grandTotal);
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getSalesTax() {
		return salesTax;
	}
	
	public String getSalesTaxInDisplayFormat() {
		return CommonUtil.convertAmountInHtmlFormat(salesTax);
	}

	public void setSalesTax(Double salesTax) {
		this.salesTax = salesTax;
	}
	
	public String getBillToDetails(String lineSeperator) {
		StringBuilder sb = new StringBuilder()
			.append(this.getBillToName()).append(lineSeperator)
			.append(this.getBillToCompany()).append(lineSeperator)
			.append(this.getBillToAddress()).append(lineSeperator)
			.append(this.getBillToCity()).append(", ").append(this.getBillToState()).append(" ").append(this.getBillToZip()).append(lineSeperator);
		if (StringUtils.isNotBlank(this.getBillToPhone())) {
			sb.append("Tel: ").append(this.getBillToPhone());
		}
		return sb.toString();
	}
	
	public String getBillToDetailsInHtmlFormat() {
		return getBillToDetails("<br>");
	}
	
	public String getShipToDetails(String lineSeperator) {
		StringBuilder sb = new StringBuilder()
			.append(StringUtils.defaultIfBlank(this.getShipToName(), "")).append(lineSeperator)
			.append(StringUtils.defaultIfBlank(this.getShipToCompany(), "")).append(lineSeperator)
			.append(StringUtils.defaultIfBlank(this.getShipToAddress(), "")).append(lineSeperator)
			.append(StringUtils.defaultIfBlank(this.getShipToCity(), "")).append(StringUtils.isBlank(this.getShipToCity()) ? "" : ", ").append(StringUtils.defaultIfBlank(this.getShipToState(), "")).append(" ").append(StringUtils.defaultIfBlank(this.getShipToZip(), "")).append(lineSeperator);
		if (StringUtils.isNotBlank(this.getShipToPhone())) {
			sb.append("Tel: ").append(this.getShipToPhone());
		}
		return sb.toString();
	}
	
	public String getShipToDetailsInHtmlFormat() {
		return getShipToDetails("<br>");
	}
	
	@Override
	public String toString() {
		return String.format("transactionNumber=%s, transactionDate=%s, accountNumber=%s, company=%s, grandTotal=%s, salesTax=%s",
				this.getTransactionNumber(),
				this.getTransactionDate(),
				this.getAccountNumber(),
				this.getBillToCompany(),
				this.getGrandTotal(),
				this.getSalesTax()
				);
	}

	public String getTransactionDatePart() {
		if (transactionDate != null) {
			return new SimpleDateFormat("MM/dd/yyyy").format(transactionDate);
		}
		else{
			return null;
		}
	}

	public String getTransactionTimePart() {
		if (transactionDate != null) {
			return new SimpleDateFormat("hh:mm:ss a").format(transactionDate);
		}
		else{
			return null;
		}
	}

	public String getReference() {
		return StringUtils.defaultIfBlank(reference, "");
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getComment() {
		return StringUtils.defaultIfBlank(comment, "");
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCashier() {
		return StringUtils.defaultIfBlank(cashier, "");
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getBillToName() {
		return billToName;
	}

	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}

	public String getBillToCompany() {
		return billToCompany;
	}

	public void setBillToCompany(String billToCompany) {
		this.billToCompany = billToCompany;
	}

	public String getBillToAddress() {
		return billToAddress;
	}

	public void setBillToAddress(String billToAddress) {
		this.billToAddress = billToAddress;
	}

	public String getBillToCity() {
		return billToCity;
	}

	public void setBillToCity(String billToCity) {
		this.billToCity = billToCity;
	}

	public String getBillToState() {
		return billToState;
	}

	public void setBillToState(String billToState) {
		this.billToState = billToState;
	}

	public String getBillToZip() {
		return billToZip;
	}

	public void setBillToZip(String billToZip) {
		this.billToZip = billToZip;
	}

	public String getBillToPhone() {
		return billToPhone;
	}

	public void setBillToPhone(String billToPhone) {
		this.billToPhone = billToPhone;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public String getShipToCompany() {
		return shipToCompany;
	}

	public void setShipToCompany(String shipToCompany) {
		this.shipToCompany = shipToCompany;
	}

	public String getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public String getShipToCity() {
		return shipToCity;
	}

	public void setShipToCity(String shipToCity) {
		this.shipToCity = shipToCity;
	}

	public String getShipToState() {
		return shipToState;
	}

	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
	}

	public String getShipToZip() {
		return shipToZip;
	}

	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}

	public String getShipToPhone() {
		return shipToPhone;
	}

	public void setShipToPhone(String shipToPhone) {
		this.shipToPhone = shipToPhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Boolean getIsOptForEmail() {
		return isOptForEmail;
	}

	public void setIsOptForEmail(Boolean isOptForEmail) {
		this.isOptForEmail = isOptForEmail;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
}
