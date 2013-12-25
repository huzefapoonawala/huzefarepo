package com.jh.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.jh.vo.FTPUser;

public class FTPReader {
	
	private static Logger logger = Logger.getLogger(FTPReader.class);
	private final static String INVOICE_DIR_PATH = "INVOICES/";
	private final static String WEB_FILES_DIR_PATH = "orgillftp/webfiles/";
	
	private FTPClient ftpClient;
	
	private String server;
	public void setServer(String server) {
		this.server = server;
	}

	private List<Map<String, String>> ftpUsers;	
	public void setFtpUsers(List<Map<String, String>> ftpUsers) {
		this.ftpUsers = ftpUsers;
	}
	
	private String downloadFilePath;
	public void setDownloadFilePath(String downloadFilePath) {
		this.downloadFilePath = downloadFilePath;
		if (!this.downloadFilePath.endsWith("/") && !this.downloadFilePath.endsWith("\\")) {
			this.downloadFilePath += "\\";
		}
	}

	public void connect(String username, String password) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			return;
		}
		ftpClient = connect(username, password, server);
	}
	
	public FTPClient connect(String username, String password, String server) throws IOException {
		if (server == null || username == null || password == null) {
			throw new IOException("No server/username/passwordserver: " + server + " username: " + username + " password:" + password);
		}
		FTPClient ftpClient = new FTPClient();
		connect(ftpClient, username, password, server);
		return ftpClient;
	}
	
	public FTPClient connectWithProxy(FTPUser ftpUser, String proxyHost, int proxyPort) throws IOException {
		if (ftpUser == null || ftpUser.getHostname() == null || ftpUser.getUsername() == null || ftpUser.getPassword() == null) {
			throw new IOException("No server/username/password.");
		}
		FTPClient ftpClient = new FTPHTTPClient(proxyHost, proxyPort);
		connect(ftpClient, ftpUser.getUsername(), ftpUser.getPassword(), ftpUser.getHostname());
		return ftpClient;
	}
	
	private void connect(FTPClient ftpClient, String username, String password, String server) throws SocketException, IOException {
		ftpClient.connect(server);
		int reply = ftpClient.getReplyCode();		 
		if (!FTPReply.isPositiveCompletion(reply)) {
			logger.warn("FTP server " + server + " refused connection");
			disconnect();
			throw new IOException("FTP server " + server + " refused connection");
		}
		if (!ftpClient.login(username, password)) {
			logger.warn("FTP server " + server + " didnt accept login with username " + username + " and password "+ password); 
			disconnect();
			throw new IOException("FTP server " + server + " didnt accept login with username " + username + " and password "+ password);
		}
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(2048);
		ftpClient.enterLocalPassiveMode();
	}

	public void connect(Integer ftpUserIdx) throws IOException {
		connect(ftpUsers.get(ftpUserIdx).get("username"), ftpUsers.get(ftpUserIdx).get("password"));
	}
	
	public List<Map<String, String>> getAllPIFiles() throws IOException {
		List<Map<String, String>> fileDetails = new ArrayList<Map<String, String>>();
		int count = 1;
		for (int i = 0; i < ftpUsers.size(); i++) {
			connect(ftpUsers.get(i).get("username"), ftpUsers.get(i).get("password"));
			Map<String, String> map = null;
			for (FTPFile file : ftpClient.listFiles(INVOICE_DIR_PATH)) {
				if (file.getName().toLowerCase().endsWith(".oin")) {
					map = new HashMap<String, String>();
					map.put("id", Integer.valueOf(count++).toString());
					map.put("fileName", file.getName());
					map.put("lastModified", DateUtil.convertDate2String(DateUtil.addMillis2Date(file.getTimestamp().getTime(), 330*60*1000l), "dd-MMM-yyyy HH:mm"));
					map.put("userIdx", Integer.valueOf(i).toString());
					map.put("userName", ftpUsers.get(i).get("username"));
					fileDetails.add(map);
				}
			}
			disconnect();
		}
		return fileDetails;
	}
	
	public BufferedReader getPIFileDetails(String fileName) throws IOException {
		return new BufferedReader(new InputStreamReader(getFTPFileData(INVOICE_DIR_PATH, fileName)));
	}
	
	public InputStream getFTPFileData(String filePath, String fileName) throws IOException {
		return ftpClient.retrieveFileStream(filePath+fileName);
	}
	
	/*public String downloadWebFile(String filename) throws IOException {
		boolean isConnected = ftpClient != null && ftpClient.isConnected();
		if (!isConnected) {
			connect(0);
		}
		
		String filepath = downloadFilePath+filename, ftpFilePath = WEB_FILES_DIR_PATH+filename;
		File lf = new File(filepath+".downloaded"), localFile = null;
		FTPFile rf = null;
		for (FTPFile ff : ftpClient.listFiles(ftpFilePath)) {
			rf = ff;
		}
//		if (rf != null) {
			if (!lf.isFile() || rf.getTimestamp().getTimeInMillis() > lf.lastModified()) {
				logger.debug("Started downloading file '"+filename+"' with size "+rf.getSize()+" from ftp dir '"+WEB_FILES_DIR_PATH+"' to local dir "+downloadFilePath);
				if (rf.getSize() > 10*1024*1024) {
					ftpClient.setControlKeepAliveTimeout(60);
					logger.debug("Setting controlkeepalivetimeout to 60 seconds");
				}
				localFile = new File(filepath);
				FileOutputStream out = new FileOutputStream(localFile);
				try {
					ftpClient.retrieveFile(ftpFilePath,out);
				} catch (IOException e) {
					if (e instanceof SocketTimeoutException && rf.getSize() == localFile.length()) {
						logger.warn("Error occurred while downloading file "+filename+" from ftp dir "+WEB_FILES_DIR_PATH+" ["+e.getMessage()+"], but file was downloaded successfully hence ignoring this error.");
					}
					else{
						throw e;
					}
				} finally{
					if (out != null) {
						out.close();
					}
				}
				InputStream in = ftpClient.retrieveFileStream(ftpFilePath);
				copyFile(in, out);
				in.close();
				
				logger.debug("Completed downloading file '"+filename+"' from ftp dir '"+WEB_FILES_DIR_PATH+"' to local dir "+downloadFilePath);
				if (!lf.isFile()) {
					lf.createNewFile();
				} 
				lf.setLastModified(rf.getTimestamp().getTimeInMillis());
			} else {
//				filepath = null;
				logger.debug("'"+filename+"' from ftp dir '"+WEB_FILES_DIR_PATH+"' is not modified since last download.");
			}
		}
		else{
			filepath = null;
		}
		if (!isConnected) {
			disconnect();
		}
		return filepath;
//		return ftpUser.getLocalFolderPath()+filename;
	}*/
	
	public String downloadWebFile(String filename) throws IOException {
		boolean isConnected = ftpClient != null && ftpClient.isConnected();
		if (!isConnected) {
			connect(0);
		}
		FTPUser ftpUser = new FTPUser();
		ftpUser.setFtpFolderPath(WEB_FILES_DIR_PATH);
		ftpUser.setLocalFolderPath(downloadFilePath);
		ftpUser.setFileNames(new ArrayList<String>(Arrays.asList(new String[]{filename})));
		downloadFTPFiles(ftpClient, ftpUser);
		if (!isConnected) {
			disconnect();
		}
		return ftpUser.getLocalFolderPath()+filename;
	}
	
	public void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] bs = null;
		int MAX_TRANSFER_SIZE = 1024;
		while (in.available() > 0) {
			bs = new byte[in.available() > MAX_TRANSFER_SIZE ? MAX_TRANSFER_SIZE : in.available()];
			in.read(bs);
			out.write(bs);
		}
	}
	
	public void disconnect(FTPClient ftpClient) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}
	
	public void disconnect() throws IOException {
		disconnect(ftpClient);
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}
	
	public List<String> downloadFTPFiles(FTPClient ftpClient, FTPUser ftpUser) throws IOException {
		List<String> fileNames = null;
		if (ftpUser.getFileNames() != null) {
			fileNames = ftpUser.getFileNames();
		}
		else if (ftpUser.getFileExtension() != null) {
			if (!ftpClient.printWorkingDirectory().equals(ftpUser.getFtpFolderPath())) {
				ftpClient.changeWorkingDirectory(ftpUser.getFtpFolderPath());
			}
			fileNames = new ArrayList<String>();
			for (String ftpFileName : ftpClient.listNames()) {
				if (ftpFileName.toLowerCase().endsWith(ftpUser.getFileExtension().toLowerCase())) {
					fileNames.add(ftpFileName);
				}
			}
		}
		if (fileNames != null && !fileNames.isEmpty()) {
			String filepath = null, ftpFilePath = null;
			File lf = null, localFile = null;
			FTPFile rf = null;
			FileOutputStream out = null;
			for (String filename : fileNames) {
				filepath = ftpUser.getLocalFolderPath()+filename; 
				ftpFilePath = ftpUser.getFtpFolderPath()+filename;
				lf = new File(filepath+".downloaded");
				localFile = null;
				rf = null;
				for (FTPFile ff : ftpClient.listFiles(ftpFilePath)) {
					rf = ff;
				}
//				if (rf != null) {
					if (!lf.isFile() || rf.getTimestamp().getTimeInMillis() > lf.lastModified()) {
						logger.debug("Started downloading file '"+filename+"' with size "+rf.getSize()+" from ftp dir '"+ftpUser.getFtpFolderPath()+"' to local dir "+ftpUser.getLocalFolderPath());
						if (rf.getSize() > 10*1024*1024) {
							ftpClient.setControlKeepAliveTimeout(60);
							logger.debug("Setting controlkeepalivetimeout to 60 seconds");
						}
						localFile = new File(filepath);
						out = new FileOutputStream(localFile);
						try {
							ftpClient.retrieveFile(ftpFilePath,out);
						} catch (IOException e) {
							if (e instanceof SocketTimeoutException && rf.getSize() == localFile.length()) {
								logger.warn("Error occurred while downloading file "+filename+" from ftp dir "+WEB_FILES_DIR_PATH+" ["+e.getMessage()+"], but file was downloaded successfully hence ignoring this error.");
							}
							else{
								throw e;
							}
						} finally{
							if (out != null) {
								out.close();
							}
						}						
						logger.debug("Completed downloading file '"+filename+"' from ftp dir '"+WEB_FILES_DIR_PATH+"' to local dir "+downloadFilePath);
						if (!lf.isFile()) {
							lf.createNewFile();
						} 
						lf.setLastModified(rf.getTimestamp().getTimeInMillis());
					} else {
//						filepath = null;
						logger.debug("'"+filename+"' from ftp dir '"+WEB_FILES_DIR_PATH+"' is not modified since last download.");
					}
//				}
			}
		} else {
			logger.warn("No files available in ftp folder "+ftpUser.getFtpFolderPath()+" to be downloaded.");
		}
		return fileNames;
	}
	
	public List<String> downloadFTPFiles(FTPUser ftpUser) throws IOException {
		FTPClient ftpClient = connect(ftpUser.getUsername(), ftpUser.getPassword(), ftpUser.getHostname());
		List<String> filenames = downloadFTPFiles(ftpClient, ftpUser);
		disconnect(ftpClient);
		return filenames;
	}
}
