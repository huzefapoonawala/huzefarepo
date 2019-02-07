package com.jh.etl.orgilldata.extract;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static final String WEB_SKU_DESCRIPTION_FILE = "orgill_skudesc.TXT";
	private static final String WEB_SKU_ADDITIONAL_DESCRIPTION_FILE = "skudescadd.TXT";
	
	@Autowired private FTPReader orgillProductDataFTPReader;
	
	@Override
	public List<ProductDto> extractData() {
		List<ProductDto> list = null;
		LOG.debug("Dowloading and extracting orgill product data");
		try {
			Map<String, ProductDto> map = extractProductCategoryMappingData(orgillProductDataFTPReader.downloadFile(WEB_DEPT_SKU_FILE), mapperForProductCategoryMapping());
			extractProductDetailsData(orgillProductDataFTPReader.downloadFile(WEB_SKU_COMMON_FILE), map, mapper2MapProductDetails());
			extractProductDetailsData(orgillProductDataFTPReader.downloadFile(WEB_SKU_DESCRIPTION_FILE), map, mapper2MapProductDescription());
			extractProductDetailsData(orgillProductDataFTPReader.downloadFile(WEB_SKU_ADDITIONAL_DESCRIPTION_FILE), map, mapper2MapAdditionalProductDescription());
			list = Optional.ofNullable(map).map(m -> new ArrayList<ProductDto>(m.values())).orElse(new ArrayList<ProductDto>());
		} catch (Exception e) {
			LOG.error("Error occurred while extracting orgill product data.", e);
		}
		LOG.debug("Dowloaded and extracted orgill product data");
		return list;
	}
	
	private Csv2ObjectMapper<ProductDto> mapperForProductCategoryMapping() {
		return (csvLine, p) -> ProductDto.builder()
				.sku(csvLine[6])
				.supplier(Supplier.ORGILL)
				.category(CategoryDto.builder().code(csvLine[0].concat(csvLine[1]).concat(csvLine[2])).supplier(Supplier.ORGILL).build())
				.build();
	}
	
	private Csv2ObjectMapper<ProductDto> mapper2MapProductDetails() {
		return (csvLine, p) -> {
			p.setTitle(csvLine[9].trim());
			p.setDescription(csvLine[2].trim());
			p.setWidth(Float.valueOf(csvLine[3]));
			p.setHeight(Float.valueOf(csvLine[4]));
			p.setLength(Float.valueOf(csvLine[5]));
			p.setWeight(Float.valueOf(csvLine[6]));
			p.setUpc(csvLine[16].trim());
			p.setRetailPrice(Double.valueOf(csvLine[11])/100);
			p.setRetailUnit(csvLine[12].trim());
			p.setImageLink(csvLine[14].trim());
			return p;
		};
	}
	
	private Csv2ObjectMapper<ProductDto> mapper2MapProductDescription() {
		return (csvLine, p) -> {
			p.setDescription(
//					Optional.ofNullable(p.getDescription()).orElse("").concat(String.join("", Arrays.copyOfRange(csvLine, 1, csvLine.length)))
					String.join("", Arrays.copyOfRange(csvLine, 1, csvLine.length))
			);
			return p;
		};
	}
	
	private Csv2ObjectMapper<ProductDto> mapper2MapAdditionalProductDescription() {
		return (csvLine, p) -> {
			p.setDescription(
					Optional.ofNullable(p.getDescription()).orElse("").concat(String.join("", Arrays.copyOfRange(csvLine, 2, csvLine.length)))
			);
			return p;
		};
	}
	
	private Map<String, ProductDto> extractProductCategoryMappingData(String localDataFilePath, Csv2ObjectMapper<ProductDto> mapper) throws Exception {
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath))) {
			return stream.map(line -> line.split("~"))
			.filter(strs -> strs.length >= 7)
			.collect(Collectors.toMap(strs -> strs[6], strs -> mapper.mapData(strs, null)));
		}
	}
	
	private void extractProductDetailsData(String localDataFilePath, Map<String, ProductDto> products, Csv2ObjectMapper<ProductDto> mapper) throws Exception {
		try (Stream<String> stream = Files.lines(Paths.get(localDataFilePath), Charset.forName("ISO-8859-1"))) {
			stream.map(line -> line.split("~")).forEach(strs -> Optional.ofNullable(products.get(strs[0])).ifPresent(p -> mapper.mapData(strs, p)));
		}
	}
}
