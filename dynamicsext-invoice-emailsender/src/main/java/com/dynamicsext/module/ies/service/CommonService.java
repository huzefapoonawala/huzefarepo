package com.dynamicsext.module.ies.service;

import java.io.File;
import java.util.Map;

import com.dynamicsext.module.ies.vo.StoreVO;

public interface CommonService {

	StoreVO getStoreDetails();
	
	void populateStoreDetails(Map<String, Object> model);

	String generatePaymentReceipt(String templateFileName, Map<String, Object> model);

	void saveFile(File file, String text);
}
