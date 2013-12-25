package com.jh.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.jh.dao.po.PurchaseOrderDAO;
import com.jh.vo.PurchaseOrderDetails;
import com.jh.vo.RequestVO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class PurchaseOrder extends ActionSupport implements ModelDriven<RequestVO>, ServletResponseAware {

	private static final long serialVersionUID = -4671283902701364231L;
	private static Logger logger = Logger.getLogger(PurchaseOrder.class);

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
	
	private List<PurchaseOrderDetails> poDetails;	
	public List<PurchaseOrderDetails> getPoDetails() {
		return poDetails;
	}
	
	private HttpServletResponse httpResponse;
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.httpResponse = servletResponse;
		
	}

	private PurchaseOrderDAO purchaseOrderDAO;	
	public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
		this.purchaseOrderDAO = purchaseOrderDAO;
	}
	
	public String fetchDetailsForPurchaseOrder() {
		poDetails = purchaseOrderDAO.getDetailsForPurchaseOrder(request);
		return SUCCESS;
	}
	
	public String generatePO() {
		try {
			httpResponse.setContentType("text/csv");
			httpResponse.setHeader("Content-Disposition", "attachment; filename=po.csv");
			PrintWriter writer = httpResponse.getWriter();
			purchaseOrderDAO.generatePO(request, writer);
			writer.close();
		} catch (Exception e) {
			logger.error("Error while generating po.", e);
		}
		return null;
	}
}
