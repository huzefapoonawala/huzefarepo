package com.dynamicsext.module.ies.vo;

import org.apache.commons.lang3.StringUtils;

public class StoreVO {

	private String storeName;
	private String storeAddress1;
	private String storeAddress2;
	private String storeCity;
	private String storeCountry;
	private String storeEmail;
	private String storeFax;
	private String storeState;
	private String storeZip;
	private String storePhone;
	private String storeWebsite;
	private String storeLogo;
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress1() {
		return storeAddress1;
	}
	public void setStoreAddress1(String storeAddress1) {
		this.storeAddress1 = storeAddress1;
	}
	public String getStoreAddress2() {
		return storeAddress2;
	}
	public void setStoreAddress2(String storeAddress2) {
		this.storeAddress2 = storeAddress2;
	}
	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	public String getStoreCountry() {
		return storeCountry;
	}
	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}
	public String getStoreEmail() {
		return storeEmail;
	}
	public void setStoreEmail(String storeEmail) {
		this.storeEmail = storeEmail;
	}
	public String getStoreFax() {
		return storeFax;
	}
	public void setStoreFax(String storeFax) {
		this.storeFax = storeFax;
	}
	public String getStoreState() {
		return storeState;
	}
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	public String getStoreZip() {
		return storeZip;
	}
	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}
	public String getStorePhone() {
		return storePhone;
	}
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}
	public String getStoreWebsite() {
		return storeWebsite;
	}
	public void setStoreWebsite(String storeWebsite) {
		this.storeWebsite = storeWebsite;
	}
	public String getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
	public String getStoreDetails(String lineBreak) {
		StringBuilder details = new StringBuilder()
			.append(storeAddress1).append(StringUtils.isNotBlank(storeAddress2)?", ":"").append(storeAddress2).append(lineBreak)
			.append(storeCity).append(", ").append(storeState).append(" ").append(storeZip).append(lineBreak)
			.append("Tel. & Fax: ").append(storePhone).append(lineBreak)
			.append("Email: ").append(storeEmail).append(lineBreak);
		if (StringUtils.isNoneBlank(storeWebsite)) {
			details.append("Website: ").append(storeWebsite).append(lineBreak);
		}
		return details.toString();		
	}
}
