package com.jh.etl.common.ftputil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.jh.etl.common.util.ExceptionHandlers;

import lombok.SneakyThrows;

public class FTPReaderImpl implements FTPReader {

	private final Log LOG = LogFactory.getLog(FTPReaderImpl.class);
	
	private FTPUser ftpUser;
	private final FTPClient ftpClient;
	
	public FTPReaderImpl(FTPUser ftpUser) {
		this.ftpUser = ftpUser;
		this.ftpClient = new FTPClient();
	}
	
	@SneakyThrows
	private void connect() {
		LOG.debug(String.format("Establishing connection to FTP server with host %s...", ftpUser.getHost()));
		ftpClient.connect(ftpUser.getHost());
		int reply = ftpClient.getReplyCode();		 
		if (!FTPReply.isPositiveCompletion(reply)) {
			//disconnect();
			throw new IOException(String.format("FTP server with host %s refused connection...", ftpUser.getHost()));
		}
		if (ftpUser.getUsername() != null && !ftpClient.login(ftpUser.getUsername(), ftpUser.getPassword())) { 
			//disconnect();
			throw new IOException(String.format("FTP server with host %s didnt accept login credentials.", ftpUser.getHost()));
		}
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(2048);
		ftpClient.enterLocalPassiveMode();
		LOG.debug(String.format("Established connection to FTP server with host %s.", ftpUser.getHost()));
	}

	private void checkAndConnect() {
		Optional.of(ftpClient).filter(Predicate.not(FTPClient::isConnected)).ifPresent(fcli -> connect());
	}
	
	@PreDestroy
	private void disconnect() {
		LOG.debug(String.format("Disconnecting FTP server with host %s...", ftpUser.getHost()));
		Optional.ofNullable(ftpClient).filter(FTPClient::isConnected).ifPresent(ExceptionHandlers.throwingConsumerWrapper((cli) -> {
			cli.logout();
			cli.disconnect();
		}, IOException.class));
		LOG.debug(String.format("Disconnected FTP server with host %s.", ftpUser.getHost()));
	}
	
	@Override
	public String downloadFile(String localFolderpath, String ftpFolderpath, String ftpFilename) throws Exception {
		checkAndConnect();
		//Optional.ofNullable(ftpFolderpath).filter(ExceptionHandlers.throwingPredicateWrapper(fln -> fln.equals(ftpClient.printWorkingDirectory()), IOException.class)).ifPresent(ExceptionHandlers.throwingConsumerWrapper(fln -> ftpClient.changeWorkingDirectory(ftpFolderpath), IOException.class));
		Path localFilePath = Paths.get(Optional.ofNullable(localFolderpath).orElse(".")).resolve(ftpFilename), 
				ftpFilePath = Paths.get(Optional.ofNullable(ftpFolderpath).orElse(".")).resolve(ftpFilename);
		FTPFile ftpFile = Stream.of(ftpClient.listFiles(ftpFilePath.toString())).findFirst().get();
		if (Files.notExists(localFilePath) || ftpFile.getTimestamp().getTimeInMillis() > Files.getLastModifiedTime(localFilePath).toMillis()) {
			LOG.debug(String.format("Downloading file %s from FTP setvet %s...", ftpFilename, ftpUser.getHost()));
			ftpClient.setControlKeepAliveTimeout(60);
			try (OutputStream out = Files.newOutputStream(localFilePath)) {
				ftpClient.retrieveFile(ftpFilePath.toString(), out);
			}
			Files.setLastModifiedTime(localFilePath, FileTime.fromMillis(ftpFile.getTimestamp().getTimeInMillis()));
			LOG.debug(String.format("Downloaded file %s from FTP server %s.", ftpFilename, ftpUser.getHost()));
		} else {
			LOG.debug(String.format("%s from ftp dir %s is not modified since last download.", ftpFilename, ftpFolderpath));
		}
		return localFilePath.toString();
	}
	
	@Override
	public String downloadFile(String ftpFilename) throws Exception {
		return downloadFile(ftpUser.getLocalFolderpath(), ftpUser.getFtpFolderpath(), ftpFilename);
	}
}
