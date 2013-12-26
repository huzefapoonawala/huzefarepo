package com.simplydifferent.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_master")
public class ProductStock {

	@Id @Column(name="PRODUCT_ID") private Integer productId;
	@Column(name="IN_STOCK") private Double stock;
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Double getStock() {
		return stock;
	}
	public void setStock(Double stock) {
		this.stock = stock;
	}
}
