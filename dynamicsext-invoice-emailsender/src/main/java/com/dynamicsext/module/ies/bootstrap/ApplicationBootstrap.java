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

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.dynamicsext"})
public class ApplicationBootstrap{
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationBootstrap.class);
	private static final String FIRSTRUN_TIME_DELTA_PROPERTY_KEY = "firstrun.time.delta";
	
	@Autowired private InvoiceSenderService invoiceSenderService;
	
    public static void main( String[] args ){
        ConfigurableApplicationContext ctx = SpringApplication.run(ApplicationBootstrap.class, args);
        ApplicationBootstrap this_ = ctx.getBean(ApplicationBootstrap.class);
        this_.init();
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
}
