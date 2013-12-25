package com.jh.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.jh.dao.item.ItemDAO;
import com.jh.util.CommonUtil;
import com.jh.vo.ReasonCode;
import com.jh.vo.RequestVO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class Item extends ActionSupport implements ModelDriven<RequestVO>, ServletResponseAware {

	private static final long serialVersionUID = 1596932811567206793L;
	private static Logger logger = Logger.getLogger(Item.class);

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
	
	private List<com.jh.vo.Item> items;	
	public List<com.jh.vo.Item> getItems() {
		return items;
	}

	private List<ReasonCode> reasonCodes;	
	public List<ReasonCode> getReasonCodes() {
		return reasonCodes;
	}
	
	private String imagePath;
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	private HttpServletResponse httpResponse;
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.httpResponse = servletResponse;
		
	}

	private ItemDAO itemDAO;	
	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}
	
	private CommonUtil commonUtil;	
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}
	
	public String fetchItemDetails() throws Exception {
		try {
			items = itemDAO.getItemDetails(request);
		} catch (Exception e) {
			logger.error("Error occured in fetchItemDetails.",e);
		}
		return SUCCESS;
	}
	
	public String updateItems() throws Exception {
		try {
			itemDAO.updateItems(request);
		} catch (Exception e) {
			logger.error("Error while updating item details.",e);
			throw e;
		}
		return SUCCESS;
	}
	
	private Boolean aliasValid;	
	public Boolean getAliasValid() {
		return aliasValid;
	}
	
	public String checkAliasUniqueness() {
		aliasValid = itemDAO.checkAliasUniqueness(request);
		return SUCCESS;
	}
	
	public String addAlias() throws Exception {
		try {
			itemDAO.addAlias(request);
		} catch (Exception e) {
			logger.error("Error occured while adding alias.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String deleteAlias() throws Exception {
		try {
			itemDAO.deleteAlias(request);
		} catch (Exception e) {
			logger.error("Error occured while deleting alias.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchReasonCodes() {
		reasonCodes = itemDAO.getReasonCodes();
		return SUCCESS;
	}
	
	public String deactivateItems() throws Exception {
		try {
			itemDAO.deactivateItems(request);
		} catch (Exception e) {
			logger.error("Error occured while deactivating items.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String addOrActivateItems() throws Exception {
		try {
			itemDAO.addOrActivateItems(request, imagePath);
		} catch (Exception e) {
			logger.error("Error occured while adding items.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String addDepartments() throws Exception {
		try {
			itemDAO.addDepartments(request);
		} catch (Exception e) {
			logger.error("Error occured while adding departments.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String addCategories() throws Exception {
		try {
			itemDAO.addCategories(request);
		} catch (Exception e) {
			logger.error("Error occured while adding categories.", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public void loadSkuImage() throws Exception{
		if (request.getSku() != null) {
			httpResponse.setContentType("image/jpeg");
			commonUtil.loadImageFromFileSystem(request.getSku(), httpResponse.getOutputStream());
		}
	}
}
