package com.simplydifferent.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {

	public static String convertDate2String(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static Date convertString2Date(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}
	
	public static Date add2Date(Date date, int value, int type) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(type, calendar.get(type)+value);
		return calendar.getTime();
	}
	
	public static Date addMillis2Date(Date date, long millis) {
		return new Date(date.getTime()+millis);
	}
	
	public static String calculateTotal(long totalTime, String format) {
		long hours = (long) (totalTime) / (1000 * 60 * 60);
		long min = (long) ((totalTime) % (1000 * 60 * 60)) / (1000 * 60);
		long sec = (long) (((totalTime) / (1000)) % 60);
		String totalDuration = format;
		if (format.toLowerCase().contains("dd")) {
			totalDuration = totalDuration.replaceFirst("dd", (hours/24 < 10 ? "0" : "")+(hours/24));
			hours %= 24;
		}
		if(format.toLowerCase().contains("hh")){
			totalDuration = totalDuration.replaceFirst("hh", (hours < 10 ? "0" : "")+hours);
		}
		
		if (format.toLowerCase().contains("mm")) {
			totalDuration = totalDuration.replaceAll("mm", (min < 10 ? "0" : "")+min);
		}
		
		if (format.toLowerCase().contains("ss")) {
			totalDuration = totalDuration.replaceAll("ss", (sec < 10 ? "0" : "")+sec);
		}
		
		return totalDuration;
	}
	
	public static List<String> getDateRange() {
		List<String> list = new ArrayList<String>();
		Calendar calendar = new GregorianCalendar();
		Calendar cDate = new GregorianCalendar();
		
		calendar.set(Calendar.MILLISECOND, 0);
		cDate.set(Calendar.DAY_OF_MONTH, 1);
		cDate.set(Calendar.HOUR_OF_DAY, 0);
		cDate.set(Calendar.MINUTE, 0);
		cDate.set(Calendar.SECOND, 0);
		cDate.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MONTH,Calendar.MONTH-2);
		
		while (true) {
			
			if (cDate.getTime().compareTo(calendar.getTime()) <= 0) {
				break;
			}
			list.add(convertDate2String(calendar.getTime(), "MMM-yyyy"));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
		}
		return list;
	}
}
