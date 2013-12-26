package com.simplydifferent.vo;

import java.util.List;

public class SalesInvoicePdfVO {

	private Customer customer;
	private SalesInvoice invoice;
	private List<SalesDetails> details;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public SalesInvoice getInvoice() {
		return invoice;
	}
	public void setInvoice(SalesInvoice invoice) {
		this.invoice = invoice;
	}
	public List<SalesDetails> getDetails() {
		return details;
	}
	public void setDetails(List<SalesDetails> details) {
		this.details = details;
	}
}
