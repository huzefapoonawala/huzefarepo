package test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.management.MBeanServerConnection;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.SystemPropertyUtils;


public class ServerManagementTest extends Setup{

	@Resource private ApplicationContext context;
	@Resource private Properties applicationProperties;
	@Value("${file.systempath.image}") private String value1;
	
	@Test
	public void testApplicationProperty() throws IOException {
		Assert.assertNotNull(context);
		System.out.println(value1);
		File imgDir = new File(value1);
//		imgDir.createNewFile();
		System.out.println(imgDir.mkdir());
//		FileUtils.forceMkdir(imgDir);
		System.out.println(imgDir.isDirectory());
//		Assert.assertNotNull(context.getBean(PropertySourcesPlaceholderConfigurer.class));
		System.out.println(applicationProperties.getProperty("dynamics.database.ip"));
		System.out.println(applicationProperties.getProperty("website.database.url"));
	}
	
	public static void main(String[] args) {
		try {
			MBeanServerConnection server = (MBeanServerConnection) new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
