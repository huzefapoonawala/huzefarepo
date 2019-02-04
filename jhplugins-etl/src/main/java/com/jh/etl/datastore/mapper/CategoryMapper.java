package com.jh.etl.datastore.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.datastore.entity.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

	@Named("ToCategory")
	@Mapping(target="parent", qualifiedByName= {"ToCategory"})
	Category toCategory(CategoryDto categoryDto);
	
	CategoryDto toCategoryDto(Category category);
	
	@Named("ToCategoryWithoutParent")
	@Mapping(ignore=true, target="parent")
	Category toCategoryExcludingParent(CategoryDto categoryDto);
	
	List<Category> toCategories(List<CategoryDto> categoryDtos);
	
	@IterableMapping(qualifiedByName= {"ToCategoryWithoutParent"})
	List<Category> toCategoriesWithoutParent(List<CategoryDto> categoryDtos);
}
