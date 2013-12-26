package com.simplydifferent.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.simplydifferent.dao.intr.ReportDAO;
import com.simplydifferent.datasouce.intr.Datasource;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.ProductStockReport;
import com.simplydifferent.vo.PurchaseInvoice;
import com.simplydifferent.vo.PurchaseReport;
import com.simplydifferent.vo.ReportRequest;

public class ReportDAOImpl implements ReportDAO{

	private Datasource datasource;
	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	
	@Override
	public PurchaseReport getPurchaseReport(ReportRequest request) throws Exception {
		String qpart = "";
		List<String> pnames = new ArrayList<String>();
		List<Object> pvals = new ArrayList<Object>();
		if (request.getSupplierId() != null) {
			qpart += "s.id=:SUPPLIERID AND ";
			pnames.add("SUPPLIERID");
			pvals.add(request.getSupplierId());
		}
		if (!StringUtils.isBlank(request.getInvoiceNumber())) {
			qpart += "p1.invoiceNumber=:INVNO AND ";
			pnames.add("INVNO");
			pvals.add(request.getInvoiceNumber());
		}
		if (request.getFromDate() != null) {
			qpart += "p1.invoiceDate>=:FDATE AND ";
			pnames.add("FDATE");
			pvals.add(request.getFromDate());
		}
		if (request.getToDate() != null) {
			qpart += "p1.invoiceDate<=:TDATE AND ";
			pnames.add("TDATE");
			pvals.add(request.getToDate());
		}
		
		PurchaseReport purchaseReport = new PurchaseReport();
		PurchaseInvoice invoice = null;
		for (Object object : datasource.getTemplate().findByNamedParam("SELECT p1, s.supplierName, SUM(p2.purchasePrice*p2.quantity) AS netAmount, SUM(p2.purchasePrice*p2.quantity*p2.vat/100) AS vatAmount FROM PurchaseInvoice p1, PurchaseDetails p2, Supplier s WHERE QPART p2.invoiceId = p1.invoiceId AND p1.supplierId = s.id GROUP BY p1.invoiceId order by p1.invoiceDate".replaceFirst("QPART", qpart), pnames.toArray(new String[pnames.size()]), pvals.toArray())) {
			Object[] objects = (Object[])object;
			invoice = (PurchaseInvoice)objects[0];
			PropertyUtils.setProperty(invoice, "supplierName", objects[1]);
			PropertyUtils.setProperty(invoice, "netAmount", objects[2]);
			PropertyUtils.setProperty(invoice, "vatAmount", objects[3]);
			purchaseReport.getInvoices().add(invoice);
		}
		return purchaseReport;
	}
	
	@Override
	public ProductStockReport getProductStockReport(ReportRequest request) throws Exception {
		String qpart = "";
		List<String> pnames = new ArrayList<String>();
		List<Object> pvals = new ArrayList<Object>();
		if (request.getBrandId() != null) {
			qpart += "b.brandId=:BRANDID AND ";
			pnames.add("BRANDID");
			pvals.add(request.getBrandId());
		}
		if (!StringUtils.isBlank(request.getProductName())) {
			qpart += "p.productName like :PNAME AND ";
			pnames.add("PNAME");
			pvals.add(request.getProductName()+"%");
		}
		
		ProductStockReport report = new ProductStockReport();
		Product product = null;
		for (Object object : datasource.getTemplate().findByNamedParam("SELECT p, p.stock*p.purchasePrice AS stockValue, b.brandName FROM Product p, Brand b  WHERE QPART p.brandId = b.brandId ORDER BY b.brandName, p.productName".replaceFirst("QPART", qpart), pnames.toArray(new String[pnames.size()]), pvals.toArray())) {
			Object[] objects = (Object[])object;
			product = (Product)objects[0];
			PropertyUtils.setProperty(product, "stockValue", objects[1]);
			PropertyUtils.setProperty(product, "brandName", objects[2]);
			report.getProducts().add(product);
		}
		return report;
	}
}
