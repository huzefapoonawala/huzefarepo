package com.jh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import com.jh.vo.FTPUser;

public class FTPWriter extends FTPReader {

	public FTPClient connect(FTPUser user) throws IOException {
		return connect(user.getUsername(), user.getPassword(), user.getHostname());
	}
	
	public void uploadFileToFTP(FTPUser user, FTPClient client, String filePath, String fileName) throws IOException {
		File file = new File(filePath, fileName);
		uploadFileToFTP(user, client, file);
	}
	
	public void uploadFileToFTP(FTPUser user, FTPClient client, File fileToUpload) throws IOException {
		FileInputStream in = new FileInputStream(fileToUpload);
		changeFTPWorkingFolder(client, user.getFtpFolderPath());
		client.storeFile(fileToUpload.getName(), in);
		in.close();
	}
	
	public void checkAndUploadFile2FTP(FTPUser user, FTPClient client, File fileToUpload) throws IOException {
		/*if (!client.isConnected()) {
			client = connect(user);
		}*/
		changeFTPWorkingFolder(client, user.getFtpFolderPath());
		if (client.listNames(fileToUpload.getName()).length == 0) {
			uploadFileToFTP(user, client, fileToUpload);
		}
	}
	
	private void changeFTPWorkingFolder(FTPClient client, String folderPath) throws IOException{
		if (!client.printWorkingDirectory().equalsIgnoreCase(folderPath)) {
			client.changeWorkingDirectory(folderPath);
		}
	}
}
