package com.jh.vo;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

public class WebsiteOrder {

	private int orderId;
	private String storeName;
	private String firstName;
	private String lastName;
	private String email;
	private String telephone;
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingCity;
	private String shippingPostcode;
	private String shippingCountry;
	private String shippingZone;
	private String shippingZoneCode;
	private Double totalBilledAmount;
	private Date dateAdded;
	private Date dateModified;
	private boolean isFulfillOrderAtStore;
	private List<Item> itemsOnOrder;
	private String orderStatus;
	private String shipOrderFrom;
	private String invoiceNumber;
	private String poNumber;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getShippingAddress1() {
		return shippingAddress1;
	}
	public void setShippingAddress1(String shippingAddress1) {
		this.shippingAddress1 = shippingAddress1;
	}
	public String getShippingAddress2() {
		return shippingAddress2;
	}
	public void setShippingAddress2(String shippingAddress2) {
		this.shippingAddress2 = shippingAddress2;
	}
	public String getShippingCity() {
		return shippingCity;
	}
	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}
	public String getShippingPostcode() {
		return shippingPostcode;
	}
	public void setShippingPostcode(String shippingPostcode) {
		this.shippingPostcode = shippingPostcode;
	}
	public String getShippingCountry() {
		return shippingCountry;
	}
	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}
	public String getShippingZone() {
		return shippingZone;
	}
	public void setShippingZone(String shippingZone) {
		this.shippingZone = shippingZone;
	}
	public Double getTotalBilledAmount() {
		return totalBilledAmount;
	}
	public void setTotalBilledAmount(Double totalBilledAmount) {
		this.totalBilledAmount = totalBilledAmount;
	}
	@JSON(format="dd-MMM-yyyy")
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	@JSON(format="dd-MMM-yyyy")
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public boolean isFulfillOrderAtStore() {
		return isFulfillOrderAtStore;
	}
	public void setFulfillOrderAtStore(boolean isFulfillOrderAtStore) {
		this.isFulfillOrderAtStore = isFulfillOrderAtStore;
	}
	public boolean getFulfillOrderAtStore() {
		return isFulfillOrderAtStore;
	}
	public List<Item> getItemsOnOrder() {
		return itemsOnOrder;
	}
	public void setItemsOnOrder(List<Item> itemsOnOrder) {
		this.itemsOnOrder = itemsOnOrder;
	}
	
	public String getShippingAddress() {
		StringBuilder sb = new StringBuilder();
		if (shippingAddress1 != null && !shippingAddress1.isEmpty()) {
			sb.append(shippingAddress1).append(", ");
		}
		if (shippingAddress2 != null && !shippingAddress2.isEmpty()) {
			sb.append(shippingAddress2).append(", ");
		}
		if (shippingCity != null && !shippingCity.isEmpty()) {
			sb.append(shippingCity).append(", ");
		}
		if (shippingZone != null && !shippingZone.isEmpty()) {
			sb.append(shippingZone);
			if (shippingPostcode != null && !shippingPostcode.isEmpty()) {
				sb.append(" - ").append(shippingPostcode);
			}
			sb.append(", ");
		}
		if (shippingCountry != null && !shippingCountry.isEmpty()) {
			sb.append(shippingCountry).append(".");
		}
		if (sb.charAt(sb.length()-1) == ',') {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getShippingZoneCode() {
		return shippingZoneCode;
	}
	public void setShippingZoneCode(String shippingZoneCode) {
		this.shippingZoneCode = shippingZoneCode;
	}
	public String getShipOrderFrom() {
		return shipOrderFrom;
	}
	public void setShipOrderFrom(String shipOrderFrom) {
		this.shipOrderFrom = shipOrderFrom;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
	/*public String getShippingAddressOnly() {
		StringBuilder sb = new StringBuilder();
		if (shippingAddress1 != null && !shippingAddress1.isEmpty()) {
			sb.append(shippingAddress1).append(", ");
		}
		if (shippingAddress2 != null && !shippingAddress2.isEmpty()) {
			sb.append(shippingAddress2).append(", ");
		}
		if (sb.charAt(sb.length()-1) == ',') {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}*/
}
