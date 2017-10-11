package com.dynamicsext.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ImportResource(locations={"classpath:application-context.xml"})
public class AppConfigs {
	
	@Bean
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
}
