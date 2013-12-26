package com.simplydifferent.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

	public static String convertDate2String(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static Double roundOf(double r, int decimalPlace){
	    BigDecimal bd = new BigDecimal(r);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String roundOfAndConvert2String(double r, int decimalPlace){
	    String d = roundOf(r, decimalPlace).toString();
	    int dsize = d.length()-(d.indexOf(".")+1);
		dsize = decimalPlace-dsize;
		for (int i = 0; i < dsize; i++) {
			d += "0";
		}
	    return d;
	}
	
	public static String roundOf2ProperDecimalAndConvert2String(double r, int decimalPlace){
	    String d = roundOf(r, decimalPlace).toString();
	    int deciidx = d.indexOf(".")+1;
		if (Integer.valueOf(d.substring(deciidx,d.length())) == 0) {
			d = d.substring(0,deciidx-1);
		}
	    return d;
	}
	
	public static void main(String[] args) {
		System.out.println(roundOf2ProperDecimalAndConvert2String(120339.35, 2));
	}
}
