package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.datastore.dao.ProductRepository;
import com.jh.etl.datastore.projection.ProductIdAndSkuAndSupplier;

public class ProductRepositoryTests extends TestSetup {

	@Autowired private ProductRepository productRepository;
	
	@Test
	public void test_findBySkusAndSuppliers() {
		List<String> params = null;
		List<ProductIdAndSkuAndSupplier> result = productRepository.findBySkusAndSuppliers(params);
		assertThat(result, IsEmptyCollection.empty());
	}
}
