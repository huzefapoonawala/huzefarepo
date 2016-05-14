package com.dynamicsext.ig.test;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import com.dynamicsext.ig.util.LicenseValidator;

public class LicenseTest {

	public static void main(String[] args)throws Exception
    {
		System.out.println(LicenseValidator.validateLicense());
		String KEY_DELIMITER="yektneilc";
		
		String clientLicense = "2821ea24a1b7cd1686fabdff384cda43964e7a25497a7224d38dc77bfe430e64yektneilc014sh45g8w", encryptionKey;
		
		clientLicense = clientLicense.replaceFirst(KEY_DELIMITER, " ");
		encryptionKey = clientLicense.substring(clientLicense.indexOf(" ")+1);
		clientLicense = clientLicense.substring(0, clientLicense.indexOf(" "));
		
		System.out.println(encryptionKey);
		System.out.println(clientLicense);
		
		byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();			
		StringBuilder sb = new StringBuilder();
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
		System.out.println("Hex format : " + sb.toString());
		System.out.println(StringUtils.equals(clientLicense, sb.toString()));
    }
}
