package com.simplydifferent.action;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.ReportDAO;
import com.simplydifferent.vo.ProductStockReport;
import com.simplydifferent.vo.PurchaseReport;
import com.simplydifferent.vo.ReportRequest;

public class Reports extends ActionSupport {

	private static final long serialVersionUID = 1268513577227659876L;

	private ReportRequest request;	
	public ReportRequest getRequest() {
		return request;
	}
	public void setRequest(ReportRequest request) {
		this.request = request;
	}
	
	private PurchaseReport purchaseReport;
	public PurchaseReport getPurchaseReport() {
		return purchaseReport;
	}
	
	private ProductStockReport productStockReport;	
	public ProductStockReport getProductStockReport() {
		return productStockReport;
	}

	private ReportDAO reportDAO;	
	public void setReportDAO(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}
	
	public String fetchPurchaseReport() throws Exception {
		purchaseReport = reportDAO.getPurchaseReport(request);
		return SUCCESS;
	}
	
	public String fetchProductStockReport() throws Exception {
		productStockReport = reportDAO.getProductStockReport(request);
		return SUCCESS;
	}
}
