package com.simplydifferent.util;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtil {

	public static void copyPropertyValue(Object source, Object destination, String propertyName) throws Exception {
		PropertyUtils.setProperty(destination, propertyName, PropertyUtils.getProperty(source, propertyName));
	}
}
