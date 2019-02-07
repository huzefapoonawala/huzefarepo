package com.jh.etl;

import static org.junit.Assert.assertThat;

import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.dao.CategoryRepository;
import com.jh.etl.datastore.dao.ProductRepository;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.datastore.entity.Product;
import com.jh.etl.orgilldata.OrgillDataEtlOrchestrator;

public class OrgillDataEtlOrchestratorTests extends TestSetupWithMockingFtpReader {

	@Autowired private OrgillDataEtlOrchestrator orgillDataEtlOrchestrator;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private ProductRepository productRepository;
	
	@Test
	public void testInitiateEtl() throws Exception {
		orgillDataEtlOrchestrator.initiateEtl();
		String childCategoryCode = "1010610605", parentCategoryCode = "1010600000";
		Category category = categoryRepository.findBySupplierAndCode(Supplier.ORGILL, childCategoryCode);
		assertThat(category, HasPropertyWithValue.hasProperty("parent", HasPropertyWithValue.hasProperty("code", Is.is(parentCategoryCode))));
		
		String sku = "0010041";
		Product product = productRepository.findBySkuAndSupplier(sku, Supplier.ORGILL);
		assertThat(product, HasPropertyWithValue.hasProperty("category", HasPropertyWithValue.hasProperty("code", Is.is(childCategoryCode))));
	}
}