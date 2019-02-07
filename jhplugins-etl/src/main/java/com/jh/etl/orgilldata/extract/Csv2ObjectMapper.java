package com.jh.etl.orgilldata.extract;

@FunctionalInterface
public interface Csv2ObjectMapper<T> {

	T mapData(String[] csvLine, T object);
}
