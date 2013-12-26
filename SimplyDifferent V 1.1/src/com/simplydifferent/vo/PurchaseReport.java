package com.simplydifferent.vo;

import java.util.ArrayList;
import java.util.List;

public class PurchaseReport {

	private List<PurchaseInvoice> invoices;

	public List<PurchaseInvoice> getInvoices() {
		if (invoices == null) {
			invoices = new ArrayList<PurchaseInvoice>();
		}
		return invoices;
	}

	public void setInvoices(List<PurchaseInvoice> invoices) {
		this.invoices = invoices;
	}
}
