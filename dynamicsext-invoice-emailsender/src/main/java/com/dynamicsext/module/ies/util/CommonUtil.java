package com.dynamicsext.module.ies.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	public static String convertAmountInHtmlFormat(Double amount) {
		String prefix = amount < 0 ? "(" : "", suffix = amount < 0 ? ")" : "";
		return new StringBuilder().append(prefix).append(Defaults.CURRENCY_SYMBOL).append(Defaults.MONEY_DISPLAY_FORMAT.format(Math.abs(amount))).append(suffix).toString();
	}
	
	public static String convertAmountInHtmlFormat(Double amount, Double defaultValue) {
		return convertAmountInHtmlFormat(amount == null ? defaultValue : amount);
	}
	
	public static String convertDateInHtmlFormat(Date date) {
		return new SimpleDateFormat(Defaults.DATE_DISPLAY_FORMAT).format(date);
	}
}
