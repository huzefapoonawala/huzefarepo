package com.jh.etl;

import org.mapstruct.Mapper;

@Mapper
public interface MapperTestMapper {

	MapperTestDtoA toA (MapperTestDtoB b);
	
	MapperTestDtoB toB (MapperTestDtoA a);
}
