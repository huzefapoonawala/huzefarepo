package com.dynamicsext.ig.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberFormatter {

	private static NumberFormatter this_;
	
	private NumberFormat nf;

	private NumberFormatter() {
		nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);		
	}
	
	public static NumberFormatter getInstance() {
		if (this_ == null) {
			this_ = new NumberFormatter();
		}
		return this_;
	}

	public NumberFormat getNumberFormat() {
		return nf;
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
