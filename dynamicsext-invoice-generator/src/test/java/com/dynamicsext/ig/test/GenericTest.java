package com.dynamicsext.ig.test;

import java.text.ParseException;

import com.dynamicsext.ig.util.NumberFormatter;

public class GenericTest {

	public static void main(String[] args) throws ParseException {
		System.out.println((NumberFormatter.getInstance().round(23.4567, 2) - 23.46) < 0);
	}
}
