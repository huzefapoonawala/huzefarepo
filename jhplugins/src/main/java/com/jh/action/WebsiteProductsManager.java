package com.jh.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.jh.dao.website.WebsiteProductsDAO;
import com.jh.vo.RequestVO;
import com.jh.vo.WebsiteOrder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class WebsiteProductsManager extends ActionSupport implements ModelDriven<RequestVO> {

	private static final long serialVersionUID = -3318104596660779775L;
	private static Logger logger = Logger.getLogger(WebsiteProductsManager.class);
	
	private RequestVO request;
	public RequestVO getRequest() {
		return request;
	}
	public void setRequest(RequestVO request) {
		this.request = request;
	}
	@Override
	public RequestVO getModel() {
		request = new RequestVO();
		return request;
	}
	
	private List<com.jh.vo.Item> products;
	public List<com.jh.vo.Item> getProducts() {
		return products;
	}
	
	private List<WebsiteOrder> orders;	
	public List<WebsiteOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<WebsiteOrder> orders) {
		this.orders = orders;
	}

	private WebsiteProductsDAO websiteProductsDAO;
	public void setWebsiteProductsDAO(WebsiteProductsDAO websiteProductsDAO) {
		this.websiteProductsDAO = websiteProductsDAO;
	}
	
	private String ordersToProcessFrom;	
	public String getOrdersToProcessFrom() {
		return ordersToProcessFrom;
	}
	public void setOrdersToProcessFrom(String ordersToProcessFrom) {
		this.ordersToProcessFrom = ordersToProcessFrom;
	}
	
	private String ordersToProcess;	
	public String getOrdersToProcess() {
		return ordersToProcess;
	}
	public void setOrdersToProcess(String ordersToProcess) {
		this.ordersToProcess = ordersToProcess;
	}
	
	public String fetchDeletedProducts() throws Exception {
		try {
			products = websiteProductsDAO.getDeletedProducts();
		} catch (Exception e) {
			logger.error("Error occurred while fetching deleted website products.",e);
		}
		return SUCCESS;
		
	}
	
	public String deleteProducts() throws Exception {
		try {
			websiteProductsDAO.deleteProducts(request);
		} catch (Exception e) {
			logger.error("Error occurred while deleting website products.",e);
		}
		return SUCCESS;
	}
	
	public String downloadAndPersistOrgillFilesFromFTP() throws Exception {
		try {
			websiteProductsDAO.downloadParseAndPersistWebFiles();
		} catch (Exception e) {
			logger.error("Error occurred while fetching deleted website products.",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchItemsToDeleteAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getItemsToDeleteAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchItemsToAddAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getItemsToAddAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String downloadAndPersistOrgillCategoriesFromFTP() throws Exception {
		try {
			websiteProductsDAO.downloadParseAndPersistWebCategories();
		} catch (Exception e) {
			logger.error("Error occurred while fetching deleted website products.",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchDeptToAddAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getDeptToAddAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchCategoryToAddAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getCategoryToAddAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchWebsiteItemsToDeleteAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getWebsiteItemsToDisableAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String deactivateWebsiteItems() throws Exception {
		try {
			websiteProductsDAO.deactivateWebsiteItems(request);
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchWebsiteItemsToActivateAfterSync() throws Exception {
		try {
			products = websiteProductsDAO.getWebsiteItemsToActivateAfterSync();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String activateWebsiteItems() throws Exception {
		try {
			websiteProductsDAO.activateWebsiteItems(request);
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchWebsiteOrders() throws Exception {
		try {
			orders = websiteProductsDAO.getWebsiteOrder();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String processOrders() throws Exception {
		try {
			websiteProductsDAO.processOrders(ordersToProcessFrom, ordersToProcess);
		} catch (Exception e) {
			logger.error("Error occurred while processing web orders.",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String updatePoNumber() throws Exception {
		try {
			websiteProductsDAO.updatePoNumber(request);
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		return SUCCESS;
	}
}
