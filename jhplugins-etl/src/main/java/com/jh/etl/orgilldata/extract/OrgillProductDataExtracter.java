package com.jh.etl.orgilldata.extract;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.common.ftputil.FTPReader;
import com.jh.etl.common.interfaces.DataExtracter;

@Component
public class OrgillProductDataExtracter implements DataExtracter<List<ProductDto>> {

	private static final Log LOG = LogFactory.getLog(OrgillProductDataExtracter.class);
	
	private static final String WEB_DEPT_SKU_FILE = "WEB_DEPT_SKU.TXT";
	private static final String WEB_SKU_COMMON_FILE = "WEB_SKU_COMMON.TXT";
	
	@Autowired private FTPReader orgillProductDataFTPReader;
	
	@Override
	public List<ProductDto> extractData() {
		List<ProductDto> list = null;
		LOG.debug("Dowloading and extracting orgill product data");
		try {
			Map<String, ProductDto> map = extractProductCategoryMappingData(orgillProductDataFTPReader.downloadFile(WEB_DEPT_SKU_FILE));
			extractProductDetailsData(orgillProductDataFTPReader.downloadFile(WEB_SKU_COMMON_FILE), map);
			list = Optional.ofNullable(map).map(m -> new ArrayList<ProductDto>(m.values())).orElse(new ArrayList<ProductDto>());
		} catch (Exception e) {
			LOG.error("Error occurred while extracting orgill product data.", e);
		}
		LOG.debug("Dowloaded and extracted orgill product data");
		return list;
	}
	
	private Map<String, ProductDto> extractProductCategoryMappingData(String localDataFilePath) throws Exception {
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			return stream.map(line -> line.split("~"))
			.filter(strs -> strs.length >= 7)
			.collect(Collectors.toMap(strs -> strs[6], strs -> ProductDto.builder()
								.sku(strs[6])
								.supplier(Supplier.ORGILL)
								.category(CategoryDto.builder().code(strs[0].concat(strs[1]).concat(strs[2])).build())
								.build()));
		}
	}
	
	private void extractProductDetailsData(String localDataFilePath, Map<String, ProductDto> products) throws Exception {
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			stream.map(line -> line.split("~"))
					.forEach(strs -> {
						Optional.ofNullable(products.get(strs[0])).ifPresent(p -> {
							p.setTitle(strs[9].trim());
							p.setDescription(strs[2].trim());
							p.setWidth(Float.valueOf(strs[3]));
							p.setHeight(Float.valueOf(strs[4]));
							p.setLength(Float.valueOf(strs[5]));
							p.setWeight(Float.valueOf(strs[6]));
							p.setUpc(strs[16].trim());
							p.setRetailPrice(Double.valueOf(strs[11])/100);
							p.setRetailUnit(strs[12].trim());
							p.setImageLink(strs[14].trim());
						});
					});
		}
	}
	
	/*private List<ProductDto> extractProductCategoryMappingDataAsList(String localDataFilePath) throws Exception{
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			List<ProductDto> list = stream.map(line -> line.split("~"))
			.filter(strs -> strs.length >= 7)
			.map(
					strs -> ProductDto.builder()
								.sku(strs[6])
								.category(CategoryDto.builder().code(strs[0].concat(strs[1]).concat(strs[2])).build())
								.build()
			)
			.collect(Collectors.toList());
			return list;
		}
	}
	
	private void extractProductDetailsDataAsList(String localDataFilePath, List<ProductDto> products) throws Exception{
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			stream.map(line -> line.split("~"))
					.forEach(strs -> {
						Optional<ProductDto> product = products.stream().filter(pro -> pro.getSku().equalsIgnoreCase(strs[0])).findFirst();
						product.ifPresent(p -> {
							p.setTitle(strs[9].trim());
							p.setDescription(strs[2].trim());
							p.setWidth(Float.valueOf(strs[3]));
							p.setHeight(Float.valueOf(strs[4]));
							p.setLength(Float.valueOf(strs[5]));
							p.setWeight(Float.valueOf(strs[6]));
							p.setUpc(strs[16].trim());
							p.setRetailPrice(Double.valueOf(strs[11])/100);
							p.setRetailUnit(strs[12].trim());
							p.setImageLink(strs[14].trim());
						});
					});
		}
	}*/
}
