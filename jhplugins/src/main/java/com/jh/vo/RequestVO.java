package com.jh.vo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;


public class RequestVO {

	private Integer supplierId;
	private Date fromDate1;
	private Date toDate1;
	private Date fromDate2;
	private Date toDate2;
	private String poData;
	private String itemsToModify;
	private String sku;
	private String alias;
	private Integer itemId;
	private String invoiceData;
	private String requisitioner;
	private Double multiplier;
	private Boolean updateCostPrice = true;
	private Boolean updateRetailPrice = true;
	private boolean savePO;
	private String supplierName;
	private String ftpFileName;
	private Integer ftpUserIdx;
	private boolean isUpdateBinLocation = false; 
	private Integer orderId;
	private String poNumber;
	
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public Date getFromDate1() {
		return fromDate1;
	}
	public void setFromDate1(Date fromDate1) {
		this.fromDate1 = fromDate1;
	}
	public Date getToDate1() {
		return toDate1;
	}
	public void setToDate1(Date toDate1) {
		this.toDate1 = toDate1;
	}
	public Date getFromDate2() {
		return fromDate2;
	}
	public void setFromDate2(Date fromDate2) {
		this.fromDate2 = fromDate2;
	}
	public Date getToDate2() {
		return toDate2;
	}
	public void setToDate2(Date toDate2) {
		this.toDate2 = toDate2;
	}
	@Override
	public String toString() {
		String toString = super.toString();
		try {
			toString = BeanUtils.describe(this).toString();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toString;
	}
	public String getPoData() {
		return poData;
	}
	public void setPoData(String poData) {
		this.poData = poData;
	}
	public String getItemsToModify() {
		return itemsToModify;
	}
	public void setItemsToModify(String itemsToModify) {
		this.itemsToModify = itemsToModify;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getInvoiceData() {
		return invoiceData;
	}
	public void setInvoiceData(String invoiceData) {
		this.invoiceData = invoiceData;
	}
	public String getRequisitioner() {
		return requisitioner;
	}
	public void setRequisitioner(String requisitioner) {
		this.requisitioner = requisitioner;
	}
	public Double getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}
	public Boolean getUpdateCostPrice() {
		return updateCostPrice;
	}
	public void setUpdateCostPrice(Boolean updateCostPrice) {
		this.updateCostPrice = updateCostPrice;
	}
	public Boolean getUpdateRetailPrice() {
		return updateRetailPrice;
	}
	public void setUpdateRetailPrice(Boolean updateRetailPrice) {
		this.updateRetailPrice = updateRetailPrice;
	}
	public boolean getSavePO() {
		return savePO;
	}
	public void setSavePO(boolean savePO) {
		this.savePO = savePO;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getFtpFileName() {
		return ftpFileName;
	}
	public void setFtpFileName(String ftpFileName) {
		this.ftpFileName = ftpFileName;
	}
	public Integer getFtpUserIdx() {
		return ftpUserIdx;
	}
	public void setFtpUserIdx(Integer ftpUserIdx) {
		this.ftpUserIdx = ftpUserIdx;
	}
	public boolean isUpdateBinLocation() {
		return isUpdateBinLocation;
	}
	public void setUpdateBinLocation(boolean isUpdateBinLocation) {
		this.isUpdateBinLocation = isUpdateBinLocation;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}	
}
