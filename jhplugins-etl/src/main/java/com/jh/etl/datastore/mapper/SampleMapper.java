package com.jh.etl.datastore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.datastore.entity.Category;

@Mapper
public interface SampleMapper {

	static SampleMapper INSTANCE = Mappers.getMapper(SampleMapper.class);
	
	Category toCategory(CategoryDto categoryDto);
}
