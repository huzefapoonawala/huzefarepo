package com.jh.etl.common.ftputil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FTPUser {
	private String host;
	private int port;
	private String username;
	private String password;
	private String ftpFolderpath;
	private String localFolderpath;
}
