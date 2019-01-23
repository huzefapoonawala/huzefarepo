package com.jh.etl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jh.etl.common.interfaces.EtlOrchestrator;

@SpringBootApplication
public class EtlApplication {

	private final Log LOG = LogFactory.getLog(EtlApplication.class);
	
	@Autowired(required=false) private Optional<List<EtlOrchestrator>> orchestrators;
	
	public static void main(String[] args) {
		SpringApplication.run(EtlApplication.class, args).getBean(EtlApplication.class).initiateEtlOrchestration();
	}
	
	public void initiateEtlOrchestration(){
		LOG.debug("Start: Initiating orchestration");
		
		orchestrators.ifPresent((orchs) -> orchs.forEach(orch -> orch.initiateEtl()));
		
		LOG.debug("End: Initiating orchestration");
	}

}