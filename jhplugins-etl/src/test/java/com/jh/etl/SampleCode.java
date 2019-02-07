package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasProperty;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.datastore.mapper.CategoryMapper;

public class SampleCode {

	public static void main(String[] args) {
		String ftpFolderpath = "orgillftp/webfiles/";
		String ftpFilename = "abc.txt";
		System.out.println(Optional.ofNullable(ftpFolderpath).orElse("empty"));
		
		ProductDto productDto = ProductDto.builder().sku("abc123").build();
		System.out.println(productDto.toString());
		productDto.toBuilder().title("this is title").build();
		System.out.println(productDto);
		
		Map<String, ProductDto> map = Map.of("a", ProductDto.builder().sku("a").build(), "b", ProductDto.builder().sku("b").build());
		List<ProductDto> list = Optional.ofNullable(map).map(m -> new ArrayList<ProductDto>(m.values())).orElse(new ArrayList<ProductDto>());
		System.out.println(list.toString());
		
		Category c = new Category();
		Optional.ofNullable(c).ifPresentOrElse((cat)->{}, ()->System.out.println("Null"));
	}
	
//	@Test
	public void testIgnoreFieldMapper() {
		CategoryDto parentDto = CategoryDto.builder().code("catetestP").name("Test Category Parent").supplier(Supplier.ORGILL).build(),
				childDto = CategoryDto.builder().code("catetestC").name("Test Category Child").supplier(Supplier.ORGILL).parent(parentDto).build();
		
		Category c = Mappers.getMapper(CategoryMapper.class).toCategoryExcludingParent(childDto);
		assertThat(c.getParent(), IsNull.nullValue());
		
		c = Mappers.getMapper(CategoryMapper.class).toCategory(childDto);
		assertThat(c.getParent(), IsNull.notNullValue());
		
		List<Category> list = Mappers.getMapper(CategoryMapper.class).toCategoriesWithoutParent(List.of(parentDto, childDto));
		assertThat(list, Every.everyItem(Matchers.hasProperty("parent", IsNull.nullValue())));
	}
	
//	@Test
	public void testConvertList2Map() {
		List<CategoryDto> list = List.of(
				CategoryDto.builder().name("c1").code("1").supplier(Supplier.ORGILL).build(),
				CategoryDto.builder().name("c2").code("2").supplier(Supplier.ORGILL).build(),
				CategoryDto.builder().name("c3").code("3").supplier(Supplier.ORGILL).build()
				);
		Map<String, CategoryDto> map = list.stream().collect(Collectors.toMap(category->String.format("%s,%s", category.getCode(),category.getSupplier()), category->category));
		assertThat(map.keySet(), IsCollectionWithSize.hasSize(3));
	}
	
//	@Test
	public void testListContainsProperty() {
		CategoryDto c1 = CategoryDto.builder().name("c1").code("1").supplier(Supplier.ORGILL).build();
		List<CategoryDto> list = List.of(
				c1,
				CategoryDto.builder().name("c2").code("2").supplier(Supplier.ORGILL).parent(c1).build(),
				CategoryDto.builder().name("c3").code("3").supplier(Supplier.ORGILL).build()
				);
		assertThat(list, IsCollectionContaining.hasItem(HasPropertyWithValue.hasProperty("parent", HasPropertyWithValue.hasProperty("code", Is.is("1")))));
	}
	
//	@Test
	public void testListWithCondition() {
		List<String> l1 = new ArrayList<>(),
				l2 = Arrays.asList("aa","bb","aa","cc");
		l2.forEach(p -> {
			Optional.ofNullable(p).filter(c -> !l1.contains(c)).ifPresent(c -> l1.add(c));
		});
		l1.forEach(System.out::println);
		
		String sku = null;
		Supplier supplier = null;
		System.out.println(String.format("%s,%s", sku, supplier));
	}
}
