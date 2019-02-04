package com.jh.etl.orgilldata.extract;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.common.ftputil.FTPReader;
import com.jh.etl.common.interfaces.DataExtracter;

@Component
public class OrgillCategoryDataExtracter implements DataExtracter<List<CategoryDto>> {

	private final Log LOG = LogFactory.getLog(OrgillCategoryDataExtracter.class);
	
	@Autowired private FTPReader orgillProductDataFTPReader;
	
	private static final String WEB_DEPT_FILENAME = "WEB_DEPT.TXT";
	
	@Override
	public List<CategoryDto> extractData() {
		LOG.debug("Dowloading and extracting orgill category data");
		List<CategoryDto> categories = null;
		try {
			String localDataFilePath = orgillProductDataFTPReader.downloadFile(WEB_DEPT_FILENAME);
			categories = transformData(localDataFilePath);
		} catch (Exception e) {
			LOG.error("Error occurred while extracting orgill category data.", e);
		}
		LOG.debug("Dowloaded and extracted orgill category data");
		return categories;
	}
	
	private List<CategoryDto> transformData(String localDataFilePath) throws Exception{
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			List<CategoryDto> list = stream.map(line -> line.split("~"))
			.filter(strs -> strs.length >= 13 && strs[3].equalsIgnoreCase("000"))
			.map(
					strs -> new CategoryDto(
							strs[12].trim(), 
							strs[0].concat(strs[1]).concat(strs[2]), 
							Supplier.ORGILL, 
							CategoryDto.builder()
								.supplier(Supplier.ORGILL)
								.code(strs[6].concat(strs[7]).concat(strs[8]))
								.build()
							)
			)
			.collect(Collectors.toList());
			return list;
		}
	}

}
