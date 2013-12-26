package com.simplydifferent.dao.impl;


import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.dao.intr.SalesDAO;
import com.simplydifferent.datasouce.intr.Datasource;
import com.simplydifferent.util.BeanUtil;
import com.simplydifferent.util.DateUtil;
import com.simplydifferent.util.PdfGenerator;
import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.NextSalesInvoiceNumber;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.SalesDetails;
import com.simplydifferent.vo.SalesInvoice;
import com.simplydifferent.vo.SalesInvoicePdfVO;
import com.simplydifferent.vo.SalesReceipt;

public class SalesDAOImpl implements SalesDAO {

	private Datasource datasource;
	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	
	private MasterDAO masterDAO;	
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
	
	private PdfGenerator pdfGenerator;	
	public void setPdfGenerator(PdfGenerator pdfGenerator) {
		this.pdfGenerator = pdfGenerator;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public SalesInvoice saveInvoice(SalesInvoice invoice) throws Exception {
		if (!StringUtils.isEmpty(invoice.getCustomInvoiceNumber())) {
			invoice.setInvoiceNumber(invoice.getCustomInvoiceNumber());
		}
		else{
			NextSalesInvoiceNumber nextSalesInvoiceNumber = datasource.getTemplate().get(NextSalesInvoiceNumber.class, "next_sales_invoice_number");
			nextSalesInvoiceNumber.setNextInvoiceNumber(nextSalesInvoiceNumber.getNextInvoiceNumber()+1);
			datasource.getHibernateSession().update(nextSalesInvoiceNumber);
		}
		invoice.setCreationDate(new Date());
		datasource.save(invoice);
		JSONArray details = (JSONArray)JSONValue.parse(invoice.getInvoiceDetails());
		for (Object object : details) {
			saveInvoiceDetails(invoice, (JSONObject)object);
		}
		return invoice;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void saveInvoiceDetails(SalesInvoice invoice, JSONObject jObject) throws Exception {
		SalesDetails sDetails = new SalesDetails();
		sDetails.setProductId(Integer.valueOf(jObject.get("productId").toString()));
		sDetails.setBatchNumber(jObject.get("batchNumber").toString());
		sDetails.setSalesPrice(Float.valueOf(jObject.get("salesPrice").toString()));
		sDetails.setVat(Float.valueOf(jObject.get("vat").toString()));
		sDetails.setQuantity(Float.valueOf(jObject.get("quantity").toString()));
		sDetails.setExpiryDate(DateUtil.convertString2Date(jObject.get("expiryDate").toString(), "dd-MMM-yyyy"));
		sDetails.setAdditionalDetails((String)jObject.get("additionalDetails"));
		sDetails.setInvoiceId(invoice.getInvoiceId());
		datasource.save(sDetails);
		masterDAO.updateProductStock(sDetails.getProductId(), Double.valueOf(sDetails.getQuantity()), false);
		masterDAO.updateProductBatchStock(sDetails.getProductId(), sDetails.getBatchNumber(), sDetails.getExpiryDate(), Double.valueOf(sDetails.getQuantity()), false);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void deleteInvoiceDetails(JSONObject jObject) throws Exception {
		SalesDetails sDetails = datasource.getTemplate().get(SalesDetails.class, Integer.valueOf(jObject.get("id").toString()));
		datasource.delete(sDetails);
		masterDAO.updateProductStock(sDetails.getProductId(), Double.valueOf(sDetails.getQuantity()), true);
		masterDAO.updateProductBatchStock(sDetails.getProductId(), sDetails.getBatchNumber(), sDetails.getExpiryDate(), Double.valueOf(sDetails.getQuantity()), true);
	}
	
	@Override
	public boolean isInvoiceNumberValid(SalesInvoice invoice) {
		return datasource.getTemplate().find("from SalesInvoice where invoiceNumber = ?",invoice.getInvoiceNumber()).isEmpty();
	}
	
	@Override
	public int getNextInvoiceNumber() {
		return datasource.getTemplate().get(NextSalesInvoiceNumber.class, "next_sales_invoice_number").getNextInvoiceNumber();
	}
	
	@Override
	public void generateInvoicePdf(SalesInvoice invoice, OutputStream out) throws Exception {
		SalesInvoicePdfVO pdfVO = new SalesInvoicePdfVO();
		Object[] objects = (Object[]) datasource.getTemplate().find("from SalesInvoice s, Customer c where s.invoiceId = ? and c.id = s.customerId",invoice.getInvoiceId()).get(0);
		pdfVO.setInvoice((SalesInvoice)objects[0]);
		pdfVO.setCustomer((Customer)objects[1]);
		pdfVO.setDetails(new ArrayList<SalesDetails>());
		int idx = -1; 
		for (SalesDetails sd : getInvoiceDetails(invoice)) {
			idx = pdfVO.getDetails().indexOf(sd);
			if (idx == -1) {
				pdfVO.getDetails().add(sd);
			}
			else{
				pdfVO.getDetails().get(idx).setQuantity(pdfVO.getDetails().get(idx).getQuantity()+sd.getQuantity());
			}
		}
		pdfGenerator.generateSalesInvoicePdf(pdfVO, out);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesInvoice> getInvoicesByCustomer(SalesInvoice invoice) {
		return datasource.getTemplate().find("from SalesInvoice s where s.customerId = ? order by s.invoiceNumber",invoice.getCustomerId());
	}
	
	@Override
	public List<SalesDetails> getInvoiceDetails(SalesInvoice invoice) throws Exception {
		List<SalesDetails> list = new ArrayList<SalesDetails>();
		SalesDetails sd = null;
		for (Object object : datasource.getTemplate().find("from SalesDetails s, Product p, Brand b where s.invoiceId = ? and s.productId = p.id and p.brandId = b.brandId order by s.id",invoice.getInvoiceId())) {
			Object[] objs = (Object[])object;
			sd = (SalesDetails)objs[0];
			BeanUtil.copyPropertyValue(objs[1], sd, "productName");
			BeanUtil.copyPropertyValue(objs[1], sd, "unitType");
			BeanUtil.copyPropertyValue(objs[2], sd, "brandName");
			list.add(sd);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SalesDetails getSalesDetailsForProductByCustomer(final Customer customer, final Product product) {
		SalesDetails details = null;
		details = datasource.getTemplate().execute(new HibernateCallback<SalesDetails>() {
			@Override
			public SalesDetails doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("select s1 from SalesDetails s1, SalesInvoice s2 where s2.customerId = ? and s1.invoiceId = s2.invoiceId and s1.productId = ? order by s2.invoiceDate desc, s2.creationDate desc").setInteger(0, customer.getId()).setInteger(1, product.getId());
				query.setFirstResult(0);
				query.setMaxResults(1);
				List<Object> list = query.list();
				return list.size() > 0 ? (SalesDetails)list.get(0) : null;
			}
		});
		return details;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void editInvoice(SalesInvoice invoice) throws Exception {
		SalesInvoice salesInvoice = datasource.getTemplate().get(SalesInvoice.class, invoice.getInvoiceId());
		BeanUtil.copyPropertyValue(invoice, salesInvoice, "invoiceDate");
		BeanUtil.copyPropertyValue(invoice, salesInvoice, "totalAmount");
		BeanUtil.copyPropertyValue(invoice, salesInvoice, "poNumber");
		datasource.update(salesInvoice);
		JSONArray details = (JSONArray)JSONValue.parse(invoice.getInvoiceDetails());
		JSONObject jObject = null;
		for (Object object : details) {
			jObject = (JSONObject)object;
			if (jObject.containsKey("isNew") && Boolean.valueOf(jObject.get("isNew").toString())) {
				saveInvoiceDetails(invoice, jObject);
			}
			else if (jObject.containsKey("isDelete") && Boolean.valueOf(jObject.get("isDelete").toString())) {
				deleteInvoiceDetails(jObject);
			}
		}
	}
	
	@Override
	public List<SalesInvoice> getInvoicesForSalesReceipt(SalesInvoice invoice) {
		/*DetachedCriteria criteria = DetachedCriteria.forClass(SalesInvoice.class)
									.add(Restrictions.eq("customerId", invoice.getCustomerId()))
									.add(Restrictions.gt("balanceAmount", 0d));
		if (invoice.getInvoiceId() != null) {
			criteria.add(Restrictions.eq("invoiceId", invoice.getInvoiceId()));
		}
		return datasource.getTemplate().findByCriteria(criteria);*/
		List<SalesInvoice> list = new ArrayList<SalesInvoice>();
		for (Object obj : datasource.getTemplate().find("select s, c.name from SalesInvoice s, Customer c where c.id = s.customerId and (? is null or s.customerId = ?) and (? is null or s.invoiceId = ?) and s.balanceAmount > 0 order by c.name, s.invoiceDate", invoice.getCustomerId(), invoice.getCustomerId(), invoice.getInvoiceId(), invoice.getInvoiceId())) {
			Object[] objs = (Object[])obj;
			SalesInvoice si = (SalesInvoice)objs[0];
			si.setCustomerName((String)objs[1]);
			list.add(si);
		}
		return list;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void createSalesReceipt(SalesReceipt sr) {
		sr.setCreationDate(new Date());
		datasource.save(sr);
		SalesInvoice invoice = (SalesInvoice)datasource.getTemplate().find("from SalesInvoice where invoiceId = ?", sr.getInvoiceId()).get(0);
		invoice.setReceivedAmount(invoice.getReceivedAmount()+sr.getReceivedAmount());
		datasource.update(invoice);
	}
}
