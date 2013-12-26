package com.simplydifferent.dao.intr;

import java.util.List;

import com.simplydifferent.vo.PurchaseDetails;
import com.simplydifferent.vo.PurchaseInvoice;
import com.simplydifferent.vo.Supplier;

public interface PurchaseDAO {

	void saveInvoice(PurchaseInvoice invoice) throws Exception;

	List<PurchaseInvoice> getPurchaseInvoices();

	List<PurchaseInvoice> getPurchaseInvoicesBySupplier(Supplier supplier);

	boolean isInvoiceNumberValid(PurchaseInvoice invoice);

	List<PurchaseDetails> getInvoiceDetails(PurchaseInvoice invoice);

	void editInvoice(PurchaseInvoice invoice) throws Exception;
}
