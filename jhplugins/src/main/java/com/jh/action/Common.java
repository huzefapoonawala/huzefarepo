package com.jh.action;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.jh.vo.ApplicationProperty;
import com.opensymphony.xwork2.ActionSupport;

public class Common extends ActionSupport {

	private static final long serialVersionUID = 469247002487366634L;

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

	private Properties applicationProperties;	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public String fetchApplicationProperties() throws Exception{
		for (ApplicationProperty ap : properties) {
			if (!StringUtils.isBlank(ap.getPropertyKey())) {
				ap.setPropertyValue(applicationProperties.getProperty(ap.getPropertyKey()));
			}
		}
		return SUCCESS;
	}
}
