package com.jh.util;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class FreemarkerTemplateUtil {

	private static Logger logger = Logger.getLogger(FreemarkerTemplateUtil.class);
	
	private Map<String, Template> templates;
	private Configuration configuration;	
	
	private Map<String, String> templatePaths;
	public void setTemplatePaths(Map<String, String> templatePaths) {
		this.templatePaths = templatePaths;
	}
	
	private FreeMarkerConfigurer templateConfigurer;	
	public void setTemplateConfigurer(FreeMarkerConfigurer templateConfigurer) {
		this.templateConfigurer = templateConfigurer;
	}

	private void createConfiguration(){
//		this.configuration = new Configuration();
//		this.configuration.setClassForTemplateLoading(getClass(), "/");
		configuration = templateConfigurer.getConfiguration();
		this.configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		this.configuration.setDefaultEncoding("UTF-8");
		this.configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		this.configuration.setIncompatibleImprovements(new Version(2, 3, 20));
	}
	
	private void createTemplates() {
		if (configuration == null) {
			createConfiguration();
		}
		templates = new HashMap<String, Template>();
		for (Entry<String, String> path : this.templatePaths.entrySet()) {
			try {
				templates.put(path.getKey(), this.configuration.getTemplate(path.getValue()));
			} catch (Exception e) {
				logger.error("Error while creating template at path "+path.getValue(), e);
			}
		}
	}

	public Template getTemplate(String key) throws Exception {
		if (templates == null) {
			createTemplates();
		}
		return templates.get(key);
	}
	
	public File createFileFromTemplate(String templateName, String filePath, String fileName, Object data) throws Exception {
		File file = new File(filePath,fileName);
		Writer out = new FileWriter(file);
		getTemplate(templateName).process(data, out);
		out.flush();
		out.close();
		return file;
	}
}
