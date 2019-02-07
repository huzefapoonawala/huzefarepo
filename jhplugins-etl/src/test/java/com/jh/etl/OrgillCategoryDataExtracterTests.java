package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.common.interfaces.DataExtracter;

public class OrgillCategoryDataExtracterTests extends TestSetupWithMockingFtpReader {
	
	@Autowired private DataExtracter<List<CategoryDto>> orgillCategoryDataExtracter;
	
	@Test
	public void testTransformData() throws Exception {
		List<CategoryDto> list = orgillCategoryDataExtracter.extractData();
		int lineCount = 0;
		try(Stream<String> stream = Files.lines(Paths.get(CATEGORY_FILENAME))) {
			lineCount = Long.valueOf(stream.map(line -> line.split("~")).filter(strs -> strs.length >= 13 && strs[3].equalsIgnoreCase("000")).count()).intValue();
		}
		assertThat(list, IsCollectionWithSize.hasSize(lineCount));
		assertThat(list, IsCollectionContaining.hasItem(new CategoryDto("HANDSAWS","1010110120",Supplier.ORGILL,CategoryDto.builder().code("1010100000").supplier(Supplier.ORGILL).build())));
	}
}
