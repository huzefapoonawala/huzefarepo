package com.dynamicsext.ig.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynamicsext.ig.util.CommonUtil;
import com.dynamicsext.ig.util.InvoiceGeneratorConfigs;
import com.dynamicsext.ig.vo.Customer;
import com.dynamicsext.ig.vo.InvoiceAmounts;
import com.dynamicsext.ig.vo.Item;

import java.awt.Font;

public class InvoicePrintUI extends JFrame implements Printable {

	private static final long serialVersionUID = -5643723661782932561L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoicePrintUI.class);

	private JPanel contentPane;
	private JEditorPane editor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InvoicePrintUI frame = new InvoicePrintUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InvoicePrintUI() {
		CommonUtil.setEscapeImpl(this.getRootPane());
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 679);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		HTMLEditorKit kit=new HTMLEditorKit();
		editor = new JEditorPane();
		editor.setFont(new Font("Tahoma", Font.PLAIN, 9));
		editor.setEditorKit(kit);
		editor.setContentType("text/html");
		//editor.setText(htmlString);
		editor.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(editor);
		contentPane.add(scrollPane);
	}

	public void printInvoice(Customer customer, List<Item> items, InvoiceAmounts ia) {
		this.setVisible(true);
		SimpleDateFormat sd = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
		String cDate = sd.format(new Date());
		StringBuilder invoiceHtml = new StringBuilder();
		invoiceHtml
			.append("<html>")
			.append("<body>")
				.append("<table width=\"100%\">")
						.append("<tr>")
							.append("<td align=\"left\"><h1>").append(InvoiceGeneratorConfigs.getStoreName()).append("</h1></td>")
							.append("<td align=\"right\"><h1>Sales Receipt</h1></td>")
						.append("</tr>")
						.append("<tr>")
							.append("<td style=\"font-size: 8px;\">")
								.append(InvoiceGeneratorConfigs.getStoreAddress()).append("<br>")
								.append(InvoiceGeneratorConfigs.getStoreCity()).append(", ").append(InvoiceGeneratorConfigs.getStoreState()).append(" ").append(InvoiceGeneratorConfigs.getStorePincode()).append("<br>")
								.append(InvoiceGeneratorConfigs.getStorePhone1()).append("<br>")
								.append(InvoiceGeneratorConfigs.getStorePhone2())
							.append("</td>")
							.append("<td style=\"font-size: 9px;\" width=\"34%\">")
								.append("Account #: ").append(customer != null ? StringUtils.defaultIfBlank(customer.getAccountNumber(), "") : "").append("<br>")
								.append("Date: ").append(cDate.substring(0, cDate.indexOf(" "))).append("<br>")
								.append("Time: ").append(cDate.substring(cDate.indexOf(" ")+1)).append("")
							.append("</td>")
						.append("</tr>")
					.append("</table>")
					.append("<table width=\"100%\">")
						.append("<tr style=\"font-size: 9px;\">")
							.append("<td>")
								.append("<table>")
									.append("<tr>")
										.append("<td valign=\"top\">Bill To:</td>")
										.append("<td>");
										if (customer != null  && StringUtils.isNotBlank(customer.getAccountNumber())) {
											invoiceHtml.append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("<br>")
											.append(customer.getCompany()).append("<br>")
											.append(customer.getAddress()).append(StringUtils.isNotBlank(customer.getAddress2()) ? ", ": "").append(StringUtils.defaultIfBlank(customer.getAddress2(), "")).append("<br>")
											.append(customer.getCity()).append(", ").append(customer.getState()).append(" ").append(customer.getZip()).append("<br>")
											/*.append("718-738-1804")*/;
										}
										invoiceHtml.append("</td>")
									.append("</tr>")
								.append("</table>")
							.append("</td>")
							.append("<td align=\"center\">")
								.append("<table>")
									.append("<tr>")
										.append("<td valign=\"top\">Ship To:</td>")
										.append("<td>");
										if (customer != null && StringUtils.isNotBlank(customer.getAccountNumber())) {
											invoiceHtml.append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("<br>")
											.append(customer.getCompany()).append("<br>")
											.append(customer.getAddress()).append(StringUtils.isNotBlank(customer.getAddress2()) ? ", ": "").append(StringUtils.defaultIfBlank(customer.getAddress2(), "")).append("<br>")
											.append(customer.getCity()).append(", ").append(customer.getState()).append(" ").append(customer.getZip()).append("<br>")
											/*.append("718-738-1804")*/;
										}
										invoiceHtml.append("</td>")
									.append("</tr>")
								.append("</table>")
							.append("</td>")
						.append("</tr>")
				.append("</table>")
				.append("<table style=\"width: 100%; min-height: 400px; font-size: 9px;\">")
					.append("<tr style=\"background-color: yellow;\">")
						.append("<th width=\"23%\" align=\"left\">Item Lookup Code</th>")
						.append("<th width=\"37%\" align=\"left\">Description</th>")
						.append("<th width=\"10%\">Quantity</th>")
						.append("<th width=\"15%\">Price</th>")
						.append("<th width=\"15%\">Extended</th>")
					.append("</tr>");
				for (Item i : items) {
					invoiceHtml.append("<tr>")
						.append("<td>").append(i.getItemLookupCode()).append("</td>")
						.append("<td>").append(i.getDescription()).append("</td>")
						.append("<td align=\"center\">").append(i.getQuantity()).append("</td>")
						.append("<td align=\"center\">").append(ia.getAmountWithCurency(i.getPrice())).append("</td>")
						.append("<td align=\"center\">").append(ia.getAmountWithCurency(i.getTotalPrice())).append("</td>")
					.append("</tr>");
				}
				invoiceHtml
					.append("<tr><td colspan=\"5\" style=\"background-color: yellow;\">&nbsp;</td></tr>")
				.append("</table>")
				.append("<table width=\"100%\" style=\"font-size: 9px;\">")
					.append("<tr>")
						.append("<td valign=\"top\">")
							.append("<table>")
								.append("<tr>")
									.append("<td align=\"center\" style=\"border: 1px solid black;\">")
										.append("Thank you for shopping<br>")
										.append(InvoiceGeneratorConfigs.getStoreName()).append("<br>")
										.append("Please come again!");
										if (StringUtils.isNotBlank(InvoiceGeneratorConfigs.getInvoiceNotes())) {
											invoiceHtml.append("<br>").append(InvoiceGeneratorConfigs.getInvoiceNotes());
										}
									invoiceHtml.append("</td>")
								.append("</tr>")
							.append("</table>")
						.append("</td>")
						.append("<td width=\"70%\" align=\"right\">")
							.append("<table>")
								.append("<tr>")
									.append("<td align=\"right\">Sub Total</td>")
									.append("<td width=\"30px\">&nbsp;</td>")
									.append("<td>").append(ia.getAmountWithCurency(ia.getSubTotalAmount())).append("</td>")
								.append("</tr>")
								.append("<tr>")
									.append("<td align=\"right\">Sales Tax</td>")
									.append("<td width=\"30px\">&nbsp;</td>")
									.append("<td>").append(ia.getAmountWithCurency(ia.getTaxAmount())).append("</td>")
								.append("</tr>")
								.append("<tr>")
									.append("<td align=\"right\">Total</td>")
									.append("<td width=\"30px\">&nbsp;</td>")
									.append("<td>").append(ia.getAmountWithCurency(ia.getGrandTotalAmount())).append("</td>")
								.append("</tr>")
								.append("<tr style=\"font-size: 6px;\"><td>&nbsp;</td></tr>")
								.append("<tr>")
									.append("<td align=\"right\">Cash Tendered</td>")
									.append("<td width=\"30px\">&nbsp;</td>")
									.append("<td>").append(ia.getAmountWithCurency(ia.getTender().getTenderAmount())).append("</td>")
								.append("</tr>")
								.append("<tr>")
									.append("<td align=\"right\">Change Due</td>")
									.append("<td width=\"30px\">&nbsp;</td>")
									.append("<td>").append(ia.getAmountWithCurency(ia.getTender().getBalanceAmount())).append("</td>")
								.append("</tr>")
							.append("</table>")
						.append("</td>")
					.append("</tr>")
				.append("</table>")
			.append("</body>")
			.append("</html>");
		editor.setText(invoiceHtml.toString());
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				LOGGER.error("Error occurred while printing receipt.", ex);
			}
		}
		this.setVisible(false);
	}

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}
		
		//pf.setOrientation(PageFormat.LANDSCAPE);

		/* User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		//g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		
		AffineTransform affTrans = new AffineTransform();
		affTrans.setToScale(6, 6);
		
		g2d.setTransform(affTrans);

		/* Now print the window and its visible contents */
		editor.printAll(g);

		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}
}
