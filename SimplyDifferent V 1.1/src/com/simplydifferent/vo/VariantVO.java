package com.simplydifferent.vo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simplydifferent.entity.VariantMaster;

public class VariantVO extends VariantMaster {

	private ProductVO product;
	
	public VariantVO() {
	}
	
	public VariantVO(VariantMaster superObject) {
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
	
	public VariantMaster extractSuper() {
		VariantMaster master = new VariantMaster();
		List<String> notToCons = new ArrayList<String>(Arrays.asList("getClass,getProduct"));
		Method[] methods = this.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !notToCons.contains(method.getName())) {
				try {
					Method method2 = master.getClass().getMethod(method.getName().replaceFirst("get", "set"), method.getReturnType());
					method2.invoke(master, new Object[]{method.invoke(this, new Object[]{})});
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return master;
	}

	public ProductVO getProduct() {
		return product;
	}

	public void setProduct(ProductVO product) {
		this.product = product;
	}
}
