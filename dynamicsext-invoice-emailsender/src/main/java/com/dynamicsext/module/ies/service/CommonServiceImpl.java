package com.dynamicsext.module.ies.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Base64Utils;

import com.dynamicsext.module.ies.vo.StoreVO;

@Component
public class CommonServiceImpl implements CommonService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private VelocityEngine engine;
	
	@Value("${store.logo.image}") private String storeLogoImg;
	@Value("${store.website.url}") private String storeWebsiteUrl;
	
	public StoreVO getStoreDetails() {
		StoreVO storeVO = jdbcTemplate.queryForObject("select top 1 [StoreName], StoreAddress1, StoreAddress2, StoreCity, StoreCountry, StoreEmail, StoreFax, StoreState, StoreZip, StoreEmail, StorePhone from Configuration;", new BeanPropertyRowMapper<StoreVO>(StoreVO.class));
		return storeVO;
	}
	
	@Override
	public void populateStoreDetails(Map<String, Object> model) {
		populateStoreDetails(model, true);
	}
	
	@Override
	public void populateStoreDetails(Map<String, Object> model, boolean isHtml){
		StoreVO store = getStoreDetails();
		File logoImg = null;
		if (StringUtils.isNotBlank(storeLogoImg) && (logoImg = new File(storeLogoImg)).exists()) {
			try {
				FileInputStream in = new FileInputStream(logoImg);
				byte[] bs = new byte[in.available()];
				in.read(bs);
				in.close();
				if (isHtml) {
					model.put("storeLogoImg", "data:image/png;base64, "+Base64Utils.encodeToString(bs));
				} else {
					model.put("storeLogoImg", storeLogoImg);
				}
			} catch (Exception e) {
				LOG.warn(String.format("Error occurred while reading logo image with path '%s'.", storeLogoImg));
				model.put("storeLogoImg", "");
			}
		}
		else{
			model.put("storeLogoImg", storeLogoImg);	
		}
		store.setStoreWebsite(storeWebsiteUrl);
		model.put("storeAddress", store.getStoreDetails(isHtml ? "<br>" : "\n"));
		model.put("storeLogoText", StringUtils.join(isHtml ? "<h3>" : "",store.getStoreName(), isHtml ? "</h3>" : ""));
	}
	
	@Override
	public String generatePaymentReceipt(String templateFileName, Map<String, Object> model){
		String text = VelocityEngineUtils.mergeTemplateIntoString(this.engine, templateFileName, "UTF-8", model);
		return text;
	}
	
	@Override
	public void saveFile(File file, String text) {
		try {
			LOG.debug(String.format("File '%s' stored at location '%s' for preview", file.getName(), file.getParent()));
			FileOutputStream out = new FileOutputStream(file);
			out.write(text.getBytes());
			out.close();
		} catch (Exception e) {
			LOG.error(String.format("Error occurred while saving file '%s' at location '%s'.", file.getName(), file.getParent()), e);
		}
	}
}
