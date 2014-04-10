package test;

import java.io.FileOutputStream;

import javax.annotation.Resource;

import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jh.util.ItemLabelGenerator;
import com.jh.vo.Item;

public class LabelTest extends Setup{
	
	@Resource private ItemLabelGenerator itemLabelGenerator;
	
	@Test
	public void testLabel() throws Exception{
		FileOutputStream out = new FileOutputStream("C:/Users/hpoonaw/Desktop/dy-label.pdf");
		Item item = new Item();
		item.setSku("0071231");
		item.setRetailPrice(230.23f);
		item.setDescription("NEUTRA AIR SPR CITRUS");
		item.setBinLocation("12,80F212,1234");
		item.setAliases("121121121121");
		itemLabelGenerator.generateLabel(item, out);
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
			table.addCell(cell);
			
			Barcode39 barcode = new Barcode39();
			barcode.setCode("12345678901222");
			barcode.setStartStopText(false);
			barcode.setBarHeight(23f);
			barcode.setExtended(true);
			barcode.setFont(null);
			Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
			cell = new PdfPCell(barcodeImage);
			cell.setColspan(4);
			cell.setMinimumHeight(28);
			cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
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
}
