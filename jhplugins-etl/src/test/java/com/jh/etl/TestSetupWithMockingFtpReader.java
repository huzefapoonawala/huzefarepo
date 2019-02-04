package com.jh.etl;

import org.springframework.boot.test.mock.mockito.MockBean;

import com.jh.etl.common.ftputil.FTPReader;

public abstract class TestSetupWithMockingFtpReader extends TestSetup {

	@MockBean protected FTPReader orgillProductDataFTPReader;
}
