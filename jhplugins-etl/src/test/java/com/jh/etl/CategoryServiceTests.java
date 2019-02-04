package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.dao.CategoryRepository;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.datastore.mapper.CategoryMapper;
import com.jh.etl.datastore.service.CategoryService;

public class CategoryServiceTests extends TestSetupWithMockingFtpReader{

	@Autowired private CategoryService categoryService;
	@Autowired private CategoryRepository categoryRepository;
	
	@Autowired private CategoryMapper categoryMapper;
	
	@Test
	public void testIfCategoryMapperIsNotNull() {
		assertThat(categoryMapper, IsNull.notNullValue());
	}
	
	@Test
	public void testPesristCategories() {
		CategoryDto c1 = CategoryDto.builder().code("catetest1").name("Test Category 1").supplier(Supplier.ORGILL).build(),
			c2 = CategoryDto.builder().code("catetest2").name("Test Category 2").supplier(Supplier.ORGILL).parent(c1).build(),
			c3 = CategoryDto.builder().code("catetest3").name("Test Category 3").supplier(Supplier.ORGILL).parent(c2).build(),
			unitTestCategory = CategoryDto.builder().code("UNITTEST").name("Category For Unit Test").supplier(Supplier.ORGILL).build(),
			c4 = CategoryDto.builder().code("catetest4").name("Test Category 4").supplier(Supplier.ORGILL).parent(unitTestCategory).build();
		List<CategoryDto> list = List.of(c1,c2,c3, c4,unitTestCategory);		
		categoryService.persistCategories(list);
		List<Category> cl = categoryRepository.findBySuppliersAndCodes(list.stream().map(CategoryDto::concatCodeAndSupplier).collect(Collectors.toList()));
		assertThat(cl, IsCollectionWithSize.hasSize(list.size()));
		assertThat(cl, IsCollectionContaining.hasItem(HasPropertyWithValue.hasProperty("parent", HasPropertyWithValue.hasProperty("code", Is.is(c3.getParent().getCode())))));
		cl.removeIf(c -> c.getCode().equals(unitTestCategory.getCode()));
		categoryRepository.deleteAll(cl);
	}
}
