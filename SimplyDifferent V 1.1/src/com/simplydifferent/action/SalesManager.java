package com.simplydifferent.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.SalesDAO;
import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.SalesDetails;
import com.simplydifferent.vo.SalesInvoice;
import com.simplydifferent.vo.SalesReceipt;

public class SalesManager extends ActionSupport implements ServletResponseAware {

	private static final long serialVersionUID = -6797609395020324776L;

	private SalesInvoice invoice;
	public SalesInvoice getInvoice() {
		return invoice;
	}
	public void setInvoice(SalesInvoice invoice) {
		this.invoice = invoice;
	}
	
	private int nextInvoiceNumber;	
	public int getNextInvoiceNumber() {
		return nextInvoiceNumber;
	}
	
	private List<SalesInvoice> invoices;	
	public List<SalesInvoice> getInvoices() {
		return invoices;
	}
	
	private List<SalesDetails> invoiceDetails;	
	public List<SalesDetails> getInvoiceDetails() {
		return invoiceDetails;
	}
	
	private SalesDetails details;
	public SalesDetails getDetails() {
		return details;
	}
	
	private Product product;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	private Customer customer;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	private SalesReceipt sr;		
	public SalesReceipt getSr() {
		return sr;
	}
	public void setSr(SalesReceipt sr) {
		this.sr = sr;
	}

	private HttpServletResponse response;
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	private SalesDAO salesDAO;	
	public void setSalesDAO(SalesDAO salesDAO) {
		this.salesDAO = salesDAO;
	}
	
	public String save() throws Exception {
		try {
			invoice = salesDAO.saveInvoice(invoice);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String isInvoiceNumberValid() {
		invoice.setIsInvoiceNumberValid(salesDAO.isInvoiceNumberValid(invoice));
		return SUCCESS;
	}
	
	public String fetchNextInvoiceNumber() {
		nextInvoiceNumber = salesDAO.getNextInvoiceNumber();
		return SUCCESS;
	}
	
	public String generateInvoicePdf() throws Exception {
		response.setContentType("application/pdf");
		salesDAO.generateInvoicePdf(invoice, response.getOutputStream());
		return null;
	}
	
	public String fetchInvoicesByCustomer() {
		invoices = salesDAO.getInvoicesByCustomer(invoice);
		return SUCCESS;
	}
	
	public String fetchInvoiceDetails() throws Exception {
		invoiceDetails = salesDAO.getInvoiceDetails(invoice);
		return SUCCESS;
	}
	
	public String fetchDetailsForProductByCustomer() throws Exception {
		details = salesDAO.getSalesDetailsForProductByCustomer(customer, product);
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		try {
			salesDAO.editInvoice(invoice);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchInvoicesForSalesReceipt() throws Exception {
		invoices = salesDAO.getInvoicesForSalesReceipt(invoice);
		return SUCCESS;
	}
	
	public String createSalesReceipt() throws Exception {
		try {
			salesDAO.createSalesReceipt(sr);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
}
