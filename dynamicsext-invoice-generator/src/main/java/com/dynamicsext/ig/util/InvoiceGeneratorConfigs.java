package com.dynamicsext.ig.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class InvoiceGeneratorConfigs {

	private static Properties igConfigs;
	
	public static void setConfigs(Properties igConfigs) {
		InvoiceGeneratorConfigs.igConfigs = igConfigs;
	}
	
	public static double getSalesTax() {
		return Double.valueOf(igConfigs.getProperty("sales_tax"));
	}
	
	public static String getStoreName() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_name"), "");
	}
	
	public static String getStoreAddress() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_address_address"), "");
	}
	
	public static String getStoreCity() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_address_city"), "");
	}
	
	public static String getStoreState() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_address_state"), "");
	}
	
	public static String getStorePincode() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_address_pincode"), "");
	}
	
	public static String getStorePhone1() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_phone_1"), "");
	}
	
	public static String getStorePhone2() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("store_phone_2"), "");
	}
	
	public static String getInvoiceNotes() {
		return StringUtils.defaultIfBlank(igConfigs.getProperty("invoice_notes"), "");
	}
}
