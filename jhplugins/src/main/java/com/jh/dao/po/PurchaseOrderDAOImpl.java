package com.jh.dao.po;

import java.io.Writer;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import au.com.bytecode.opencsv.CSVWriter;

import com.jh.dao.pi.PurchaseInvoiceDAO;
import com.jh.util.CommonUtil;
import com.jh.util.DateUtil;
import com.jh.vo.PurchaseOrderDetails;
import com.jh.vo.RequestVO;

public class PurchaseOrderDAOImpl extends JdbcDaoSupport implements PurchaseOrderDAO {

	private static Logger logger = Logger.getLogger(PurchaseOrderDAOImpl.class);
	
	private CommonUtil commonUtil;	
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	private PurchaseInvoiceDAO purchaseInvoiceDAO;	
	public void setPurchaseInvoiceDAO(PurchaseInvoiceDAO purchaseInvoiceDAO) {
		this.purchaseInvoiceDAO = purchaseInvoiceDAO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseOrderDetails> getDetailsForPurchaseOrder(RequestVO request) {
		logger.debug("Request params = "+request.toString());
		List<PurchaseOrderDetails> list = null;
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("GetDetailsForPO")
				.declareParameters(
					new SqlParameter("supplierId", Types.INTEGER), 
					new SqlParameter("fromDate1",Types.TIMESTAMP), 
					new SqlParameter("toDate1",Types.TIMESTAMP),
					new SqlParameter("fromDate2",Types.TIMESTAMP), 
					new SqlParameter("toDate2",Types.TIMESTAMP)
				)
				.returningResultSet("poDetails", new BeanPropertyRowMapper<PurchaseOrderDetails>(PurchaseOrderDetails.class));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("supplierId", request.getSupplierId());
		params.put("fromDate1", request.getFromDate1());
		params.put("toDate1", request.getToDate1());
		params.put("fromDate2", request.getFromDate2());
		params.put("toDate2", request.getToDate2());
		Map<String, Object> map = jdbcCall.execute(params);
		list = (List<PurchaseOrderDetails>)map.get("poDetails");
		for (PurchaseOrderDetails po : list) {
			po.setTotalOrderCost(commonUtil.roundOf(Double.valueOf(po.getCostPrice())*po.getOrderQuantity(), 2));
			if (po.getImage() == null) {
				po.setImage(commonUtil.getImageUrl(po.getSku()+".jpg"));
			}
			else{
				po.setImage(commonUtil.getImageUrl(po.getImage()));
			}
		}
		return list;		
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generatePO(RequestVO request, Writer writer ) throws Exception {
		try {
			List<Map<String, Object>> list = (List)JSONValue.parse(request.getPoData());
			String [] format = null;
			char qChar = CSVWriter.NO_QUOTE_CHARACTER;
			if (!request.getSupplierName().toUpperCase().contains("ORGILL")) {
				qChar = CSVWriter.DEFAULT_QUOTE_CHARACTER;
				format = new String[]{"SKU","Qty","Description","Cost Price","Total Cost Price"};
			}
			else{
				format = new String[]{"SKU","Qty","Retail"};
			}
			CSVWriter csvWriter = new CSVWriter(writer,',',qChar);
			csvWriter.writeNext(format);
			JSONArray dArr = null;
			JSONObject iObj = null;
			int count = 1;
			if (request.getSavePO()) {
				dArr = new JSONArray();
			}
			for (Map<String, Object> map : list) {
				if (request.getSavePO()) {
					iObj = new JSONObject();
					iObj.put("quantity",map.get("orderQuantity"));
					iObj.put("lineNumber",(count++)+"");
					iObj.put("costPrice",map.get("costPrice"));
					iObj.put("sku",map.get("sku"));
					iObj.put("retailPrice",map.get("retailPrice"));
					iObj.put("updateRP",false);
					dArr.add(iObj);
				}
				if (request.getSupplierName().toUpperCase().contains("ORGILL")) {
					csvWriter.writeNext(new String[]{map.get("sku").toString(),map.get("orderQuantity").toString(), Double.valueOf(Double.valueOf(map.get("retailPrice").toString())*100).longValue()+""});
				} else {
					csvWriter.writeNext(new String[]{map.get("sku").toString(),map.get("orderQuantity").toString(),(String)map.get("description"),map.get("costPrice").toString(),map.get("totalOrderCost").toString()});
				}
			}
			csvWriter.close();
			if (request.getSavePO()) {
				iObj = new JSONObject();
				iObj.put("invoiceNumber", "PO-ADDON-"+DateUtil.convertDate2String(new Date(), "HHmm"));
				iObj.put("supplierAddress", "");
				iObj.put("shipToAddress", "");
				iObj.put("shippingCost", "0");
				iObj.put("invoiceDetails", dArr);
				request.setInvoiceData("["+iObj.toJSONString()+"]");
				request.setRequisitioner("");
				purchaseInvoiceDAO.saveInvoices(request);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}
}
