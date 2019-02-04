package com.jh.etl.datastore.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.enums.Supplier;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder=true)
@ToString(includeFieldNames=true)
@Entity
public class Product {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
	private String sku;
	@ManyToOne @JoinColumn(name="CATEGORY_ID") private CategoryDto category;
	private String title;
	private String description;
	private Float width;
	private Float height;
	private Float length;
	private Float weight;
	private String upc;
	private Double retailPrice;
	private String retailUnit;
	private String imageLink;
	@Enumerated(EnumType.STRING) private Supplier supplier;
}