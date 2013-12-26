package com.simplydifferent.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.simplydifferent.vo.SalesDetails;
import com.simplydifferent.vo.SalesInvoicePdfVO;

public class PdfGenerator {
	
	private BaseFont HEADER_FONT, TEXT_FONT;
	
	private String templatePath = "";	
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	private String headerFontPath = BaseFont.COURIER_BOLD;
	public void setHeaderFontPath(String headerFontPath) {
		this.headerFontPath = headerFontPath;
	}
	
	private String textFontPath = BaseFont.COURIER;
	public void setTextFontPath(String textFontPath) {
		this.textFontPath = textFontPath;
	}

	public PdfGenerator() {
		super();
	}
	
	public void initFonts() {
		try {
			HEADER_FONT = BaseFont.createFont(headerFontPath,BaseFont.WINANSI,true);
			TEXT_FONT = BaseFont.createFont(textFontPath,BaseFont.WINANSI,true);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateSalesInvoicePdf(SalesInvoicePdfVO pdfVO, OutputStream out) throws Exception {
		PdfImportedPage templatePage = null;
		Document document = new Document(PageSize.A4);
		document.setMargins(36, 36, 100, 185);
		document.setMarginMirroring(true);
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, out);
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 800));
			
			File file = new File(templatePath);
			if (file.isFile()) {
				PdfReader pdfReader = new PdfReader(new FileInputStream(file));
				templatePage = writer.getImportedPage(pdfReader, 1);
				pdfReader.close();
			}
			
			HeaderFooter event = new HeaderFooter(templatePage);
			writer.setPageEvent(event);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.open();
		PdfPTable table = new PdfPTable(new float[] { 8, 3, 3, 2, 3, 2, 3, 3 });
		table.setWidthPercentage(100f);
		table.setSplitLate(false);
		table.getDefaultCell().setBorder(0);

		Font font = new Font(HEADER_FONT,12);
		table.addCell(createCell(new Phrase("TAX INVOICE # "+pdfVO.getInvoice().getInvoiceNumber(), font), 8, 0, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BASELINE, null));

		font = new Font(TEXT_FONT,10);
		table.addCell(createCell(new Phrase("\n"+DateUtil.convertDate2String(pdfVO.getInvoice().getInvoiceDate(), "dd-MMM-yyyy"),font), 8, 0, -1, PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_TOP, null));
		
		table.addCell(createCell(new Phrase("\n"+"To,\n"+pdfVO.getCustomer().getName().toUpperCase()+(StringUtils.isEmpty(pdfVO.getCustomer().getAddress())?"" : (",\n"+pdfVO.getCustomer().getAddress().toUpperCase()))+"\n\n\n\n ",font), 4, 0, -1, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_TOP, null));
		table.addCell(createCell(new Phrase(""), 4, 0, -1, PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_TOP, null));

		table.getDefaultCell().setUseAscender(true);
		font = new Font(HEADER_FONT,8);
		for (String text : "Product,Rate,Qty,Unit,Net,VAT(%),Tax Amt,Total".split(",")) {
			table.addCell(createCell(new Phrase(text,font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, BaseColor.LIGHT_GRAY));
		}
		
		Double net,tax,total,tnet = 0d,ttax = 0d,gtotal = 0d;
		Map<Float, Double[]> taxMap = new TreeMap<Float, Double[]>(); 
		font = new Font(TEXT_FONT,8);		
		for (SalesDetails sd : pdfVO.getDetails()) {
			table.addCell(createCell(new Phrase(sd.getBrandName()+" - "+sd.getProductName(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(Double.valueOf(sd.getSalesPrice()), 2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			table.addCell(createCell(new Phrase(Utilities.roundOf2ProperDecimalAndConvert2String(sd.getQuantity(),2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			table.addCell(createCell(new Phrase(sd.getUnitType(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			net = Double.valueOf(sd.getSalesPrice()*sd.getQuantity());
			tnet += net;
			table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(net, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			table.addCell(createCell(new Phrase(Utilities.roundOf2ProperDecimalAndConvert2String(sd.getVat(), 2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			tax = (net*sd.getVat())/100;
			ttax += tax;
			table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(tax, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			total = net+tax;
			gtotal += total;
			table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(total, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			if (!taxMap.containsKey(sd.getVat())) {
				taxMap.put(sd.getVat(), new Double[]{net,tax});
			}
			else{
				taxMap.get(sd.getVat())[0] = Utilities.roundOf(taxMap.get(sd.getVat())[0]+net, 2);
				taxMap.get(sd.getVat())[1] = Utilities.roundOf(taxMap.get(sd.getVat())[1]+tax, 2);
			}
		}
		for (int i = 0; i < 4; i++) {
			table.addCell(createCell(new Phrase("",font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		}
		table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(tnet, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		table.addCell(createCell(new Phrase("",font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(ttax, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		font = new Font(HEADER_FONT,11);
		gtotal = Math.ceil(gtotal);
		table.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(gtotal,2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));

		table.addCell(createCell(new Phrase(""), 8, 0f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		
		/*font = new Font(HEADER_FONT,8);
		table.addCell(createCell(new Phrase(""), 4, 0f, -1, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_TOP, null));*/
		font = new Font(HEADER_FONT,9);
		table.addCell(createCell(new Phrase("\n"+"Amount In Words: Rs."+WordUtils.capitalize(Num2WordConvertor.getInstance().convertNumToWord(gtotal.intValue())),new Font(HEADER_FONT,9)), 4, 0f, -1, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_TOP, null));
		
		PdfPTable sTable = new PdfPTable(new float[] {2,5,3});
		sTable.setWidthPercentage(100f);
		sTable.setSplitLate(false);
		sTable.getDefaultCell().setBorder(0);
		
		for (String text : "VAT(%),Assessable Value,Tax Amt".split(",")) {
			sTable.addCell(createCell(new Phrase(text,font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, BaseColor.LIGHT_GRAY));
		}
		font = new Font(TEXT_FONT,8);
		for (Entry<Float, Double[]> entry : taxMap.entrySet()) {
			sTable.addCell(createCell(new Phrase(Utilities.roundOf2ProperDecimalAndConvert2String(entry.getKey(), 2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			sTable.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(entry.getValue()[0], 2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
			sTable.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(entry.getValue()[1], 2),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		}
		sTable.addCell(createCell(new Phrase("",font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		sTable.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(tnet, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		sTable.addCell(createCell(new Phrase(Utilities.roundOfAndConvert2String(ttax, 2).toString(),font), 1, 0.01f, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, null));
		table.addCell(createCell(sTable, 4, 0f, -1, PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_TOP, null));
		
		try {
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		/*sTable = new PdfPTable(new float[] { 5, 2, 1, 1, 2, 2, 2, 2 });
		sTable.setWidthPercentage(100f);
		sTable.setSplitLate(true);
		sTable.getDefaultCell().setBorder(0);
		font = new Font(TEXT_FONT,7);
		sTable.addCell(createCell(new Phrase("\"I/We hereby certify that my/our registration Certificate under the Maharashtra Value Added Tax Act, 2005 is in force on the date on which the sale of goods specified in this Tax Invoice is made by me/us and that the transaction of sale covered by this Tax Invoice has been effected by me/us. And it shall be accounted for in the turnover of sales while filing of return and the due tax, if any, payable on the sale has been paid or shall be paid.\"",font), 8, 0, -1, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, null));
		sTable.addCell(createCell(new Phrase("VAT TIN: 27880729001 V w.e.f. : 11/10/2009\nCST TIN : 27880729001 C w.e.f. : 11/10/2009",font), 4, 0, -1, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_TOP, null));
		font = new Font(TEXT_FONT,8);
		sTable.addCell(createCell(new Phrase("For Simply Different,\n\n\n\n(Authorised Signature)",font), 4, 0, -1, PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_TOP, null));
		ColumnText ct = new ColumnText(writer.getDirectContent());
		ct.setSimpleColumn(document.left(), 0, document.right(), 135);
		ct.addElement(sTable);
		ct.go();*/

		document.close();
		writer.close();
	}
	
	public static PdfPCell createCell(Phrase phrase, int colspan, float borderwidth, float minHt, int horAlin, int vertAlin, BaseColor color) {
		PdfPCell cell = new PdfPCell(phrase);
		return setCellAttributes(cell, colspan, borderwidth, minHt, horAlin, vertAlin, color);
	}
	
	public static PdfPCell createCell(PdfPTable table, int colspan, float borderwidth, float minHt, int horAlin, int vertAlin, BaseColor color) {
		PdfPCell cell = new PdfPCell(table);
		return setCellAttributes(cell, colspan, borderwidth, minHt, horAlin, vertAlin, color);
	}
	
	private static PdfPCell setCellAttributes(PdfPCell cell, int colspan, float borderwidth, float minHt, int horAlin, int vertAlin, BaseColor color) {
		cell.setColspan(colspan);
		cell.setBorderWidthBottom(borderwidth);
		cell.setBorderWidthLeft(borderwidth);
		cell.setBorderWidthRight(borderwidth);
		cell.setBorderWidthTop(borderwidth);
		cell.setPadding(3);
		if (minHt > -1) {
			cell.setMinimumHeight(minHt);
		}
		cell.setHorizontalAlignment(horAlin);
		cell.setVerticalAlignment(vertAlin);
		cell.setBackgroundColor(color);
		return cell;
	}
	
	class HeaderFooter extends PdfPageEventHelper {
		
		private PdfImportedPage templatePage;
		public HeaderFooter() {
			super();
			// TODO Auto-generated constructor stub
		}

		public HeaderFooter(PdfImportedPage templatePage) {
			super();
			this.templatePage = templatePage;
		}


		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			if (templatePage != null) {
				writer.getDirectContent().addTemplate(templatePage, 0, 0);
			}
			/*PdfPTable table = new PdfPTable(new float[] { 5, 2, 1, 1, 1, 2, 2, 1, 2, 2 });
			table.setWidthPercentage(100f);
			table.setSplitLate(false);
			table.getDefaultCell().setBorder(0);

			Font font = null;
			try {
				font = new Font(HEADER_FONT,18);
				Phrase headerPhrase = new Phrase(), newline = new Phrase("\n",new Font(BaseFont.createFont(BaseFont.TIMES_BOLD,BaseFont.CP1250,true),5));
				headerPhrase.add(new Phrase("Simply Different\n", font));
				headerPhrase.add(newline);
				font = new Font(TEXT_FONT,12);
				headerPhrase.add(new Phrase("Our Ingredients – Your Passion\n", font));
				headerPhrase.add(newline);
				font = new Font(TEXT_FONT,8);
				headerPhrase.add(new Phrase("Shop # 14, Rose Avenue, Opp. Gulmohar Habitat, Salunke Vihar Rd., Pune – 411048\n", font));
				headerPhrase.add(new Phrase("Contact # 9923646404 / 9823005282 / 020-30471711", font));
				table.addCell(createCell(headerPhrase, 10, 0, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BASELINE, null));
				if (document.getPageNumber() == 1) {
					table.addCell(createCell(new Phrase(new Chunk(new LineSeparator(0.1f,100,BaseColor.BLACK,PdfPCell.ALIGN_CENTER,0))), 10, 0, -1, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BASELINE, null));
					document.add(table);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		
        /*public void onEndPage (PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            try {
				ColumnText.showTextAligned(writer.getDirectContent(),
				        Element.ALIGN_CENTER, new Phrase(String.format("Page %d", writer.getPageNumber()), new Font(TEXT_FONT,6)),
				        (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 10, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }*/
    }
}
