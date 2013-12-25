package com.jh.dao.pi;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jh.vo.RequestVO;

public interface PurchaseInvoiceDAO {

	String validateInvoice(File invoiceFile) throws Exception;

	void saveInvoices(RequestVO request);

	List<Map<String, String>> getAllPIFiles() throws IOException;

	String getFtpFileDetails(RequestVO request) throws Exception;

}
