package com.simplydifferent.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.PurchaseDAO;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.PurchaseDetails;
import com.simplydifferent.vo.PurchaseInvoice;
import com.simplydifferent.vo.Supplier;

public class PurchaseManager extends ActionSupport{

	private static final long serialVersionUID = -3416710931943047441L;
	
	private PurchaseInvoice invoice;
	public PurchaseInvoice getInvoice() {
		return invoice;
	}
	public void setInvoice(PurchaseInvoice invoice) {
		this.invoice = invoice;
	}

	private List<PurchaseInvoice> invoices;
	public List<PurchaseInvoice> getInvoices() {
		return invoices;
	}
	
	private List<PurchaseDetails> invoiceDetails;
	public List<PurchaseDetails> getInvoiceDetails() {
		return invoiceDetails;
	}

	private Supplier supplier;
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	private Product product;	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	private PurchaseDAO purchaseDAO;	
	public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
		this.purchaseDAO = purchaseDAO;
	}
	
	public String save() throws Exception {
		purchaseDAO.saveInvoice(invoice);
		return SUCCESS;
	}
	
	public String fetchInvoices() throws Exception {
		invoices = purchaseDAO.getPurchaseInvoices();
		return SUCCESS;
	}
	
	public String fetchInvoicesBySupplier() throws Exception {
		invoices = purchaseDAO.getPurchaseInvoicesBySupplier(supplier);
		return SUCCESS;
	}
	
	public String isInvoiceNumberValid() {
		invoice.setIsInvoiceNumberValid(purchaseDAO.isInvoiceNumberValid(invoice));
		return SUCCESS;
	}
	
	public String fetchInvoiceDetails() throws Exception {
		invoiceDetails = purchaseDAO.getInvoiceDetails(invoice);
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		purchaseDAO.editInvoice(invoice);
		return SUCCESS;
	}
}