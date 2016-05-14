package com.dynamicsext.module.ies.util;

public class CommonUtil {

	public static String convertAmountInHtmlFormat(Double amount) {
		String prefix = amount < 0 ? "(" : "", suffix = amount < 0 ? ")" : "";
		return new StringBuilder().append(prefix).append(Defaults.CURRENCY_SYMBOL).append(Defaults.MONEY_DISPLAY_FORMAT.format(Math.abs(amount))).append(suffix).toString();
	}
}
