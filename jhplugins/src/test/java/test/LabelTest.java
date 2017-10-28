package test;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BarcodeDatamatrix;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jh.util.ItemLabelGenerator;
import com.jh.util.PropertiesUtil;
import com.jh.vo.Item;

public class LabelTest extends Setup{
	
	@Resource private ItemLabelGenerator itemLabelGenerator;
	@Resource private PropertiesUtil propertiesUtil;	
	
	private Item item;
	
	@Before
	public void setUp() {
		item = new Item();
		item.setSku("0071231");
		item.setRetailPrice(2100.99f);
		item.setDescription("Garbage Can SQR Wheeled111");
		item.setBinLocation("123,80F2122,1234");
		item.setAliases("121121121121");
	}
	
	@Test
	public void testLabel() throws Exception{
		FileOutputStream out = new FileOutputStream("./samples/dy-label.pdf");
		itemLabelGenerator.generateLabel(item, out);
		/*Barcode39 barcode = new Barcode39();
		barcode.setCode(StringUtils.defaultIfBlank(item.getAliases(), item.getSku()));
		barcode.setBarHeight(27f);
		barcode.setFont(null);
		barcode.setX(2f);
		Image img = barcode.createAwtImage(Color.BLACK, Color.WHITE);		
		ImageIO.write(toBufferedImage(img), "png", new File("C:/Users/hpoonaw/Desktop", "bimg.png"));*/
	}
	
	 static BufferedImage toBufferedImage(Image image) {
         if (image instanceof BufferedImage) {
             // Return image unchanged if it is already a BufferedImage.
             return (BufferedImage) image;
         }
         
         // Ensure image is loaded.
         image = new ImageIcon(image).getImage();        
         
         int type = hasAlpha(image) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
         BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
         Graphics g = bufferedImage.createGraphics();
         g.drawImage(image, 0, 0, null);
         g.dispose();
         
         return bufferedImage;
     }
	 
	 static boolean hasAlpha(Image image) {
         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
         try {
             pg.grabPixels();
         } catch (InterruptedException ex) { }
         return pg.getColorModel().hasAlpha();
     }

	public static void main(String[] args) {
		Rectangle pageSize = new Rectangle(192, 108);
		Document document = new Document(pageSize, 4.8f, 4.8f, 9.6f, 0);
		try {
			FileOutputStream out = new FileOutputStream("C:/Users/hpoonaw/Desktop/dy-label.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			BaseFont arialBold = null, arial = null;
			try {
				arialBold = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
//				arial = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
			} catch (Exception e) {
				// TODO: handle exception
			}
			Font arialBoldFont10 = new Font(arialBold, 10), arialBoldFont12 = new Font(arialBold, 12);
			
			Paragraph text = new Paragraph("NEUTRA AIR SPR CITRUS", arialBoldFont10);
			PdfPTable table = new PdfPTable(new float[] {5, 3, 6, 1});
			table.setWidthPercentage(100f);
			table.setSplitLate(false);
			table.getDefaultCell().setBorder(0);
			PdfPCell cell = null;
			
			/*cell = new PdfPCell(text);
			cell.setColspan(4);
			cell.setBorderWidthBottom(0.1f);
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthRight(0);
			cell.setBorderWidthTop(0);
			cell.setPaddingBottom(5);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Jamaica HDW", arialBoldFont12));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			cell.setColspan(2);
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("$9000.99", new Font(arialBold, 18)));
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
			
			
			cell = new PdfPCell(new Paragraph("1234567", arialBoldFont12));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			cell.setColspan(2);
			cell.setBorder(0);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("12,80F12,1234", new Font(arial, 9)));
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
			table.addCell(cell);*/
			
			String barText = "123456789011";
			float x = 0.9f;
			
			Barcode barcode = new Barcode39();
			barcode.setCode(barText);
			barcode.setBarHeight(26f);
			barcode.setExtended(false);
			barcode.setFont(null);
			barcode.setX(x);
			com.itextpdf.text.Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
//			barcodeImage.scaleAbsolute(183, 26);
			cell = new PdfPCell(barcodeImage);
			cell.setColspan(4);
			cell.setMinimumHeight(28);
			cell.setBorder(0);
			table.addCell(cell);
			
			barcode = new Barcode128();
			barcode.setCode(barText);
			barcode.setBarHeight(26f);
			barcode.setFont(null);
			barcode.setX(x);
			barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
			barcodeImage.scaleAbsolute(190, 28);
			cell = new PdfPCell(barcodeImage);
			cell.setColspan(4);
			cell.setMinimumHeight(28);
			cell.setBorder(0);
//			table.addCell(cell);
			
			BarcodeDatamatrix datamatrix = new BarcodeDatamatrix();
			datamatrix.generate(barText);
			barcodeImage = datamatrix.createImage();
			cell = new PdfPCell(barcodeImage);
			cell.setColspan(4);
			cell.setMinimumHeight(28);
			cell.setBorder(0);
			table.addCell(cell);
			
			document.add(table);
			document.close();
			writer.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testPrinter() throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.print();
	}
	
	@Test
	public void testLabelImage() throws IOException {
		BufferedImage img = new BufferedImage(194, 110, BufferedImage.TYPE_INT_RGB);
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
		s = "$"+item.getRetailPrice();
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
		s = item.getBinLocation();
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
		barcode.setBarHeight(25f);
		barcode.setFont(null);
		barcode.setX(1f);
		Image barcodeImg = barcode.createAwtImage(Color.BLACK, Color.WHITE);	
		g2d.drawImage(barcodeImg, 3, y, null);
		
		g2d.dispose();
		ImageIO.write(img, "bmp", new File("./samples", "bimg.bmp"));
	}
}
