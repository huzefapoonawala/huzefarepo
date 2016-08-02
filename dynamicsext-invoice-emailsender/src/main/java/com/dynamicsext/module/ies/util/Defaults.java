package com.dynamicsext.module.ies.util;

import java.text.DecimalFormat;

public interface Defaults {

	DecimalFormat MONEY_DISPLAY_FORMAT = new DecimalFormat("###,###,##0.00");
	String CURRENCY_SYMBOL = "$";
	String INVOICE_FILE_EXTENSION = ".html";
	String DATE_DISPLAY_FORMAT ="MM/dd/yy";
	String PDF_FILE_EXTENSION = ".pdf";
}
