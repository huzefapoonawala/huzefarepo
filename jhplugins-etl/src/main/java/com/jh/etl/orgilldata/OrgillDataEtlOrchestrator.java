package com.jh.etl.orgilldata;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.common.interfaces.DataExtracter;
import com.jh.etl.common.interfaces.EtlOrchestrator;
import com.jh.etl.datastore.service.CategoryService;

@Component
public class OrgillDataEtlOrchestrator implements EtlOrchestrator {

	private static final Log LOG = LogFactory.getLog(OrgillDataEtlOrchestrator.class); 
	
	@Autowired private DataExtracter<List<CategoryDto>> orgillCategoryDataExtracter; 
	@Autowired private DataExtracter<List<ProductDto>> orgillProductDataExtracter;
	@Autowired private CategoryService categoryService;
	
	@Override
	public void initiateEtl() {
		LOG.debug("Start: Initiating orgill data etl");
		
		categoryService.persistCategories(orgillCategoryDataExtracter.extractData());
		orgillProductDataExtracter.extractData();
		
		LOG.debug("End: Initiating orgill data etl");
	}

}