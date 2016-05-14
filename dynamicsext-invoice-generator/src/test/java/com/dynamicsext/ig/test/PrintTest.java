package com.dynamicsext.ig.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.UIManager;

import com.dynamicsext.ig.gui.InvoicePrintUI;
import com.dynamicsext.ig.util.InvoiceGeneratorConfigs;
import com.dynamicsext.ig.vo.Customer;
import com.dynamicsext.ig.vo.InvoiceAmounts;
import com.dynamicsext.ig.vo.Item;
import com.dynamicsext.ig.vo.Tender;

public class PrintTest implements Printable {

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		/* User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		/* Now we perform our rendering */
		g.drawString("Test the print dialog!", 100, 100);

		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}

	public static void main(String args[]) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("D:/Workspace/dynamicsext-invoice-generator/src/main/resources/ig-configs.properties"));
		InvoiceGeneratorConfigs.setConfigs(properties);
		InvoicePrintUI invoicePrinter = new InvoicePrintUI();
		invoicePrinter.setVisible(true);
		List<Item> items = new ArrayList<Item>();
		for (int idx = 0; idx < 30; idx++) {
			Item i = new Item();
			i.setDescription("Test item "+(idx+1));
			i.setItemLookupCode("ABC1234567890");
			i.setPrice(23.4454);
			i.setQuantity(20);
			i.setTotalPrice(2334.2222);
			items.add(i);
		}
		InvoiceAmounts ia = new InvoiceAmounts();
		ia.setGrandTotalAmount(2334.33333);
		ia.setSubTotalAmount(2330);
		ia.setTaxAmount(4.33333);
		ia.setTender(new Tender());
		ia.getTender().setBalanceAmount(-20.0);
		ia.getTender().setTenderAmount(2340.0);
		Customer c = new Customer();
		c.setAccountNumber("ABC1234");
		c.setFirstName("Huzefa");
		c.setLastName("Poonawala");
		c.setCompany("Empire Hardware & Supply");
		c.setAddress("112-18 LIberty Ave");
		c.setAddress2(null);
		c.setCity("South Richmond Hill");
		c.setState("NY");
		c.setZip("11419");
		invoicePrinter.printInvoice(c,items,ia);
		/*try {
			String cn = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(cn); // Use the native L&F
		} catch (Exception cnf) {
		}
		PrinterJob job = PrinterJob.getPrinterJob();
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PageFormat pf = job.pageDialog(aset);
		job.setPrintable(new PrintTest(), pf);
		boolean ok = job.printDialog(aset);
		if (ok) {
			try {
				job.print(aset);
			} catch (PrinterException ex) {
				 The job did not successfully complete 
			}
		}
		System.exit(0);*/
	}
}
