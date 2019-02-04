package com.jh.etl;

import static org.junit.Assert.assertThat;

import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.dao.CategoryRepository;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.orgilldata.OrgillDataEtlOrchestrator;

public class OrgillDataEtlOrchestratorTests extends TestSetupWithMockingFtpReader {

	@Autowired private OrgillDataEtlOrchestrator orgillDataEtlOrchestrator;
	@Autowired private CategoryRepository categoryRepository;
	
	@Test
	public void testInitiateEtl() throws Exception {
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT.TXT"))
			.thenReturn( "./sampledata/WEB_DEPT_FOR_UNITTEST.TXT")
//			.thenReturn("./sampledata/WEB_DEPT_SKU.TXT")
		;
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT_SKU.TXT"))
			.thenReturn( "./sampledata/WEB_DEPT_SKU_FOR_UNITTEST.TXT")
//			.thenReturn("./sampledata/WEB_DEPT_SKU.TXT")
		;
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_SKU_COMMON.TXT"))
				.thenReturn("./sampledata/WEB_SKU_COMMON_FOR_UNITTEST.TXT")
		//		.thenReturn("./sampledata/WEB_SKU_COMMON.TXT")
		;
		orgillDataEtlOrchestrator.initiateEtl();
		String childCategoryCode = "1010110105", parentCategoryCode = "1010100000";
		Category category = categoryRepository.findBySupplierAndCode(Supplier.ORGILL, childCategoryCode);
		assertThat(category, HasPropertyWithValue.hasProperty("parent", HasPropertyWithValue.hasProperty("code", Is.is(parentCategoryCode))));
	}
}