package com.jh.vo;

import java.util.List;

public class FTPUser {

	private String hostname;
	private Integer port;
	private String username;
	private String password;
	private String ftpFolderPath;
	private String localFolderPath;
	private String fileExtension;
	private List<String> fileNames;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFtpFolderPath() {
		return ftpFolderPath;
	}
	public void setFtpFolderPath(String ftpFolderPath) {
		this.ftpFolderPath = ftpFolderPath;
		if (!this.ftpFolderPath.endsWith("/") && !this.ftpFolderPath.endsWith("\\")) {
			this.ftpFolderPath = this.ftpFolderPath+"/";
		}
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLocalFolderPath() {
		return localFolderPath;
	}
	public void setLocalFolderPath(String localFolderPath) {
		this.localFolderPath = localFolderPath;
		if (!this.localFolderPath.endsWith("/") && !this.localFolderPath.endsWith("\\")) {
			this.localFolderPath = this.localFolderPath+"/";
		}
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
}
