package com.jh.etl.common.ftputil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FTPConfigs {

	@Autowired private FTPUsers ftpUsers;
	
	@Bean
	public FTPReader orgillProductDataFTPReader() {
		FTPReader ftpReader = new FTPReaderImpl(ftpUsers.getOrgillProductData());
		return ftpReader;
	}
}
