package com.jh.etl.orgilldata;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jh.etl.common.interfaces.DataExtracter;
import com.jh.etl.common.interfaces.EtlOrchestrator;

@Component
public class OrgillDataEtlOrchestrator implements EtlOrchestrator {

	private final Log LOG = LogFactory.getLog(OrgillDataEtlOrchestrator.class); 
	
	@Autowired private DataExtracter<List<String>> orgillDataExtracter; 
	
	@Override
	public void initiateEtl() {
		LOG.debug("Start: Initiating orgill data etl");
		
		orgillDataExtracter.extractData();
		
		LOG.debug("End: Initiating orgill data etl");
	}

}