package com.jh.etl.common.dto;

import com.jh.etl.common.enums.Supplier;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder=true)
@ToString(includeFieldNames=true)
public class ProductDto {
	
	private String sku;
	private CategoryDto category;
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
	private Supplier supplier;
}
