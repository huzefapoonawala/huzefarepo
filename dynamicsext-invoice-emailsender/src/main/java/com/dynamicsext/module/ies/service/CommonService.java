package com.dynamicsext.module.ies.service;

import java.util.Map;

import com.dynamicsext.module.ies.vo.StoreVO;

public interface CommonService {

	StoreVO getStoreDetails();
	
	void populateStoreDetails(Map<String, Object> model);
}
