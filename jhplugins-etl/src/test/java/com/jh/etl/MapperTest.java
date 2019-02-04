package com.jh.etl;

import org.mapstruct.factory.Mappers;

public class MapperTest {

	public static void main(String[] args) {
		MapperTestDtoA a = new MapperTestDtoA();
		a.setId(1);
		a.setName("from a");
		
		MapperTestDtoB b = Mappers.getMapper(MapperTestMapper.class).toB(a);
		
		System.out.println(a.toString());
		System.out.println(b.toString());
	}
}
