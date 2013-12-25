package com.jh.tc;

import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.jh.util.DateUtil;

public class DateTypeConverter extends StrutsTypeConverter {

	private final String format2 = "yyyy-MM-dd HH:mm:ss";
	private final String format1 = "yyyy-MM-dd";	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map map, String[] values, Class toClass) {
		Date date = null;
		try {
			date = DateUtil.convertString2Date(values[0], format2);
		} catch (Exception e) {
			try {
				date = DateUtil.convertString2Date(values[0], format1);
			} catch (Exception e1) {
			}
		}
		return date;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map map, Object date) {
		// TODO Auto-generated method stub
		return DateUtil.convertDate2String((Date)date, format2);
	}
}
