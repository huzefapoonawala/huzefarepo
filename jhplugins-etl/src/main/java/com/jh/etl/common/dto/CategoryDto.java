package com.jh.etl.common.dto;

import com.jh.etl.common.enums.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Builder
public class CategoryDto {

	private String name;
	private String code;
	private Supplier supplier;
	private CategoryDto parent;
	
	public String concatCodeAndSupplier() {
		return String.format("%s,%s", this.code,this.supplier);
	}
}
