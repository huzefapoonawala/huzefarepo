package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.common.interfaces.DataExtracter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrgillProductDataExtracterTests extends TestSetupWithMockingFtpReader {

	@Autowired private DataExtracter<List<ProductDto>> orgillProductDataExtracter;
	
	@Test
	public void testExtractData() throws Exception {
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT_SKU.TXT"))
				.thenReturn( "./sampledata/WEB_DEPT_SKU_FOR_UNITTEST.TXT")
//				.thenReturn("./sampledata/WEB_DEPT_SKU.TXT")
				;
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_SKU_COMMON.TXT"))
				.thenReturn("./sampledata/WEB_SKU_COMMON_FOR_UNITTEST.TXT")
//				.thenReturn("./sampledata/WEB_SKU_COMMON.TXT")
				;
		List<ProductDto> list = orgillProductDataExtracter.extractData();
		assertThat(list, IsCollectionWithSize.hasSize(100));
		assertThat(list, IsCollectionContaining.hasItem(
				ProductDto.builder()
					.sku("0010041")
					.category(CategoryDto.builder().code("1010610605").build())
					.title("07661/07561 TOOL HOLDER 24IN")
					.description("HOLDER TOOL BAR STL 24IN BLACK")
					.width(9.75f)
					.height(5.5f)
					.length(26.75f)
					.weight(4.17f)
					.upc("095421076611")
					.retailPrice(16.99d)
					.retailUnit("CD")
					.imageLink("0010041.JPG")
					.build()
				));
	}
}
