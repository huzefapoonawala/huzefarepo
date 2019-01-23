package com.jh.etl.orgilldata.extract;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jh.etl.common.ftputil.FTPReader;
import com.jh.etl.common.interfaces.DataExtracter;

@Component
public class OrgillDataExtracter implements DataExtracter<List<String>> {

	private final Log LOG = LogFactory.getLog(OrgillDataExtracter.class);
	
	@Autowired private FTPReader orgillProductDataFTPReader;
	
	@Override
	public List<String> extractData() {
		LOG.debug("Start: Dowloading and extracting orgill data");
		orgillProductDataFTPReader.downloadFile("");
		LOG.debug("End: Dowloading and extracting orgill data");
		return null;
	}

}
