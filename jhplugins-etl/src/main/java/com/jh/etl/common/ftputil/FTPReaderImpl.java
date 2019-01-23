package com.jh.etl.common.ftputil;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPReaderImpl implements FTPReader {

	private final Log LOG = LogFactory.getLog(FTPReaderImpl.class);
	
	private FTPUser ftpUser;
	private FTPClient ftpClient;
	
	public FTPReaderImpl(FTPUser ftpUser) {
		this.ftpUser = ftpUser;
		this.ftpClient = new FTPClient();
	}
	
	private void connect() throws IOException {
		LOG.debug(String.format("Establishing connection to ftp server with host %s...", ftpUser.getHost()));
		ftpClient.connect(ftpUser.getHost());
		int reply = ftpClient.getReplyCode();		 
		if (!FTPReply.isPositiveCompletion(reply)) {
			//disconnect();
			throw new IOException(String.format("FTP server with host %s refused connection", ftpUser.getHost()));
		}
		if (ftpUser.getUsername() != null && !ftpClient.login(ftpUser.getUsername(), ftpUser.getPassword())) { 
			//disconnect();
			throw new IOException(String.format("FTP server with host %s didnt accept login with username %s and password %s.", ftpUser.getHost(), ftpUser.getUsername(), ftpUser.getPassword()));
		}
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(2048);
		ftpClient.enterLocalPassiveMode();
		/*ftpClient.filter(Predicate.not(FTPClient::isConnected)).ifPresent((fcli) -> {
			try {
				fcli.connect(ftpUser.getHost());
				LOG.debug(String.format("FTP connection established with host %s", ftpUser.getHost()));
			} catch (Exception e) {
				LOG.error(String.format("Error occurred while establishing FTP connection with %s.", ftpUser.getHost()), e);
			}
		});*/
	}
	
	@Override
	public String downloadFile(String filename) {
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
