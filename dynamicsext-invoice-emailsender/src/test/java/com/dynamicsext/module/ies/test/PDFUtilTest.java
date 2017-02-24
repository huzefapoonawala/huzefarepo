package com.dynamicsext.module.ies.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynamicsext.module.ies.util.PDFUtil;
import com.dynamicsext.module.ies.vo.PaymentEntryVO;
import com.dynamicsext.module.ies.vo.TenderVO;
import com.dynamicsext.module.ies.vo.TransactionEntryVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

public class PDFUtilTest {

	public static void main(String[] args) throws Exception {
		String description = "Masing tape Beige 1.5\"<br><br>add correct p/n";
		int lineCount = description.split("<br>").length-1; 
		String qty = "1";
		for (int i = 0; i < lineCount; i++) {
			qty += "ln";
		}
		System.out.println(qty);
		testQuotePdf();
		testWorkOrderPdf();
		testAccountPaymentPdf();
	}
	
	public static void testInvoicePdf() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("storeLogoImg", "./configs/jamaicahardwarelogo_218X44.jpg");
		model.put("storeLogoText", "Jamaica Electrical & Hardware Inc.");
		model.put("storeAddress", "131-01 Jamaica Ave.\nRichmond Hill, NY 11418\nTel. & Fax: 718-880-1258\nEmail: Sales@JamaicaHardware.com\nWebsite: www.jamaicahardware.com");
		
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setTransactionNumber(217509);
		transactionVO.setAccountNumber("7187381804");
		transactionVO.setTransactionDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse("08/10/2016 03:27:42 PM"));
		transactionVO.setCashier("DPE");
		transactionVO.setBillToCompany("Empire Hardware & Supply");
		transactionVO.setBillToName("Juzar Ezzi");
		transactionVO.setBillToAddress("112-18 LIberty Ave");
		transactionVO.setBillToCity("South Richmond Hill");
		transactionVO.setBillToState("NY");
		transactionVO.setBillToZip("11419");
		transactionVO.setBillToPhone("718-738-1804");
		
		List<TransactionEntryVO> entries = new ArrayList<TransactionEntryVO>();
		TransactionEntryVO entry = new TransactionEntryVO();
		entry.setItemLookupCode("COPY");
		entry.setDescription("PHOTO COPY");
		entry.setQuantity(1);
		entry.setPrice(0.03);
		entry.setExtPrice(0.03);
		entries.add(entry);
		model.put("transactionEntries", entries);
		
		model.put("subTotal", "$0.03");
		
		transactionVO.setSalesTax(0.00);
		transactionVO.setGrandTotal(0.03);
		model.put("transaction", transactionVO);
		
		List<TenderVO> tenders = new ArrayList<TenderVO>();
		model.put("tenders", tenders);
		
		model.put("changeDue", "$0.00");
		
		PDFUtil.generateInvoice(new File("./samples/sample-invoice.pdf"), model);
	}
	
	public static void testQuotePdf() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("storeLogoImg", "./configs/jamaicahardwarelogo_218X44.jpg");
		model.put("storeLogoText", "Jamaica Electrical & Hardware Inc.");
		model.put("storeAddress", "131-01 Jamaica Ave.\nRichmond Hill, NY 11418\nTel. & Fax: 718-880-1258\nEmail: Sales@JamaicaHardware.com\nWebsite: www.jamaicahardware.com");
		model.put("storeNotes", "All eligible Returns and Exchanges must be made in 7 days with ORIGINAL Invoice and in original unopened packing. Returns not available on the following products: Custom Tinted Paint, Hand Tools, Power Tools, Electrical.");
		
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setTransactionNumber(1);
		transactionVO.setAccountNumber("5166034725");
		transactionVO.setTransactionDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse("06/21/2016 02:19:20 PM"));
		transactionVO.setCashier("Cashier 1");
		
		transactionVO.setBillToCompany("Ezzi & Sons, US.");
		transactionVO.setBillToName("Shabbir Ezzi");
		transactionVO.setBillToAddress("2624 flower st");
		transactionVO.setBillToCity("Westbury");
		transactionVO.setBillToState("NY");
		transactionVO.setBillToZip("11590");
		transactionVO.setBillToPhone("516-603-4725");
		
		transactionVO.setShipToCompany("Jamaica Hardware");
		transactionVO.setShipToName("Shabs");
		transactionVO.setShipToAddress("131-01 Jamaica Ave");
		transactionVO.setShipToCity("Richmond Hill");
		transactionVO.setShipToState("NY");
		transactionVO.setShipToZip("11418");
		transactionVO.setShipToPhone("718-880-1258");
		
		transactionVO.setReference("Regal Quote");
		transactionVO.setComment("InRregal they have Pearl finish instead of Satin.");
		
		List<TransactionEntryVO> entries = new ArrayList<TransactionEntryVO>();
		TransactionEntryVO entry = new TransactionEntryVO();
		entry.setItemLookupCode("3043726");
		entry.setDescription("Purdy Roller Cover 4PK 3/8NAP");
		entry.setQuantity(3);
		entry.setPrice(13.79);
		entry.setExtPrice(41.37);
		entries.add(entry);
		
		entry = new TransactionEntryVO();
		entry.setItemLookupCode("N02300-005");
		entry.setDescription("Fresh Start INT/EXT Primer 5GL");
		entry.setQuantity(5);
		entry.setPrice(128.79);
		entry.setExtPrice(643.95);
		entries.add(entry);
		
		entry = new TransactionEntryVO();
		entry.setItemLookupCode("05471X-005");
		entry.setDescription("Regal Select Flat 1X 5Gal<br>&nbsp;2130-70 Seatle Gray");
		entry.setQuantity(4);
		entry.setPrice(179.40);
		entry.setExtPrice(717.60);
		entries.add(entry);
		
		entry = new TransactionEntryVO();
		entry.setItemLookupCode("05501X-005");
		entry.setDescription("Regal Select Pearl 1X 5Gal<br>&nbsp;2130-70 Seatle Gray, Similar to Satin");
		entry.setQuantity(4);
		entry.setPrice(179.39);
		entry.setExtPrice(717.56);
		entries.add(entry);
		model.put("transactionEntries", entries);
		
		model.put("subTotal", "$2,120.48");
		
		transactionVO.setSalesTax(188.19);
		transactionVO.setGrandTotal(2308.67);
		
		model.put("order", transactionVO);
		
		model.put("documentType", 1);
		
		PDFUtil.generateQuoteAndWorkOrder(new File("./samples/sample-quote.pdf"), model);
	}
	
	public static void testWorkOrderPdf() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("storeLogoImg", "./configs/jamaicahardwarelogo_218X44.jpg");
		model.put("storeLogoText", "Jamaica Electrical & Hardware Inc.");
		model.put("storeAddress", "131-01 Jamaica Ave.\nRichmond Hill, NY 11418\nTel. & Fax: 718-880-1258\nEmail: Sales@JamaicaHardware.com\nWebsite: www.jamaicahardware.com");
		model.put("storeNotes", "All eligible Returns and Exchanges must be made in 7 days with ORIGINAL Invoice and in original unopened packing. Returns not available on the following products: Custom Tinted Paint, Hand Tools, Power Tools, Electrical.");
		
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setTransactionNumber(1);
		transactionVO.setAccountNumber("5166034725");
		transactionVO.setTransactionDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse("06/21/2016 02:19:20 PM"));
		transactionVO.setCashier("Cashier 1");
		
		transactionVO.setBillToCompany("Ezzi & Sons, US.");
		transactionVO.setBillToName("Shabbir Ezzi");
		transactionVO.setBillToAddress("2624 flower st");
		transactionVO.setBillToCity("Westbury");
		transactionVO.setBillToState("NY");
		transactionVO.setBillToZip("11590");
		transactionVO.setBillToPhone("516-603-4725");
		
		/*transactionVO.setShipToCompany("Jamaica Hardware");
		transactionVO.setShipToName("Shabs");
		transactionVO.setShipToAddress("131-01 Jamaica Ave");
		transactionVO.setShipToCity("Richmond Hill");
		transactionVO.setShipToState("NY");
		transactionVO.setShipToZip("11418");
		transactionVO.setShipToPhone("718-880-1258");*/
		
		transactionVO.setReference("Regal Quote");
//		transactionVO.setComment("InRregal they have Pearl finish instead of Satin.");
		
		List<TransactionEntryVO> entries = new ArrayList<TransactionEntryVO>();
		TransactionEntryVO entry = new TransactionEntryVO();
		entry.setItemLookupCode("COPY");
		entry.setDescription("Custom Photo Copy");
		entry.setQuantity(5);
		entry.setQuantityRTD(2);
		entry.setQuantityOnOrder(3);
		entry.setPrice(0.18);
		entry.setExtPrice(0.90);
		entries.add(entry);
		
		entry = new TransactionEntryVO();
		entry.setItemLookupCode("3948866");
		entry.setDescription("KW1 House Key Kwikset");
		entry.setQuantity(3);
		entry.setQuantityOnOrder(0);
		entry.setPrice(1.38);
		entry.setExtPrice(4.14);
		entries.add(entry);
		
		entry = new TransactionEntryVO();
		entry.setItemLookupCode("FAX");
		entry.setDescription("Domestic Fax Service");
		entry.setQuantity(2);
		entry.setQuantityRTD(0);
		entry.setQuantityOnOrder(2);
		entry.setPrice(1.15);
		entry.setExtPrice(2.30);
		entries.add(entry);
		
		model.put("transactionEntries", entries);
		
		model.put("subTotal", "$7.34");
		
		transactionVO.setSalesTax(0.00);
		transactionVO.setGrandTotal(7.34);
		transactionVO.setDeposit(0.12);
		
		model.put("newBalance", "$2.40");
		
		model.put("order", transactionVO);
		
		model.put("documentType", 2);
		
		PDFUtil.generateQuoteAndWorkOrder(new File("./samples/sample-workorder.pdf"), model);
	}
	
	public static void testAccountPaymentPdf() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("storeLogoImg", "./configs/jamaicahardwarelogo_218X44.jpg");
		model.put("storeLogoText", "Jamaica Electrical & Hardware Inc.");
		model.put("storeAddress", "131-01 Jamaica Ave.\nRichmond Hill, NY 11418\nTel. & Fax: 718-880-1258\nEmail: Sales@JamaicaHardware.com\nWebsite: www.jamaicahardware.com");
		model.put("storeNotes", "All eligible Returns and Exchanges must be made in 7 days with ORIGINAL Invoice and in original unopened packing. Returns not available on the following products: Custom Tinted Paint, Hand Tools, Power Tools, Electrical.");
		
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setTransactionNumber(1);
		transactionVO.setAccountNumber("5166034725");
		transactionVO.setTransactionDate(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse("06/21/2016 02:19:20 PM"));
		transactionVO.setCashier("Cashier 1");
		
		transactionVO.setBillToCompany("Ezzi & Sons, US.");
		transactionVO.setBillToName("Shabbir Ezzi");
		transactionVO.setBillToAddress("2624 flower st");
		transactionVO.setBillToCity("Westbury");
		transactionVO.setBillToState("NY");
		transactionVO.setBillToZip("11590");
		transactionVO.setBillToPhone("516-603-4725");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		List<PaymentEntryVO> paymentEntries = new ArrayList<PaymentEntryVO>();
		PaymentEntryVO p = new PaymentEntryVO();
		p.setTransactionNumber(208399l);
		p.setInvoiceDate(sdf.parse("05/26/16"));
		p.setDueDate(sdf.parse("05/26/16"));
		p.setInvoiceAmount(182.15);
		p.setBalanceDue(50.00);
		p.setPayment(77.86);
		paymentEntries.add(p);
		
		p = new PaymentEntryVO();
		p.setTransactionNumber(208400l);
		p.setInvoiceDate(sdf.parse("05/26/16"));
		p.setDueDate(sdf.parse("05/26/16"));
		p.setInvoiceAmount(18.40);
		p.setBalanceDue(0.00);
		p.setPayment(18.40);
		paymentEntries.add(p);
		
		p = new PaymentEntryVO();
		p.setTransactionNumber(208556l);
		p.setInvoiceDate(sdf.parse("05/27/16"));
		p.setDueDate(sdf.parse("05/27/16"));
		p.setInvoiceAmount(51.14);
		p.setBalanceDue(0.00);
		p.setPayment(51.14);
		paymentEntries.add(p);
		
		p = new PaymentEntryVO();
		p.setTransactionNumber(208923l);
		p.setInvoiceDate(sdf.parse("05/31/16"));
		p.setDueDate(sdf.parse("05/31/16"));
		p.setInvoiceAmount(76.72);
		p.setBalanceDue(0.00);
		p.setPayment(76.72);
		paymentEntries.add(p);
		
		p = new PaymentEntryVO();
		p.setTransactionNumber(209920l);
		p.setInvoiceDate(sdf.parse("06/08/16"));
		p.setDueDate(sdf.parse("06/08/16"));
		p.setInvoiceAmount(118.23);
		p.setBalanceDue(118.23);
		p.setPayment(0.00);
		paymentEntries.add(p);
		model.put("paymentEntries", paymentEntries);
		
		transactionVO.setGrandTotal(224.12);
		model.put("previousBal", "$392.35");
		model.put("newBal", "$168.23");
		model.put("pr", transactionVO);
		
		List<TenderVO> tenders = new ArrayList<TenderVO>();
		TenderVO t = new TenderVO();
		t.setDescription("Credit Cards");
		t.setAmount(124.12);
		tenders.add(t);
		
		t = new TenderVO();
		t.setDescription("Cash");
		t.setAmount(100);
		tenders.add(t);
		
		model.put("tenders", tenders);
		
		PDFUtil.generateAccountPayment(new File("./samples/sample-accountpayment.pdf"), model);
	}
}
