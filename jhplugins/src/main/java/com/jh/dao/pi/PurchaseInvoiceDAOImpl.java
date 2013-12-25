package com.jh.dao.pi;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jh.dao.item.ItemDAO;
import com.jh.util.CommonUtil;
import com.jh.util.DateUtil;
import com.jh.util.FTPReader;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public class PurchaseInvoiceDAOImpl extends JdbcDaoSupport implements PurchaseInvoiceDAO {

	private static Logger logger = Logger.getLogger(PurchaseInvoiceDAOImpl.class);
	
	private static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
	
	private CommonUtil commonUtil;	
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}
	
	private ItemDAO itemDAO;
	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}
	
	private FTPReader purchaseInvoiceFTPReader;
	public void setPurchaseInvoiceFTPReader(FTPReader purchaseInvoiceFTPReader) {
		this.purchaseInvoiceFTPReader = purchaseInvoiceFTPReader;
	}

	@Override
	public String validateInvoice(File invoiceFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(invoiceFile));
		String result = parseInvoice(reader);
		reader.close();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private String parseInvoice(BufferedReader reader) throws Exception{
		StringBuilder sb = null;
		String line;
		JSONObject cJSON = null, sJSON = null;
		JSONArray invArray = new JSONArray(), detailsArray = null;
		Double shipCost = null;
		boolean isCredit = false;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("HD1")) {
				if (cJSON != null) {
					cJSON.put("invoiceDetails", detailsArray);
					cJSON.put("shippingCost", commonUtil.roundOfAndConvert2String(shipCost, 2));
					if (sb.length() > 0) {
						checkAndPopulateSkusDetails(cJSON, "<request>"+sb.toString()+"</request>");
					}
					invArray.add(cJSON);
				}
				cJSON = new JSONObject();
				detailsArray = new JSONArray();
				cJSON.put("invoiceNumber", line.substring(3, 18).trim());
				cJSON.put("invoiceDate", DateUtil.convertDate2String(DateUtil.convertString2Date(line.substring(18, 24), "MMddyy"), "dd-MMM-yyyy"));
				cJSON.put("po", line.substring(24, 39).trim());
				cJSON.put("billTo", line.substring(47, 53));
				cJSON.put("shipTo", line.substring(53, 59));
				shipCost = 0d;
				sb = new StringBuilder();
				isCredit = false;
			}
			if (line.startsWith("NM1RE")) {
				cJSON.put("supplierAddress", line.substring(5, 64));
			}
			if (line.startsWith("NM1ST")) {
				cJSON.put("shipToAddress", line.substring(5, 64));
			}
			if (line.startsWith("NM1BS")) {
				cJSON.put("billToAddress", line.substring(5, 64));
			}
			if (line.startsWith("DT1")) {
				sJSON = new JSONObject();
				sJSON.put("lineNumber", line.substring(3, 7));
				sJSON.put("sku", line.substring(29, 37).replaceFirst("-", ""));
				sJSON.put("upc", line.substring(54, 66));
				sJSON.put("quantity", Integer.valueOf(line.substring(7, 14)));
				sJSON.put("costPrice", commonUtil.roundOfAndConvert2String(Double.valueOf(line.substring(17, 28)), 2));
				sJSON.put("retailPrice", commonUtil.roundOfAndConvert2String(Double.valueOf(line.substring(69, 79)), 2));
				sJSON.put("updateRP", true);
				sJSON.put("image", commonUtil.getImageUrl(sJSON.get("sku")+".jpg"));
				sb.append("<sku>").append(sJSON.get("sku")).append("</sku>");
//				sb.append(sJSON.get("sku")).append(",");
				detailsArray.add(sJSON);
			}
			if (line.startsWith("AC1")) {
				shipCost += (Double.valueOf(line.substring(17,18).trim()+line.substring(4, 17))/100);
			}
			if (line.startsWith("TR1")) {
				cJSON.put("totalCost", (isCredit ? "-" : "")+commonUtil.roundOfAndConvert2String(Double.valueOf(line.substring(3, 14))/100, 2));
			}
			if (line.startsWith("TE1") && line.substring(58, 62).equalsIgnoreCase("CR -")) {
				isCredit = true;
			}
		}
		if (cJSON != null) {
			cJSON.put("invoiceDetails", detailsArray);
			cJSON.put("shippingCost", commonUtil.roundOfAndConvert2String(shipCost, 2));
			if (sb.length() > 0) {
				checkAndPopulateSkusDetails(cJSON, "<request>"+sb.toString()+"</request>");
			}
			invArray.add(cJSON);
		}
		cJSON = new JSONObject();
		cJSON.put("invoices", invArray);
		return cJSON.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	private void checkAndPopulateSkusDetails(JSONObject cJSON, String skus) {
		/*List<Item> list = getJdbcTemplate().query(
							"SELECT ID, " +
							"ItemLookupCode as sku, " +
							"Description, " +
							"Quantity as stockQuantity, " +
							"ReorderPoint as reorderLevel, " +
							"RestockLevel, " +
							"lastsold, " +
							"LastReceived, " +
							"(SELECT Alias+',' FROM Alias a where a.ItemID = i.ID FOR XML PATH('')) as aliases " +
							"FROM Item i where ItemLookupCode IN (SKULIST)".replaceFirst("SKULIST", "'"+skus.replaceAll(",", "','")+"'"), new BeanPropertyRowMapper<Item>(Item.class));*/
		List<Item> list = itemDAO.getItemDetails(skus); 
		JSONObject tmp = null, atmp = null;
		int idx = -1;
		String skuNA = "";
		JSONArray aliasNA = new JSONArray();
		for (Object obj : (JSONArray)cJSON.get("invoiceDetails")) {
			tmp = (JSONObject)obj;
			idx = list.indexOf(new Item(tmp.get("sku").toString()));
			if (idx != -1) {
				tmp.put("id", list.get(idx).getId());
				tmp.put("stockQuantity", list.get(idx).getStockQuantity());
				tmp.put("reorderLevel", list.get(idx).getReorderLevel());
				tmp.put("restockLevel", list.get(idx).getRestockLevel());
				tmp.put("description", list.get(idx).getDescription());
				tmp.put("committedQuantity", list.get(idx).getCommittedQuantity());
				if (list.get(idx).getLastSold() != null) {
					tmp.put("lastSold", DateUtil.convertDate2String(list.get(idx).getLastSold(), DEFAULT_DATE_FORMAT));
				}
				if (list.get(idx).getLastReceived() != null) {
					tmp.put("lastReceived", DateUtil.convertDate2String(list.get(idx).getLastReceived(), DEFAULT_DATE_FORMAT));
				}
				tmp.put("onOrder", list.get(idx).getOnOrder());
				tmp.put("transferOut", list.get(idx).getTransferOut());
				tmp.put("aliases", list.get(idx).getAliases());
				if (!tmp.get("upc").toString().trim().isEmpty() && (list.get(idx).getAliases() == null || !list.get(idx).getAliases().contains(tmp.get("upc").toString()))) {
					atmp = new JSONObject();
					atmp.put("sku", tmp.get("sku"));
					atmp.put("upc", tmp.get("upc"));
					aliasNA.add(atmp);
				}
			}
			else{
				skuNA += tmp.get("sku")+",";
			}
		}
		if (!skuNA.isEmpty()) {
			cJSON.put("skusNotAvailable", new ArrayList<String>(Arrays.asList(skuNA.split(","))));
		}
		if (!aliasNA.isEmpty()) {
			cJSON.put("aliasesNotAvailable", aliasNA);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveInvoices(final RequestVO request) {
		JSONArray iArray = (JSONArray) JSONValue.parse(request.getInvoiceData());
		JSONObject iObj = null;
		final String queryPO = "INSERT INTO [dbo].[PurchaseOrder]"+
					"([LastUpdated]"+
		                   ",[POTitle]"+
		                   ",[POType]"+
		                   ",[StoreID]"+
		                   ",[WorksheetID]"+
		                   ",[PONumber]"+
		                   ",[Status]"+
		                   ",[DateCreated]"+
		                   ",[To]"+
		                   ",[ShipTo]"+
		                   ",[Requisitioner]"+
		                   ",[ShipVia]"+
		                   ",[FOBPoint]"+
		                   ",[Terms]"+
		                   ",[TaxRate]"+
		                   ",[Shipping]"+
		                   ",[Freight]"+
		                   ",[RequiredDate]"+
		                   ",[ConfirmingTo]"+
		                   ",[Remarks]"+
		                   ",[SupplierID]"+
		                   ",[OtherStoreID]"+
		                   ",[CurrencyID]"+
		                   ",[ExchangeRate]"+
		                   ",[OtherPOID]"+
		                   ",[InventoryLocation]"+
		                   ",[IsPlaced]"+
		                   ",[DatePlaced]"+
		                   ",[BatchNumber]" +
		                   ",EstOtherFees)"+
		             " VALUES "+
		                   "(current_timestamp"+
		                   ",''"+
		                   ",0"+
		                   ",0"+
		                   ",0"+
		                   ",?"+
		                   ",0"+
		                   ",current_timestamp"+
		                   ",?"+
		                   ",?"+
		                   ",?"+
		                   ",''"+
		                   ",''"+
		                   ",''"+
		                   ",0"+
		                   ",0"+
		                   ",''"+
		                   ",current_timestamp"+
		                   ",''"+
		                   ",''"+
		                   ",?"+
		                   ",0"+
		                   ",0"+
		                   ",1"+
		                   ",0"+
		                   ",0"+
		                   ",1"+
		                   ",current_timestamp"+
		                   ",0" +
		                   ",?)";
		String queryPOE = "INSERT INTO [dbo].[PurchaseOrderEntry]"+
		           "([ItemDescription]"+
		           ",[LastUpdated]"+
		           ",[QuantityReceivedToDate]"+
		           ",[StoreID]"+
		           ",[PurchaseOrderID]"+
		           ",[QuantityOrdered]"+
		           ",[QuantityReceived]"+
		           ",[ItemID]"+
		           ",[OrderNumber]"+
		           ",[Price]"+
		           ",[TaxRate]"+
		           ",[InventoryOfflineID]"+
//		           ",[ShippingPerItem]"+
//		           ",[OtherFeesPerItem]"+
//		           ",[LastQuantityReceived]"+
//		           ",[LastReceivedDate]
					")"+
		" SELECT top 1 Description "+
		           ",current_timestamp"+
		           ",0"+
		           ",0"+
		           ",?"+
		           ",?"+
		           ",0"+
		           ",ID"+
		           ",?"+
		           ",?"+
		           ",0"+
		           ",0"+
//		           ",0"+
//		           ",0"+
//		           ",?"+
//		           ",current_timestamp"+
		" from dbo.Item where ItemLookupCode = ? " +
		" update dbo.Item set Price = ? where ItemLookupCode = ? and ? = 1 " +
		" UPDATE dbo.Item SET BinLocation = ? WHERE ItemLookupCode = ? AND ?=1 ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		for (Object object : iArray) {
			final JSONObject jInv = (JSONObject) object;
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						@Override
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(queryPO, Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, (String)jInv.get("invoiceNumber"));
							ps.setString(2, (String)jInv.get("supplierAddress"));
							ps.setString(3, (String)(jInv.containsKey("shipToAddress") ? jInv.get("shipToAddress") : jInv.get("billToAddress")));
							ps.setString(4, request.getRequisitioner());
							ps.setInt(5, request.getSupplierId());
							ps.setString(6, (String)jInv.get("shippingCost"));
							return ps;
						}
					}
					,keyHolder
			);
			Double cp = 0.0;
			String invDate = "";
			try {
				invDate = DateUtil.convertDate2String(DateUtil.convertString2Date(jInv.get("invoiceDate").toString(), "dd-MMM-yyyy"), "MMyy");
			} catch (Exception e) {
				logger.error("Error occurred while converting invoice date.",e);
			}
			for (Object object2 : (JSONArray)jInv.get("invoiceDetails")) {
				iObj = (JSONObject) object2;
				cp = Double.valueOf(iObj.get("costPrice").toString());
				if (request.getMultiplier() != null) {
					cp += ((cp*request.getMultiplier())/100);
				}
				getJdbcTemplate().update(
					queryPOE,
					keyHolder.getKey().intValue(),
					iObj.get("quantity"),
					iObj.get("lineNumber"),
					cp,
//					iObj.get("quantity"),
					iObj.get("sku"),
					
					iObj.get("retailPrice"),
					iObj.get("sku"),
					iObj.get("updateRP"),
					
					generateBinLocation(cp, iObj.get("quantity").toString(), invDate),
					iObj.get("sku"),
					request.isUpdateBinLocation() ? "1" : "0"
				);
			}
			logger.debug("PurchaseOrderID "+keyHolder.getKey()+" InvoiceNumber "+jInv.get("invoiceNumber"));
		}
	}
	
	@Override
	public List<Map<String, String>> getAllPIFiles() throws IOException {
		return purchaseInvoiceFTPReader.getAllPIFiles();
	}
	
	@Override
	public String getFtpFileDetails(RequestVO request) throws Exception {
		purchaseInvoiceFTPReader.connect(request.getFtpUserIdx());
		BufferedReader reader = purchaseInvoiceFTPReader.getPIFileDetails(request.getFtpFileName());
		String result = parseInvoice(reader);
		reader.close();
		purchaseInvoiceFTPReader.disconnect();
		return result;
	}
	
	private String generateBinLocation(Double cp, String quantity, String poDate){
		StringBuilder sb = new StringBuilder(commonUtil.roundOfAndConvert2String(cp, 2));
		sb.append(" ").append(sb.substring(sb.indexOf(".")+1,sb.indexOf(" "))).append("F").append(sb.substring(0, sb.indexOf("."))).delete(0, sb.indexOf(" ")+1);
		sb.insert(0,",").insert(0, quantity).append(",").append(poDate);
		return sb.toString();
	}
}
