package com.simplydifferent.dao.intr;

import java.io.OutputStream;
import java.util.List;

import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.SalesDetails;
import com.simplydifferent.vo.SalesInvoice;
import com.simplydifferent.vo.SalesReceipt;

public interface SalesDAO {

	SalesInvoice saveInvoice(SalesInvoice invoice) throws Exception;

	boolean isInvoiceNumberValid(SalesInvoice invoice);

	int getNextInvoiceNumber();

	void generateInvoicePdf(SalesInvoice invoice, OutputStream out) throws Exception;

	List<SalesInvoice> getInvoicesByCustomer(SalesInvoice invoice);

	List<SalesDetails> getInvoiceDetails(SalesInvoice invoice) throws Exception;

	SalesDetails getSalesDetailsForProductByCustomer(Customer customer, Product product);

	void editInvoice(SalesInvoice invoice) throws Exception;

	List<SalesInvoice> getInvoicesForSalesReceipt(SalesInvoice invoice);

	void createSalesReceipt(SalesReceipt sr);

}
