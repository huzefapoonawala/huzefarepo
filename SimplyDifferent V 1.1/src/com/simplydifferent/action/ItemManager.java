package com.simplydifferent.action;

import com.opensymphony.xwork2.ActionSupport;

public class ItemManager extends ActionSupport {

	private static final long serialVersionUID = -1100160093672925244L;
	private String itemList;

	public String saveItems() {
		System.out.println(itemList);
		return SUCCESS;
	}

	public String getItemList() {
		return itemList;
	}

	public void setItemList(String itemList) {
		this.itemList = itemList;
	}
}
