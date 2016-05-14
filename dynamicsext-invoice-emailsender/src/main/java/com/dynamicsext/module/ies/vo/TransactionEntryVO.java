package com.dynamicsext.module.ies.vo;

import com.dynamicsext.module.ies.util.CommonUtil;

public class TransactionEntryVO {

	private String itemLookupCode;
	private String description;
	private Integer quantity;
	private Double price;
	private Double extPrice;
	public String getItemLookupCode() {
		return itemLookupCode;
	}
	public void setItemLookupCode(String itemLookupCode) {
		this.itemLookupCode = itemLookupCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public String getPriceInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(price);
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getExtPrice() {
		return extPrice;
	}
	public String getExtPriceInHtmlFormat() {
		return CommonUtil.convertAmountInHtmlFormat(extPrice);
	}
	public void setExtPrice(Double extPrice) {
		this.extPrice = extPrice;
	}
}
