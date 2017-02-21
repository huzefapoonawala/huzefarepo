package com.dynamicsext.module.ies.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dynamicsext.module.ies.vo.AccountStatementDetailsVO;
import com.dynamicsext.module.ies.vo.PaymentEntryVO;
import com.dynamicsext.module.ies.vo.TenderVO;
import com.dynamicsext.module.ies.vo.TransactionEntryVO;
import com.dynamicsext.module.ies.vo.TransactionVO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtil {

	private static final FontFamily DEFAULT_FONT = Font.getFamily("Helvetica");
	private static final Font h1 = new Font(DEFAULT_FONT, 20, Font.BOLD), 
							bold = new Font(DEFAULT_FONT), 
							h3 = new Font(DEFAULT_FONT, 17, Font.BOLD);
	static{
		bold.setStyle(Font.BOLD);
	}
	
	@SuppressWarnings("unchecked")
	public static void generateAccountStatement(File pdfFile, Map<String, Object> model) throws Exception {
		Document document = createDocument(pdfFile);
		document.open();
		setHeader(document, model, "Account Statement"); 
		
		TransactionVO customer = (TransactionVO) model.get("customer");
		
		PdfPTable summaryTable = new PdfPTable(6);
		summaryTable.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell(new Phrase("Account Summary",h1));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(6);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Summary Information"));
		cell.setColspan(6);
		cell.setBackgroundColor(BaseColor.YELLOW);
		summaryTable.addCell(cell);
		
		PdfPTable subTable = new PdfPTable(new float[]{20,35,15,30});
		
		cell = new PdfPCell(new Phrase("Account Number:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getAccountNumber()));
		cell.setBorder(PdfPCell.NO_BORDER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Closing Date:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase((String)model.get("toDate")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Name:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getBillToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(2);
		subTable.addCell(cell);
		
		cell = new PdfPCell(subTable);
		cell.setColspan(6);
		summaryTable.addCell(cell);
		
		for (String label : new String[]{"Current","1 - 30 Days","31 - 60 Days","61 - 90 Days","Over 90 Days","Balance Due"}) {
			cell = new PdfPCell(new Phrase(label));
			cell.setBackgroundColor(BaseColor.YELLOW);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(cell);
		}
		
		cell = new PdfPCell(new Phrase(customer.getRange0InHtmlFormat()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getRange1To30InHtmlFormat()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getRange31To60InHtmlFormat()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getRange61To90InHtmlFormat()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getRange90InHtmlFormat()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getGrandTotalInHtmlFormatDefaultTo0()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		summaryTable.addCell(cell);
		
		document.add(summaryTable);
		
		document.add(new Phrase(" "));
		
		PdfPTable detailsTable = new PdfPTable(1);
		detailsTable.setWidthPercentage(100);
		cell = new PdfPCell(new Phrase("A c c o u n t R e c e i v a b l e A c t i v i t y", bold));
		cell.setBorder(PdfPCell.NO_BORDER);
		detailsTable.addCell(cell);
		document.add(detailsTable);
		
		subTable = new PdfPTable(new float[]{17,12,12,23,12,12,12});
		subTable.setWidthPercentage(100);
        subTable.setTableEvent(new BorderEvent());
		subTable.setHeaderRows(1);
		
		for (String label : new String[]{"Invoice #","Date|2","Due Date|2","Details","Debit|2","Credit|2","Balance|2"}) {
			int idx = label.indexOf("|");
			cell = new PdfPCell(new Phrase(idx == -1 ? label : label.substring(0, idx)));
			cell.setBackgroundColor(BaseColor.YELLOW);
			if (idx > -1) {
				cell.setHorizontalAlignment(Integer.valueOf(label.substring(idx+1)));
			}
			subTable.addCell(cell);
		}
		
		List<AccountStatementDetailsVO> details = (List<AccountStatementDetailsVO>)model.get("details");
		for (AccountStatementDetailsVO d : details) {
			for (int j = 0; j < 7; j++) {
				cell = new PdfPCell(new Phrase(" "));
				cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
				subTable.addCell(cell);
			}
			
			cell = new PdfPCell(new Phrase(d.getInvoiceNumber()));
			cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getInvoiceDateInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getDueDateInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getDetails()));
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getDebitInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getCreditInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(d.getBalanceInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			subTable.addCell(cell);
		}
		
		for (int j = 0; j < 7; j++) {
			cell = new PdfPCell(new Phrase(" "));
			cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
			subTable.addCell(cell);
		}
		
		cell = new PdfPCell(new Phrase("Total"));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase((String)model.get("totalDebit")));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase((String)model.get("totalCredit")));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(customer.getGrandTotalInHtmlFormatDefaultTo0()));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subTable.addCell(cell);
		
		document.add(subTable);
		
		document.close();
	}
	
	private static Document createDocument(File pdfFile) throws Exception {
		final float MARGIN = 18;
		Document document = new Document(PageSize.LETTER);
		document.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
		
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		return document;
	}
	
	private static void setHeader(Document document, Map<String, Object> model, String fileHeader) throws Exception {
		document.add(createHeader(model, fileHeader, null));
		document.add(new Phrase(" "));
	}
	
	private static PdfPTable createHeader( Map<String, Object> model, String fileHeader, PDFGenericInterface genericInterface) throws Exception {
		PdfPTable header = new PdfPTable(new float[]{38,62}), subTable = new PdfPTable(1);
		header.setWidthPercentage(100);
		
		PdfPCell cell = null;
		if (StringUtils.isNotBlank((String)model.get("storeLogoImg"))) {
			Image logo = Image.getInstance((String)model.get("storeLogoImg"));
			cell = new PdfPCell(logo);
		} else {
			cell = new PdfPCell(new Phrase((String) model.get("storeLogoText"), h3));
		}
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(StringUtils.isNotBlank((String)model.get("storeAddress")) ? ((String)model.get("storeAddress")) : "", new Font(DEFAULT_FONT, 9)));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		subTable.addCell(cell);
		
		cell = new PdfPCell(subTable);
		cell.setBorder(PdfPCell.NO_BORDER);
		header.addCell(cell);
		
		subTable = new PdfPTable(new float[]{30,35,35});
		cell = new PdfPCell(new Phrase(fileHeader, h1));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(3);
		subTable.addCell(cell);
		
		if (genericInterface != null) {
			genericInterface.decorateRightHeaderPart(subTable);
		}
		
		cell = new PdfPCell(subTable);
		cell.setBorder(PdfPCell.NO_BORDER);
		header.addCell(cell);
		
		return header;
	}
	
	private static PdfPCell createCellWithText(String text, int border) {
		PdfPCell cell = new PdfPCell(new Phrase(text));
		cell.setBorder(border);
		return cell;
	}
	
	@SuppressWarnings("unchecked")
	public static void generateInvoice(File pdfFile, Map<String, Object> model) throws Exception {
		Document document = createDocument(pdfFile);
		document.open();
		
		final TransactionVO transaction = (TransactionVO) model.get("transaction");
		
		PdfPTable header = createHeader(model, "Invoice", new PDFGenericInterface() {
			@Override
			public void decorateRightHeaderPart(PdfPTable headerRightPart) {
				PdfPCell cell = new PdfPCell(new Phrase(" "));
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(3);
				headerRightPart.addCell(cell);

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Invoice #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(Integer.valueOf(transaction.getTransactionNumber()).toString(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Account #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getAccountNumber(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Date:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionDatePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Time:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionTimePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Sales Rep:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getCashier(), PdfPCell.NO_BORDER));
			}
		});
		document.add(header);
		document.add(new Phrase(" "));
		
		PdfPTable tmpTable = new PdfPTable(new float[]{12,33,12,43});
		tmpTable.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell(new Phrase("Bill To:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getBillToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Ship To:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getShipToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(4);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Reference:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getReference()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(3);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Comment:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getComment()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(3);
		tmpTable.addCell(cell);

		document.add(tmpTable);
		document.add(new Phrase(" "));
		
		tmpTable = new PdfPTable(new float[]{20,41,13,13,13});
		tmpTable.setWidthPercentage(100);
		tmpTable.setTableEvent(new BorderEvent());
		tmpTable.setHeaderRows(1);
		
		for (String label : new String[]{"Item No.", "Description", "QTY|2", "Price|2", "Ext. Price|2"}) {
			int idx = label.indexOf("|");
			cell = new PdfPCell(new Phrase(idx == -1 ? label : label.substring(0, idx)));
			cell.setBackgroundColor(BaseColor.YELLOW);
			if (idx > -1) {
				cell.setHorizontalAlignment(Integer.valueOf(label.substring(idx+1)));
			}
			tmpTable.addCell(cell);
		}
		
		List<TransactionEntryVO> transactionEntries = (List<TransactionEntryVO>) model.get("transactionEntries");
		for (TransactionEntryVO t : transactionEntries) {
			cell = new PdfPCell(new Phrase(t.getItemLookupCode()));
			cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getDescription().replaceAll("<br>", "\n").replaceAll("&nbsp;", "")));
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getQuantity().toString()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getPriceInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getExtPriceInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
		}
		document.add(tmpTable);
		
		tmpTable = new PdfPTable(1);
		tmpTable.setWidthPercentage(100);
		cell = new PdfPCell(new Phrase(" "));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setColspan(5);
		cell.setBorder(PdfPCell.TOP);
		tmpTable.addCell(cell);
		document.add(tmpTable);		
		
		tmpTable = new PdfPTable(new float[]{87,13});
		tmpTable.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Sub Total"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		cell = new PdfPCell(new Phrase((String)model.get("subTotal")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Sales Tax"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getSalesTaxInDisplayFormat()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Total"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getGrandTotalInDisplayFormat()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(" "));
		cell.setColspan(2);
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		for (TenderVO t : (List<TenderVO>)model.get("tenders")) {
			cell = new PdfPCell(new Phrase(t.getDescription()+" Tendered"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable.addCell(cell);
			cell = new PdfPCell(new Phrase(t.getAmountInDisplayFormat()));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable.addCell(cell);
		}		
		cell = new PdfPCell(new Phrase("Change Due"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		cell = new PdfPCell(new Phrase((String)model.get("changeDue")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable.addCell(cell);
		
		document.add(tmpTable);
		
		document.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void generateQuoteAndWorkOrder(File pdfFile, Map<String, Object> model) throws Exception {
		Document document = createDocument(pdfFile);
		document.open();
		
		final int documentType = (Integer) model.get("documentType");
		final TransactionVO transaction = (TransactionVO) model.get("order");
		
		PdfPTable header = createHeader(model, documentType == Defaults.WORK_ORDER_TYPE ?  "Work Order" : "Quote", new PDFGenericInterface() {
			@Override
			public void decorateRightHeaderPart(PdfPTable headerRightPart) {
				PdfPCell cell = new PdfPCell(new Phrase(" "));
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(3);
				headerRightPart.addCell(cell);

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText((documentType == Defaults.WORK_ORDER_TYPE ? "Work order" : "Quote")+" #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(Integer.valueOf(transaction.getTransactionNumber()).toString(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Account #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getAccountNumber(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Date:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionDatePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Time:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionTimePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Sales Rep:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getCashier(), PdfPCell.NO_BORDER));
			}
		});
		document.add(header);
		document.add(new Phrase(" "));
		
		PdfPTable tmpTable = new PdfPTable(new float[]{12,33,12,43});
		tmpTable.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell(new Phrase("Bill To:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getBillToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Ship To:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getShipToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(4);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Reference:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getReference()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(3);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Comment:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getComment()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(3);
		tmpTable.addCell(cell);

		document.add(tmpTable);
		document.add(new Phrase(" "));
		
		tmpTable = new PdfPTable(new float[]{20,41,13,13,13});
		tmpTable.setWidthPercentage(100);
		tmpTable.setTableEvent(new BorderEvent());
		tmpTable.setHeaderRows(1);
		
		for (String label : new String[]{"Item No.", "Description", "QTY|2", "Price|2", "Ext. Price|2"}) {
			int idx = label.indexOf("|");
			cell = new PdfPCell(new Phrase(idx == -1 ? label : label.substring(0, idx)));
			cell.setBackgroundColor(BaseColor.YELLOW);
			if (idx > -1) {
				cell.setHorizontalAlignment(Integer.valueOf(label.substring(idx+1)));
			}
			tmpTable.addCell(cell);
		}
		
		List<TransactionEntryVO> transactionEntries = (List<TransactionEntryVO>) model.get("transactionEntries");
		for (TransactionEntryVO t : transactionEntries) {
			cell = new PdfPCell(new Phrase(t.getItemLookupCode()));
			cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
			tmpTable.addCell(cell);
			
			String description = t.getDescription().replaceAll("<br>", "\n").replaceAll("&nbsp;", "");
			if (documentType == Defaults.WORK_ORDER_TYPE) {
				if (t.getQuantityOnOrder() > 0) {
					description += "\nQuantity RTD:\nQuantity On Order:";
				}
			}
			cell = new PdfPCell(new Phrase(description));
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			String qty = t.getQuantity().toString();
			int lineCount = t.getDescription().split("<br>").length-1;
			for (int i = 0; i < lineCount; i++) {
				qty += "\n";
			}
			if (documentType == Defaults.WORK_ORDER_TYPE) {
				if (t.getQuantityOnOrder() > 0) {
					qty += "\n"+t.getQuantityRTD()+"\u00a0\u00a0\u00a0\u00a0\u00a0\n"+t.getQuantityOnOrder()+"\u00a0\u00a0\u00a0\u00a0\u00a0";
				}
			}
			qty+="\n\u00a0";
			cell = new PdfPCell(new Phrase(qty));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getPriceInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(t.getExtPriceInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
		}
		document.add(tmpTable);
		
		tmpTable = new PdfPTable(1);
		tmpTable.setWidthPercentage(100);
		cell = new PdfPCell(new Phrase(" "));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setColspan(5);
		cell.setBorder(PdfPCell.TOP);
		tmpTable.addCell(cell);
		document.add(tmpTable);		
		
		tmpTable = new PdfPTable(new float[]{62,38});
		tmpTable.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase((String)model.get("storeNotes")));
		cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tmpTable.addCell(cell);
		
		PdfPTable tmpTable2 = new PdfPTable(new float[]{58,42});
		tmpTable2.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Sub Total"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase((String)model.get("subTotal")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Sales Tax"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getSalesTaxInDisplayFormat()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Total"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getGrandTotalInDisplayFormat()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		if (documentType == Defaults.WORK_ORDER_TYPE) {
			cell = new PdfPCell(new Phrase(" "));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setColspan(2);
			tmpTable2.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Remaining Deposit"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
			cell = new PdfPCell(new Phrase(transaction.getDepositInHtmlFormat()));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
			
			cell = new PdfPCell(new Phrase("New Balance"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
			cell = new PdfPCell(new Phrase((String)model.get("newBalance")));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
		}
		
		cell = new PdfPCell(tmpTable2);
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		document.add(tmpTable);
		
		document.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void generateAccountPayment(File pdfFile, Map<String, Object> model) throws Exception {
		Document document = createDocument(pdfFile);
		document.open();
		
		final TransactionVO transaction = (TransactionVO) model.get("pr");
		
		PdfPTable header = createHeader(model, "Account Payment", new PDFGenericInterface() {
			@Override
			public void decorateRightHeaderPart(PdfPTable headerRightPart) {
				PdfPCell cell = new PdfPCell(new Phrase(" "));
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(3);
				headerRightPart.addCell(cell);

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Payment #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(Integer.valueOf(transaction.getTransactionNumber()).toString(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Account #:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getAccountNumber(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Date:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionDatePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Time:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getTransactionTimePart(), PdfPCell.NO_BORDER));

				headerRightPart.addCell(createCellWithText("", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText("Sales Rep:", PdfPCell.NO_BORDER));
				headerRightPart.addCell(createCellWithText(transaction.getCashier(), PdfPCell.NO_BORDER));
			}
		});
		document.add(header);
		document.add(new Phrase(" "));
		
		PdfPTable tmpTable = new PdfPTable(new float[]{12,88});
		tmpTable.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell(new Phrase("Bill To:"));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(transaction.getBillToDetails("\n")));
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setColspan(4);
		tmpTable.addCell(cell);
		
		document.add(tmpTable);
		
		tmpTable = new PdfPTable(new float[]{19,15,15,17,17,17});
		tmpTable.setWidthPercentage(100);
		tmpTable.setTableEvent(new BorderEvent());
		tmpTable.setHeaderRows(1);
		
		for (String label : new String[]{"Reference", "Invoice Date|1", "Due Date|1", "Invoice Amount|2", "Balance Due|2", "Payment|2"}) {
			int idx = label.indexOf("|");
			cell = new PdfPCell(new Phrase(idx == -1 ? label : label.substring(0, idx)));
			cell.setBackgroundColor(BaseColor.YELLOW);
			if (idx > -1) {
				cell.setHorizontalAlignment(Integer.valueOf(label.substring(idx+1)));
			}
			tmpTable.addCell(cell);
		}
		
		List<PaymentEntryVO> paymentEntries = (List<PaymentEntryVO>) model.get("paymentEntries");
		for (PaymentEntryVO p : paymentEntries) {
			cell = new PdfPCell(new Phrase("TR-"+p.getTransactionNumber()));
			cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(p.getInvoiceDateInHtmlFormat()));
			cell.setBorder(PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(p.getDueDateInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(p.getInvoiceAmountInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(p.getBalanceDueInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase(p.getPaymentInHtmlFormat()));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.RIGHT);
			tmpTable.addCell(cell);
		}
		
		document.add(tmpTable);
		
		tmpTable = new PdfPTable(1);
		tmpTable.setWidthPercentage(100);
		cell = new PdfPCell(new Phrase(" "));
		cell.setBackgroundColor(BaseColor.YELLOW);
		cell.setColspan(5);
		cell.setBorder(PdfPCell.TOP);
		tmpTable.addCell(cell);
		document.add(tmpTable);		
		
		tmpTable = new PdfPTable(new float[]{62,38});
		tmpTable.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase((String)model.get("storeNotes")));
		cell.setBorder(PdfPCell.RIGHT | PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tmpTable.addCell(cell);
		
		PdfPTable tmpTable2 = new PdfPTable(new float[]{58,42});
		tmpTable2.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Total Payments\n\n"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getGrandTotalInDisplayFormat()+"\n\n"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		List<TenderVO> tenders = (List<TenderVO>) model.get("tenders");
		for (TenderVO t : tenders) {
			cell = new PdfPCell(new Phrase("Paid "+t.getDescription()));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
			cell = new PdfPCell(new Phrase(t.getAmountInDisplayFormat()));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tmpTable2.addCell(cell);
		}
		
		cell = new PdfPCell(new Phrase("\nPrevious Balance"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase("\n"+model.get("previousBal")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Payments"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase(transaction.getGrandTotalInDisplayFormat()));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("New Balance"));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		cell = new PdfPCell(new Phrase((String)model.get("newBal")));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tmpTable2.addCell(cell);
		
		cell = new PdfPCell(tmpTable2);
		cell.setBorder(PdfPCell.NO_BORDER);
		tmpTable.addCell(cell);
		
		document.add(tmpTable);
		
		document.close();
	}
}

class BorderEvent implements PdfPTableEvent {
    public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        float width[] = widths[0];
        float x1 = width[0];
        float x2 = width[width.length - 1];
        float y1 = heights[0];
        float y2 = heights[heights.length - 1];
        PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
        cb.rectangle(x1, y1, x2 - x1, y2 - y1);
        cb.stroke();
        cb.resetRGBColorStroke();
    }
}

interface PDFGenericInterface{
	void decorateRightHeaderPart(PdfPTable headerRightPart);
}