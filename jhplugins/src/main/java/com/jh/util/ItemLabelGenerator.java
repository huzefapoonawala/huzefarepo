package com.jh.util;


import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jh.vo.Item;

public class ItemLabelGenerator {
	
	private static Logger logger = Logger.getLogger(ItemLabelGenerator.class);
	
	private PropertiesUtil propertiesUtil;	
	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}

	public void generateLabel(Item item, OutputStream out) throws Exception {
		Rectangle pageSize = new Rectangle(194, 110);
		Document document = new Document(pageSize, 0, 0, 0, 0);
		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();
		BaseFont arialBold = null, arial = null;
		try {
			arialBold = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
		} catch (Exception e) {
			logger.warn("Unable to locate font Arial Bold on path c:/windows/fonts/arialbd.ttf");
		}
		try {
			arial = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
		} catch (Exception e) {
			logger.warn("Unable to locate font Arial on path c:/windows/fonts/arial.ttf");
		}
		Font arialBoldFont12 = new Font(arialBold, 12);
		
		Paragraph text = new Paragraph(StringUtils.defaultIfBlank((item.getDescription().length() > 24 ? item.getDescription().substring(0, 24) : item.getDescription()).toUpperCase(), ""), new Font(arialBold, 12));
		PdfPTable table = new PdfPTable(new float[] {5, 3, 6, 1});
		table.setWidthPercentage(100f);
		table.setSplitLate(false);
		table.getDefaultCell().setBorder(0);
		PdfPCell cell = new PdfPCell(text);
		cell.setColspan(4);
		cell.setBorderWidthBottom(0.1f);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthTop(0);
		cell.setPaddingBottom(5);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph(StringUtils.defaultIfBlank(propertiesUtil.getApplicationProperties().getProperty(PropertiesUtil.PRODUCTLABEL_STORENAME), ""), new Font(arialBold, 14)));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph(StringUtils.join(new Object[]{"$",item.getRetailPrice()}), new Font(arialBold, 20)));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("", arialBoldFont12));
		cell.setColspan(4);
		cell.setMinimumHeight(13);
		cell.setBorder(0);
		table.addCell(cell);
		
		
		cell = new PdfPCell(new Paragraph(item.getSku(), new Font(arialBold, 17)));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph(StringUtils.defaultIfBlank(item.getBinLocation(), ""), new Font(arial, 10)));
		cell.setColspan(1);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setMinimumHeight(3);
		cell.setBorderWidth(0.1f);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph(""));
		cell.setColspan(1);
		cell.setMinimumHeight(3);
		cell.setBorder(0);
		table.addCell(cell);
		
		Barcode39 barcode = new Barcode39();
		barcode.setCode(StringUtils.defaultIfBlank(item.getAliases(), item.getSku()));
		barcode.setBarHeight(27f);
		barcode.setFont(null);
		barcode.setX(0.9f);
		
		/*Barcode128 barcode = new Barcode128();
		barcode.setCode(StringUtils.defaultIfBlank(item.getAliases(), item.getSku()));
		barcode.setBarHeight(26f);
		barcode.setFont(null);
		barcode.setX(1f);*/
		
		Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
		cell = new PdfPCell(barcodeImage);
		cell.setColspan(4);
		cell.setMinimumHeight(29);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorder(0);
		table.addCell(cell);
		
		document.add(table);
		document.close();
		writer.close();
		out.close();
	}

	public void generateLabelImage(Item item, OutputStream out) throws Exception {
		BufferedImage img = new BufferedImage(194, 110, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setPaint (Color.WHITE);
		g2d.fillRect (0, 0, img.getWidth(), img.getHeight());

		g2d.setPaint(Color.BLACK);

		String fontName = "Arial Bold";
		java.awt.Font arial12 = new java.awt.Font(fontName, Font.BOLD, 12);

		FontMetrics fm = g2d.getFontMetrics(arial12);
//		Description
		g2d.setFont(arial12);
		String s = StringUtils.defaultIfBlank((item.getDescription().length() > 24 ? item.getDescription().substring(0, 24) : item.getDescription()).toUpperCase(), "").toUpperCase(); 
		int x = (img.getWidth() - fm.stringWidth(s))/2;
		int y = fm.getHeight();
		g2d.drawString(s, x, y);

		y += 7;
		g2d.drawLine(2, y, img.getWidth()-2, y);

//		Store Name
		y += 25;
		java.awt.Font font = new java.awt.Font(fontName, Font.BOLD, 14);
		g2d.setFont(font);
		s = propertiesUtil.getApplicationProperties().getProperty(PropertiesUtil.PRODUCTLABEL_STORENAME);
		g2d.drawString(s, 2, y);

//		Cost Price
		font = new java.awt.Font(fontName, Font.BOLD, 20);
		s = StringUtils.join(new Object[]{"$",item.getRetailPrice()});
		fm = g2d.getFontMetrics(font);
		x = img.getWidth() - 2 - fm.stringWidth(s);
		if (x < 105) {
			x = 105;
		}
		g2d.setFont(font);
		g2d.drawString(s, x, y);
		
//		SKU
		y += 30;
		font = new java.awt.Font(fontName, Font.BOLD, 17);
		fm = g2d.getFontMetrics(font);
		s = item.getSku();
		x = (105 - fm.stringWidth(s))/2;
		g2d.setFont(font);
		g2d.drawString(s, x, y);
		
		g2d.drawRect(102, y-20, 90, 20);
		
//		Bin Location
		s = StringUtils.defaultIfBlank(item.getBinLocation(), "");
		font = new java.awt.Font(fontName, Font.NORMAL, 10);
		fm = g2d.getFontMetrics(font);
		x = (88 - fm.stringWidth(s)) / 2;
		if (x > 0) {
			x += 103;
		}
		else{
			x = 103;
		}
		g2d.setFont(font);
		g2d.drawString(s, x, y-5);
		
//		Barcode
		y += 5;
		Barcode39 barcode = new Barcode39();
		barcode.setCode(StringUtils.defaultIfBlank(item.getAliases(), item.getSku()));
		barcode.setBarHeight(26f);
		barcode.setFont(null);
		barcode.setX(1f);
		java.awt.Image barcodeImg = barcode.createAwtImage(Color.BLACK, Color.WHITE);	
		g2d.drawImage(barcodeImg, 3, y, null);
		
		g2d.dispose();
		ImageIO.write(img, "png", out);
	}
}
