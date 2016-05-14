package com.dynamicsext.ig.licensegenerator;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

import javax.crypto.KeyGenerator;

public class LicenseGenerator {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
		String KEY_DELIMITER="yektneilc";
		
		KeyGenerator generator = KeyGenerator.getInstance("AES");
	    generator.init(128);
	    Key encryptionKey = generator.generateKey();
	    String key = Utils.toHex(encryptionKey.getEncoded());
	     
	    System.out.print("Enter client MAC address: ");
	    Scanner console = new Scanner(System.in);
	    String clientMACAddr = console.nextLine().trim();
	    clientMACAddr = clientMACAddr.replaceAll("-", "");
	    console.close();
	    StringBuilder sb = new StringBuilder().append(clientMACAddr).append(key);
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(sb.toString().getBytes());
		byte byteData[] = md.digest();
		
		sb = new StringBuilder().append(Utils.toHex(byteData)).append(KEY_DELIMITER).append(key);
	    System.out.println("Client license: "+sb.toString());
	}
}


class Utils{
	private static String digits = "0123456789abcdef";

	/**
	 * Return length many bytes of the passed in byte array as a hex string.
	 * 
	 * @param data the bytes to be converted.
	 * @param length the number of bytes in the data block to be converted.
	 * @return a hex representation of length bytes of data.
	 */
	public static String toHex(byte[] data, int length){
		StringBuffer  buf = new StringBuffer();

		for (int i = 0; i != length; i++)
		{
			int v = data[i] & 0xff;

			buf.append(digits.charAt(v >> 4));
			buf.append(digits.charAt(v & 0xf));
		}

		return buf.toString();
	}

	/**
	 * Return the passed in byte array as a hex string.
	 * 
	 * @param data the bytes to be converted.
	 * @return a hex representation of data.
	 */
	public static String toHex(byte[] data){
		return toHex(data, data.length);
	}
}