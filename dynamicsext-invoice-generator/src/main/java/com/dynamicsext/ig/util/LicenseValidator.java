package com.dynamicsext.ig.util;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

public class LicenseValidator {

	private static final String KEY_DELIMITER="yektneilc";
	private static final String LICENSE_FILE_PATH = "./configs/ig-license.dat";
	
	public static boolean validateLicense() {
		boolean isLicenseValid = false;
		try {
			FileInputStream fis = new FileInputStream(LICENSE_FILE_PATH);
			StringBuilder sb = new StringBuilder();
			int ch;
			while ((ch = fis.read()) != -1) {
				sb.append((char)ch);
			}
			fis.close();
			
			String clientLicense = sb.toString();
			clientLicense = clientLicense.replaceFirst(KEY_DELIMITER, " ");
			String encryptionKey = clientLicense.substring(clientLicense.indexOf(" ")+1);
			clientLicense = clientLicense.substring(0, clientLicense.indexOf(" "));
			
			byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();			
			sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], ""));		
			}
			sb.append(encryptionKey);
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(sb.toString().getBytes());
			byte byteData[] = md.digest();
			sb = new StringBuilder();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			isLicenseValid = StringUtils.equals(clientLicense, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isLicenseValid;
	}
}
