package com.dynamicsext.bdu.util;

import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component(value="inputReader")
public class InputReader {

	private Scanner console = new Scanner(System.in);
	
	public String getSupplierFile2Upload() {
		System.out.println("Please enter file name (in CSV format) consisting supplier list to upload : ");
		return console.nextLine().trim();
	}
	
	public String getProductFile2Upload() {
		System.out.println("Please enter file name (in CSV format) consisting product list to upload : ");
		return console.nextLine().trim();
	}
}
