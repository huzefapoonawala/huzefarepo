package com.jh.vo;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class PurchaseOrderDetails extends Item {

	private Integer orderQuantity;
	private Double totalOrderCost;
	private Integer quantitySoldRecently;
	
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Double getTotalOrderCost() {
		return totalOrderCost;
	}
	public void setTotalOrderCost(Double totalOrderCost) {
		this.totalOrderCost = totalOrderCost;
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
	public Integer getQuantitySoldRecently() {
		return quantitySoldRecently;
	}
	public void setQuantitySoldRecently(Integer quantitySoldRecently) {
		this.quantitySoldRecently = quantitySoldRecently;
	}
}
