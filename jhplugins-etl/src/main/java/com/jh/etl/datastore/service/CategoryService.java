package com.jh.etl.datastore.service;

import java.util.List;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.datastore.entity.Category;

public interface CategoryService {

	void persistCategories(List<CategoryDto> categories);

	List<Category> getCategoriesByCodesAndSuppliers(List<String> params);
}
