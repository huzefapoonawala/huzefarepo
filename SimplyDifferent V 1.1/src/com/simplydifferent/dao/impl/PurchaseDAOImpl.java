package com.simplydifferent.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.dao.intr.PurchaseDAO;
import com.simplydifferent.datasouce.intr.Datasource;
import com.simplydifferent.util.BeanUtil;
import com.simplydifferent.util.DateUtil;
import com.simplydifferent.vo.PurchaseDetails;
import com.simplydifferent.vo.PurchaseInvoice;
import com.simplydifferent.vo.Supplier;

public class PurchaseDAOImpl implements PurchaseDAO {

	private Datasource datasource;
	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	
	private MasterDAO masterDAO;	
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveInvoice(PurchaseInvoice invoice) throws Exception {
		invoice.setCreationDate(new Date());
		datasource.save(invoice);
		JSONArray details = (JSONArray)JSONValue.parse(invoice.getInvoiceDetails());
		for (Object object : details) {
			saveInvoiceDetails(invoice, (JSONObject)object);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void saveInvoiceDetails(PurchaseInvoice invoice, JSONObject jObject) throws Exception {
		PurchaseDetails pDetails = new PurchaseDetails();
		pDetails.setProductId(Integer.valueOf(jObject.get("productId").toString()));
		pDetails.setBatchNumber(jObject.get("batchNumber").toString());
		pDetails.setExpiryDate(DateUtil.convertString2Date(jObject.get("expiryDate").toString(), "dd-MMM-yyyy"));
		pDetails.setPurchasePrice(Float.valueOf(jObject.get("purchasePrice").toString()));
		pDetails.setVat(Float.valueOf(jObject.get("vat").toString()));
		pDetails.setQuantity(Float.valueOf(jObject.get("quantity").toString()));
		pDetails.setInvoiceId(invoice.getInvoiceId());
		datasource.save(pDetails);
		masterDAO.updateProductStock(pDetails.getProductId(), Double.valueOf(pDetails.getQuantity()), true);
		masterDAO.updateProductBatchStock(pDetails.getProductId(), pDetails.getBatchNumber(), pDetails.getExpiryDate(), Double.valueOf(pDetails.getQuantity()), true);
	}
	
	@Override
	public List<PurchaseInvoice> getPurchaseInvoices() {
		List<PurchaseInvoice> list = new ArrayList<PurchaseInvoice>();
		PurchaseInvoice tmp = null;
		for (Object object : datasource.getTemplate().find("from PurchaseInvoice p, Supplier s where p.supplierId = s.id order by p.invoiceNumber")) {
			Object[] objects = (Object[])object;
			tmp = (PurchaseInvoice)objects[0];
			tmp.setSupplierName(((Supplier)objects[1]).getSupplierName());
			list.add(tmp);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseInvoice> getPurchaseInvoicesBySupplier(Supplier supplier) {
		return datasource.getTemplate().find("from PurchaseInvoice p where p.supplierId = ? order by p.invoiceNumber",supplier.getId());
	}
	
	@Override
	public boolean isInvoiceNumberValid(PurchaseInvoice invoice) {
		return datasource.getTemplate().find("from PurchaseInvoice where invoiceNumber = ? and supplierId = ?",invoice.getInvoiceNumber(),invoice.getSupplierId()).isEmpty();
	}
	
	@Override
	public List<PurchaseDetails> getInvoiceDetails(PurchaseInvoice invoice) {
		List<PurchaseDetails> list = new ArrayList<PurchaseDetails>();
		for (Object object : datasource.getTemplate().find("from PurchaseDetails pd, Product p, Brand b where pd.invoiceId = ? and pd.productId = p.id and p.brandId = b.brandId order by p.id", invoice.getInvoiceId())) {
			Object[] objects = (Object[])object;
			try {
				for (Object[] arr : new Object[][]{{"brandId",objects[2]},{"brandName",objects[2]},{"productName",objects[1]},{"unitType",objects[1]}}) {
					BeanUtil.copyPropertyValue(arr[1], objects[0], arr[0].toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add((PurchaseDetails)objects[0]);
		}
		return list;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void editInvoice(PurchaseInvoice invoice) throws Exception {
		PurchaseInvoice invoiceMaster = datasource.getTemplate().get(PurchaseInvoice.class, invoice.getInvoiceId());
		BeanUtil.copyPropertyValue(invoice, invoiceMaster, "invoiceDate");
		BeanUtil.copyPropertyValue(invoice, invoiceMaster, "totalAmount");
		datasource.update(invoiceMaster);
		JSONArray details = (JSONArray)JSONValue.parse(invoice.getInvoiceDetails());
		JSONObject jObject = null;
		for (Object object : details) {
			jObject = (JSONObject)object;
			if (jObject.containsKey("isNew") && Boolean.valueOf(jObject.get("isNew").toString())) {
				saveInvoiceDetails(invoice, jObject);
			}
		}
	}
}
