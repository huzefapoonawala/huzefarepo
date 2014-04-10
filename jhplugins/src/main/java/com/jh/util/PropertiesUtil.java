package com.jh.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jh.vo.ApplicationProperty;
import com.jh.vo.FTPUser;

public class PropertiesUtil implements ApplicationPropertiesConstant {

	private Properties applicationProperties;	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}	
	public Properties getApplicationProperties() {
		return applicationProperties;
	}

	private FTPReader ftpReader;	
	public void setFtpReader(FTPReader ftpReader) {
		this.ftpReader = ftpReader;
	}

	public List<ApplicationProperty> fetchApplicationProperties(List<ApplicationProperty> properties) {
		for (ApplicationProperty ap : properties) {
			if (!StringUtils.isBlank(ap.getPropertyKey())) {
				if (!StringUtils.isBlank(ap.getPropertyType()) && StringUtils.equalsIgnoreCase(ap.getPropertyType(), "SYS_ENV")) {
					ap.setPropertyValue(System.getenv(ap.getPropertyKey()));
				} else {
					ap.setPropertyValue(applicationProperties.getProperty(ap.getPropertyKey()));
				}
			}
		}
		return properties;
	}
	
	private String executeCommand(String[] command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
	
	public boolean checkDbConnection(List<ApplicationProperty> properties) {
		boolean isConnectionValid = false;
		try {
			String hostname = null, dbname = null, username = null, password = null;
			for (ApplicationProperty p : properties) {
				if (p.getPropertyKey().equals(DATABASE_IP_KEY)) {
					hostname = p.getPropertyValue();
				} 
				else if (p.getPropertyKey().equals(DATABASE_NAME_KEY)) {
					dbname = p.getPropertyValue();
				}
				else if (p.getPropertyKey().equals(DATABASE_USERNAME_KEY)) {
					username = p.getPropertyValue();
				}
				else if (p.getPropertyKey().equals(DATABASE_PASSWORD_KEY)) {
					password = p.getPropertyValue();
				}
			}
			StringBuilder url = new StringBuilder().append("jdbc:jtds:sqlserver://").append(hostname).append(":1433/").append(dbname);
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url.toString(),username,password);
			Statement statement = connection.createStatement();
			isConnectionValid = statement.execute("select current_timestamp");
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnectionValid;
	}
	
	
	public void persistApplicationProperties(List<ApplicationProperty> appProperties) throws Exception {
		File propFile = new File(System.getenv(PROPERTIES_FILE_PATH_KEY));
		if (!propFile.isFile()) {
			if (!propFile.getParentFile().isDirectory()) {
				FileUtils.forceMkdir(propFile.getParentFile());
			}
			propFile.createNewFile();
		}
		Properties appProps = new Properties();
		FileInputStream in = new FileInputStream(propFile);
		appProps.load(in);
		in.close();
		for (ApplicationProperty ap : appProperties) {
			if (StringUtils.isBlank(ap.getPropertyType()) || !StringUtils.equalsIgnoreCase(ap.getPropertyType(), SYSTEM_ENV_KEY)) {
				appProps.put(ap.getPropertyKey(), ap.getPropertyValue());
			}
		}
		FileOutputStream out = new FileOutputStream(propFile);
		appProps.store(out, null);
		out.close();
	}
	
	public boolean checkFtpConnection(FTPUser ftpUser) {
		boolean isConnectionValid = false;
		try {
			ftpReader.connect(ftpUser.getUsername(), ftpUser.getPassword(), ftpUser.getHostname());
			isConnectionValid = ftpReader.getFtpClient().isConnected();
			ftpReader.disconnect(ftpReader.getFtpClient());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isConnectionValid;
	}
	
	public static void main(String[] args) {
		
		
		PropertiesUtil pu = new PropertiesUtil();
//		System.out.println(pu.executeCommand("setx \"test.env.java\" \"java\" -m"));
		String [] commands = {"C:\\Users\\hpoonaw\\Desktop\\Elevate.exe","setx","\"test.elevate\"","\"var from elevate java\"","-m"};
		System.out.println(pu.executeCommand(commands));
		System.out.println(System.getenv("test.elevate"));
		/*String propertiesFile = System.getenv(PROPERTIES_FILE_PATH_KEY);
		File propFile = new File(propertiesFile);
		System.out.println(propFile.getParentFile().isDirectory());
		if (!propFile.isFile()) {
			System.out.println(propFile.getParentFile());
		}*/
	}
}
