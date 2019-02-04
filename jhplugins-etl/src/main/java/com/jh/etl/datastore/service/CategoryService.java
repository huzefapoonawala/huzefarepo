package com.jh.etl.datastore.service;

import java.util.List;

import com.jh.etl.common.dto.CategoryDto;

public interface CategoryService {

	void persistCategories(List<CategoryDto> categories);
}
