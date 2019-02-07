package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.datastore.dao.CategoryRepository;

public class CategoryRepositoryTests extends TestSetup {

	@Autowired private CategoryRepository categoryRepository;
	
	@Test
	public void test_findBySuppliersAndCodes() {
		List<String> params = null;
		assertThat(categoryRepository.findAll(categoryRepository.inWithConcatCodeAndSupplier(params)), IsEmptyCollection.empty());
		params = new ArrayList<>();
		assertThat(categoryRepository.findAll(categoryRepository.inWithConcatCodeAndSupplier(params)), IsEmptyCollection.empty());
		params = List.<String>of("UNITTEST,ORGILL");
		assertThat(categoryRepository.findAll(categoryRepository.inWithConcatCodeAndSupplier(params)), IsCollectionWithSize.hasSize(params.size()));
	}
}
