package com.jh.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.jh.dao.pi.PurchaseInvoiceDAO;
import com.jh.vo.RequestVO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class InvoiceUploader extends ActionSupport implements ServletResponseAware, ModelDriven<RequestVO> {

	private static final long serialVersionUID = -8374855287179464588L;
	private static Logger logger = Logger.getLogger(InvoiceUploader.class);
	
	private HttpServletResponse response;
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}
	
	private String invoiceFileContentType;	
	public String getInvoiceFileContentType() {
		return invoiceFileContentType;
	}
	public void setInvoiceFileContentType(String invoiceFileContentType) {
		this.invoiceFileContentType = invoiceFileContentType;
	}
	
	private File invoiceFile;
	public File getInvoiceFile() {
		return invoiceFile;
	}
	public void setInvoiceFile(File invoiceFile) {
		this.invoiceFile = invoiceFile;
	}
	
	private Integer supplierId;	
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	
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
	
	private List<Map<String, String>> piFiles;
	public List<Map<String, String>> getPiFiles() {
		return piFiles;
	}

	private PurchaseInvoiceDAO purchaseInvoiceDAO;	
	public void setPurchaseInvoiceDAO(PurchaseInvoiceDAO purchaseInvoiceDAO) {
		this.purchaseInvoiceDAO = purchaseInvoiceDAO;
	}
	
	public String validateInvoiceFile() {
		String data = "";
		try {
			data = purchaseInvoiceDAO.validateInvoice(invoiceFile);
		} catch (Exception e) {
			data = "{errorMessage:\"Error parsing invoice file\"}";
			logger.error("Error parsing invoice file.",e);
		}
		response.setContentType("text/html");
		try {
			PrintWriter out = response.getWriter();
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body><textarea>");
			sb.append(data);
			sb.append("</textarea></body></html>");
			out.write(sb.toString());
			out.close();
		} catch (Exception e) {
			logger.error("Error occured while uploading invoice file.", e);
		}
		return null;
	}
	
	public String saveInvoices() throws Exception{
		try {
			request.setUpdateBinLocation(true);
			purchaseInvoiceDAO.saveInvoices(request);
		} catch (Exception e) {
			logger.error("Error while saving invoices.",e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchAllPIFiles() throws Exception{
		piFiles = purchaseInvoiceDAO.getAllPIFiles();
		return SUCCESS;
	}
	
	public String showFtpFileDetails() throws Exception{
		String data = "";
		try {
			data = purchaseInvoiceDAO.getFtpFileDetails(request);
		} catch (Exception e) {
			data = "{errorMessage:\"Error parsing invoice file\"}";
			logger.error("Error parsing invoice file.",e);
		}
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.write(data);
		out.close();
		return null;
	}
}
