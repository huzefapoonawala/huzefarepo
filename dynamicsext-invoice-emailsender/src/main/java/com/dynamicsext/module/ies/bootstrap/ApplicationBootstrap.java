package com.dynamicsext.module.ies.bootstrap;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dynamicsext.module.ies.service.InvoiceSenderService;
import com.dynamicsext.module.ies.service.OrderGeneratorService;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.dynamicsext"})
public class ApplicationBootstrap{
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationBootstrap.class);
	private static final String FIRSTRUN_TIME_DELTA_PROPERTY_KEY = "firstrun.time.delta";
	private static final String SET_PROFILE = "profile";
	private static final String ORDER_GENERATOR_PROFILE = "ordergenerator";
	private static final String ORDER_IDS = "order.ids";
	
	@Autowired private InvoiceSenderService invoiceSenderService;
	@Autowired private OrderGeneratorService orderGeneratorService;
	
    public static void main( String[] args ){
    	String profile = System.getProperty(SET_PROFILE);
        ConfigurableApplicationContext ctx = SpringApplication.run(ApplicationBootstrap.class, args);
        ApplicationBootstrap this_ = ctx.getBean(ApplicationBootstrap.class);
        if (StringUtils.equalsIgnoreCase(ORDER_GENERATOR_PROFILE, profile)) {
        	this_.initOrderGenerator();
		}
        else{
        	this_.init();
        }
        
    }
    
    private void init() {
		LOG.debug("Start: Fetch & send invoices");
		try {
			File lastRunOn = new File("./configs", "lastrunon.dat");
			if (!lastRunOn.exists()) {
				lastRunOn.createNewFile();
				lastRunOn.setLastModified(new Date().getTime() - Long.valueOf(StringUtils.defaultIfBlank(System.getProperty(FIRSTRUN_TIME_DELTA_PROPERTY_KEY), "0"))*60*1000);
			}
			Date lastRunDate = new Date(lastRunOn.lastModified()), currentDate = new Date();
			LOG.debug(String.format("Process previously run on %s", lastRunDate));
			invoiceSenderService.fetchInvoiceAndSendEmails();
			lastRunOn.setLastModified(currentDate.getTime());
		} catch (Exception e) {
			LOG.error("Error occurred while sending emails.", e);
		}
		LOG.debug("End: Fetch & send invoices");
	}
    
    private void initOrderGenerator() {
    	if (StringUtils.isNotBlank(System.getProperty(ORDER_IDS))) {
    		for (String orderId : System.getProperty(ORDER_IDS).split(",")) {
    			try {
            		orderGeneratorService.generateOrder(Long.valueOf(orderId));
    			} catch (Exception e) {
    				LOG.error(String.format("Error occurred while generating order with id '%s'.", orderId),e);
    			}
			}
		}
    	else{
    		LOG.info("No order/s found to be generated.");
    	}
	}
}
