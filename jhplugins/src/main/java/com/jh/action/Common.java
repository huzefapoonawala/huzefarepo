package com.jh.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.jh.util.PropertiesUtil;
import com.jh.vo.ApplicationProperty;
import com.jh.vo.FTPUser;
import com.opensymphony.xwork2.ActionSupport;

public class Common extends ActionSupport {

	private static final long serialVersionUID = 469247002487366634L;
	private static Logger logger = Logger.getLogger(Common.class);

	private ApplicationProperty property;	
	public ApplicationProperty getProperty() {
		return property;
	}
	public void setProperty(ApplicationProperty property) {
		this.property = property;
	}
	
	private List<ApplicationProperty> properties;
	public List<ApplicationProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<ApplicationProperty> properties) {
		this.properties = properties;
	}
	
	private boolean validDbConnection;
	public boolean getValidDbConnection() {
		return validDbConnection;
	}
	
	private boolean propertiesPersisted;
	public boolean getPropertiesPersisted() {
		return propertiesPersisted;
	}
	
	private boolean validFtpConnection;
	public boolean getValidFtpConnection() {
		return validFtpConnection;
	}
	
	private FTPUser ftpUser;
	public FTPUser getFtpUser() {
		return ftpUser;
	}
	public void setFtpUser(FTPUser ftpUser) {
		this.ftpUser = ftpUser;
	}

	private PropertiesUtil propertiesUtil;	
	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}
	
	public String fetchApplicationProperties() throws Exception{
		propertiesUtil.fetchApplicationProperties(properties);
		return SUCCESS;
	}
	
	public String checkDBConnection() {
		validDbConnection = propertiesUtil.checkDbConnection(properties);
		return SUCCESS;
	}
	
	public String persistApplicationProperties() throws Exception {
		try {
			propertiesUtil.persistApplicationProperties(properties);
			propertiesPersisted = true;
		} catch (Exception e) {
			logger.error("Error occurred while persisting application properties.", e);
			propertiesPersisted = false;
		}
		return SUCCESS;
	}
	
	public String checkFtpConnection() {
		validFtpConnection = propertiesUtil.checkFtpConnection(ftpUser);
		return SUCCESS;
	}
}
