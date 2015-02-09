package com.dynamicsext.bdu.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dynamicsext.bdu.processor.DataProcessor;

public class UploaderBootstrap {
	
	private static final String APP_CONTEXT_PATH = "bdu-context.xml";
	
	private static final Logger LOG = LoggerFactory.getLogger(UploaderBootstrap.class);
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		LOG.info("Bulk data uploader started");
		try {
			AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(APP_CONTEXT_PATH);
			ctx.start();
			ctx.getBean(DataProcessor.class).readAndProcessData2Upload();
			ctx.stop();
		} catch (Exception e) {
			LOG.error("Error occurred in bootstrap.",e);
		}
		LOG.info("Bulk data uploader ended");
	}
}
