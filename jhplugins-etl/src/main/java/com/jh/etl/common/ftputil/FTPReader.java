package com.jh.etl.common.ftputil;

public interface FTPReader {

	String downloadFile(String localFolderpath, String ftpFolderpath, String ftpFilename) throws Exception;

	String downloadFile(String ftpFilename) throws Exception;
	
}
