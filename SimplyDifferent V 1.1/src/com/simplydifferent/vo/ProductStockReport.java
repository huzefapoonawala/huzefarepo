package com.simplydifferent.vo;

import java.util.ArrayList;
import java.util.List;

public class ProductStockReport {

	private List<Product> products;

	public List<Product> getProducts() {
		if (products == null) {
			products = new ArrayList<Product>();
		}
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
