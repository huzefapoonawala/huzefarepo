package com.jh.etl;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jh.etl.common.ftputil.FTPReader;

public abstract class TestSetupWithMockingFtpReader extends TestSetup {

	@MockBean protected FTPReader orgillProductDataFTPReader;
	
	protected static final String CATEGORY_FILENAME = "./sampledata/WEB_DEPT_FOR_UNITTEST.TXT";
	
	@Before
	public void beforeStartTest() throws Exception {
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT.TXT")).thenReturn(CATEGORY_FILENAME);
		
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_DEPT_SKU.TXT"))
			.thenReturn( "./sampledata/WEB_DEPT_SKU_FOR_UNITTEST.TXT")
//			.thenReturn("./sampledata/WEB_DEPT_SKU.TXT")
		;
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("WEB_SKU_COMMON.TXT"))
				.thenReturn("./sampledata/WEB_SKU_COMMON_FOR_UNITTEST.TXT")
		//		.thenReturn("./sampledata/WEB_SKU_COMMON.TXT")
				;
		
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("orgill_skudesc.TXT"))
			.thenReturn("./sampledata/orgill_skudesc_FOR_UNITTEST.TXT");
		
		Mockito.when(this.orgillProductDataFTPReader.downloadFile("skudescadd.TXT"))
			.thenReturn("./sampledata/skudescadd_FOR_UNITTEST.txt");
	}
}
