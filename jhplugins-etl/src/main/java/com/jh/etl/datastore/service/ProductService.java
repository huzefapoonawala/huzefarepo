package com.jh.etl.datastore.service;

import java.util.List;

import com.jh.etl.common.dto.ProductDto;

public interface ProductService {

	void persistProducts(List<ProductDto> products);
}
