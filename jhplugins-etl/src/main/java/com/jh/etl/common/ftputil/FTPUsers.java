package com.jh.etl.common.ftputil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="ftp")
@Getter
@Setter
public class FTPUsers {

	private FTPUser orgillProductData;
}
