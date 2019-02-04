package com.jh.etl.datastore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.datastore.entity.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	Product toProduct(ProductDto productDto);
}
