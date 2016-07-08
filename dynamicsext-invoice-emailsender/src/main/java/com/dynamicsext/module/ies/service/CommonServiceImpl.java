package com.dynamicsext.module.ies.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.dynamicsext.module.ies.vo.StoreVO;

@Component
public class CommonServiceImpl implements CommonService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Value("${store.logo.image}") private String storeLogoImg;
	@Value("${store.website.url}") private String storeWebsiteUrl;
	
	public StoreVO getStoreDetails() {
		StoreVO storeVO = jdbcTemplate.queryForObject("select top 1 [StoreName], StoreAddress1, StoreAddress2, StoreCity, StoreCountry, StoreEmail, StoreFax, StoreState, StoreZip, StoreEmail, StorePhone from Configuration;", new BeanPropertyRowMapper<StoreVO>(StoreVO.class));
		return storeVO;
	}
	
	public void populateStoreDetails(Map<String, Object> model) {
		StoreVO store = getStoreDetails();
		File logoImg = null;
		if (StringUtils.isNotBlank(storeLogoImg) && (logoImg = new File(storeLogoImg)).exists()) {
			try {
				FileInputStream in = new FileInputStream(logoImg);
				byte[] bs = new byte[in.available()];
				in.read(bs);
				in.close();
				model.put("storeLogoImg", "data:image/png;base64, "+Base64Utils.encodeToString(bs));
			} catch (Exception e) {
				LOG.warn(String.format("Error occurred while reading logo image with path '%s'.", storeLogoImg));
				model.put("storeLogoImg", "");
			}
		}
		else{
			model.put("storeLogoImg", storeLogoImg);	
		}
		store.setStoreWebsite(storeWebsiteUrl);
		model.put("storeAddress", store.getStoreDetails("<br>"));
		model.put("storeLogoText", StringUtils.join("<h3>",store.getStoreName(),"</h3>"));
	}
}
