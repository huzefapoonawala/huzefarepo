package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.datastore.dao.ProductRepository;
import com.jh.etl.datastore.projection.ProductIdAndSkuAndSupplier;
import com.jh.etl.datastore.service.ProductService;
import com.jh.etl.orgilldata.extract.OrgillProductDataExtracter;

public class ProductServiceTests extends TestSetupWithMockingFtpReader {

	@Autowired private OrgillProductDataExtracter orgillProductDataExtracter;
	@Autowired private ProductService productService;
	@Autowired private ProductRepository productRepository;
	
	@Test
	public void testPersistProducts() throws Exception {
		List<ProductDto> products = orgillProductDataExtracter.extractData();
		productService.persistProducts(products);
		List<ProductIdAndSkuAndSupplier> storedProducts = productRepository.findBySkusAndSuppliers(products.stream().map(ProductDto::concatSkuAndSupplier).collect(Collectors.toList()));
		assertThat(storedProducts, IsCollectionWithSize.hasSize(products.size()));
	}
}
