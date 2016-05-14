package com.dynamicsext.ig.vo;

public class Item {

	private String itemLookupCode;
	private int id;
	private String description;
	private Double price;
	private Integer quantity;
	private Double totalPrice;
	public String getItemLookupCode() {
		return itemLookupCode;
	}
	public void setItemLookupCode(String itemLookupCode) {
		this.itemLookupCode = itemLookupCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
