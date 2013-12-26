package com.simplydifferent.vo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simplydifferent.entity.ProductMaster;

public class ProductVO extends ProductMaster {

	public ProductVO() {
	}
	
	public ProductVO(ProductMaster superObject) {
		List<String> notToCons = new ArrayList<String>();
		Method[] methods = superObject.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !notToCons.contains(method.getName())) {
				try {
					Method method2 = super.getClass().getMethod(method.getName().replaceFirst("get", "set"), method.getReturnType());
					method2.invoke(this, new Object[]{method.invoke(superObject, new Object[]{})});
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public ProductMaster extractSuper() {
		ProductMaster productMaster = new ProductMaster();
		List<String> notToCons = new ArrayList<String>(Arrays.asList("getClass"));
		Method[] methods = this.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !notToCons.contains(method.getName())) {
				try {
					Method method2 = productMaster.getClass().getMethod(method.getName().replaceFirst("get", "set"), method.getReturnType());
					method2.invoke(productMaster, new Object[]{method.invoke(this, new Object[]{})});
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return productMaster;
	}
}
