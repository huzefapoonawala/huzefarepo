package com.jh.vo;


import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.json.annotations.JSON;

public class Item {
	
	private int id;
	private String sku;
	private Float retailPrice;
	private Float costPrice;
	private Integer stockQuantity;
	private Integer committedQuantity;
	private Integer restockLevel;
	private Integer reorderLevel;
	private String description;
	private String image;
	private Date lastReceived;
	private Date lastSold;
	private String aliases;
	private Integer onOrder;
	private Integer transferOut;
	private Float width;
	private Float height;
	private Float length;
	private Float weight;
	private String uom;
	private String deptName;
	private String categoryName;
	private Integer deptId;
	private Integer categoryId;
	private String deptCode;
	private String categoryCode;
	private String toOrderQuantity;
	
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Item(String sku) {
		super();
		this.sku = sku;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Float getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Float retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Float getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}
	public Integer getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public Integer getRestockLevel() {
		return restockLevel;
	}
	public void setRestockLevel(Integer restockLevel) {
		this.restockLevel = restockLevel;
	}
	public Integer getReorderLevel() {
		return reorderLevel;
	}
	public void setReorderLevel(Integer reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	@JSON(format="dd-MMM-yyyy")
	public Date getLastReceived() {
		return lastReceived;
	}
	public void setLastReceived(Date lastReceived) {
		this.lastReceived = lastReceived;
	}
	@JSON(format="dd-MMM-yyyy")
	public Date getLastSold() {
		return lastSold;
	}
	public void setLastSold(Date lastSold) {
		this.lastSold = lastSold;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public Integer getCommittedQuantity() {
		return committedQuantity;
	}
	public void setCommittedQuantity(Integer committedQuantity) {
		this.committedQuantity = committedQuantity;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isValid = super.equals(obj);
		if (!isValid && obj instanceof Item) {
			Item item = (Item)obj;
			isValid = this.getSku() != null && item.getSku() != null && this.getSku().equalsIgnoreCase(item.getSku());
		}
		return isValid;
	}
	public Integer getOnOrder() {
		return onOrder;
	}
	public void setOnOrder(Integer onOrder) {
		this.onOrder = onOrder;
	}
	public Integer getTransferOut() {
		return transferOut;
	}
	public void setTransferOut(Integer transferOut) {
		this.transferOut = transferOut;
	}
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	public Float getLength() {
		return length;
	}
	public void setLength(Float length) {
		this.length = length;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getToOrderQuantity() {
		return toOrderQuantity;
	}
	public void setToOrderQuantity(String toOrderQuantity) {
		this.toOrderQuantity = toOrderQuantity;
	}
}
