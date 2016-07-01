package com.dynamicsext.module.ies.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dynamicsext.module.ies.vo.StoreVO;

@Component
public class CommonServiceImpl implements CommonService {

	@Autowired private JdbcTemplate jdbcTemplate;
	
	public StoreVO getStoreDetails() {
		StoreVO storeVO = jdbcTemplate.queryForObject("select [StoreName], StoreAddress1, StoreAddress2, StoreCity, StoreCountry, StoreEmail, StoreFax, StoreState, StoreZip, StoreEmail, StorePhone, '' as storeLogo, '' as storeWebsite from Configuration;", new BeanPropertyRowMapper<StoreVO>(StoreVO.class));
		return storeVO;
	}
	
	public void populateStoreDetails(Map<String, Object> model) {
		StoreVO store = getStoreDetails();
		model.put("storeLogoImg", store.getStoreLogo());
		model.put("storeAddress", store.getStoreDetails("<br>"));
		model.put("storeLogoText", StringUtils.join("<h3>",store.getStoreName(),"</h3>"));
	}
}
