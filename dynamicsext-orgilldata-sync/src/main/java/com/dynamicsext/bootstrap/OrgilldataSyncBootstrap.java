package com.dynamicsext.bootstrap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.dao.item.ItemDAO;
import com.jh.dao.website.WebsiteProductsDAO;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;

@SpringBootApplication
@ComponentScan(
		basePackages = {"com.dynamicsext"}
)
public class OrgilldataSyncBootstrap {
	
	private final Log LOG = LogFactory.getLog(OrgilldataSyncBootstrap.class);
	
	@Autowired private WebsiteProductsDAO websiteProductsDAO;
	@Autowired private ObjectMapper jacksonObjectMapper;
	@Autowired private ItemDAO itemDAO;
	
	@Value("${file.systempath.image}") private String orgillImageFilePath;
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(OrgilldataSyncBootstrap.class, args);
		ctx.getBean(OrgilldataSyncBootstrap.class).initiateSync();
	}
	
	/**
	 * 
	 */
	public void initiateSync() {
		LOG.info("Start: Initiate orgill data sync");
		try {
			websiteProductsDAO.downloadParseAndPersistWebCategories();
			List<Item> depts2Add = websiteProductsDAO.getDeptToAddAfterSync();
			RequestVO requestVO = new RequestVO();;
			if (!depts2Add.isEmpty()) {
				LOG.debug("Start: Adding new departments");
				requestVO.setItemsToModify(jacksonObjectMapper.writeValueAsString(depts2Add));
				itemDAO.addDepartments(requestVO);
				LOG.debug("End: Adding new departments");
			}
			else{
				LOG.debug("No new departments to be added...");
			}
			
			List<Item> cates2Add = websiteProductsDAO.getCategoryToAddAfterSync();
			if (!cates2Add.isEmpty()) {
				LOG.debug("Start: Adding new categories");
				requestVO.setItemsToModify(jacksonObjectMapper.writeValueAsString(cates2Add));
				itemDAO.addCategories(requestVO);
				LOG.debug("End: Adding new categories");
			}
			else{
				LOG.debug("No new categories to be added...");
			}
			
			websiteProductsDAO.downloadParseAndPersistWebFiles();
			List<Item> items2Add = websiteProductsDAO.getItemsToAddAfterSync();
			if (!items2Add.isEmpty()) {
				LOG.debug("Start: Adding new items");
				List<String> skus2Add = new ArrayList<String>();
				for (Item i : items2Add) {
					skus2Add.add(i.getSku());
				}
				requestVO.setItemsToModify(jacksonObjectMapper.writeValueAsString(skus2Add));
				itemDAO.addOrActivateItems(requestVO, orgillImageFilePath);
				LOG.debug("End: Adding new items");
			}
			else{
				LOG.debug("No new items to be added...");
			}
		} catch (Exception e) {
			LOG.error("Error occurred while syncing orgill data.", e);
		}
		LOG.info("End: Initiate orgill data sync");
	}
}
