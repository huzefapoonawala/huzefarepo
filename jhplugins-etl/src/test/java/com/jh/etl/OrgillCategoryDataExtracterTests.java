package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.common.interfaces.DataExtracter;
import com.jh.etl.orgilldata.extract.OrgillCategoryDataExtracter;

public class OrgillCategoryDataExtracterTests extends TestSetupWithMockingFtpReader {
	
	@Autowired private DataExtracter<List<CategoryDto>> orgillCategoryDataExtracter;
	
	@Test
	public void testTransformData() throws Exception {
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT.TXT")).thenReturn("./sampledata/WEB_DEPT_FOR_UNITTEST.TXT");
		List<CategoryDto> list = orgillCategoryDataExtracter.extractData();
		assertThat(list, IsCollectionWithSize.hasSize(10));
		assertThat(list, IsCollectionContaining.hasItem(new CategoryDto("HANDSAWS","1010110120",Supplier.ORGILL,CategoryDto.builder().code("1010100000").build())));
	}
	
	@Configuration
	@Import(OrgillCategoryDataExtracter.class)
	static class Config{
		
	}
}
