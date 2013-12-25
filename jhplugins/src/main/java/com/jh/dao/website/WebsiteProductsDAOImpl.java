package com.jh.dao.website;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.jh.dao.item.ItemDAO;
import com.jh.util.CommonUtil;
import com.jh.util.DateUtil;
import com.jh.util.EdiParser;
import com.jh.util.FTPReader;
import com.jh.util.FTPWriter;
import com.jh.util.FreemarkerTemplateUtil;
import com.jh.vo.FTPUser;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;
import com.jh.vo.WebsiteOrder;
import com.jh.vo.edi.x12.EdiInvoice;
import com.jh.vo.edi.x12.type997.EdiAcknowledgement;

public class WebsiteProductsDAOImpl implements WebsiteProductsDAO {

	private static final String WEB_DEPT_SKU_FILE = "WEB_DEPT_SKU.TXT";
	private static final String WEB_SKU_COMMON_FILE = "WEB_SKU_COMMON.TXT";
	private static final String WEB_DEPT_FILE = "WEB_DEPT.TXT";
	private static final String WEB_INVENTORY_FILE = "WEB_INVENTORY.TXT";
	
	private static Logger logger = Logger.getLogger(WebsiteProductsDAOImpl.class);
	
	private FTPReader webfilesFTPReader;
	public void setWebfilesFTPReader(FTPReader webfilesFTPReader) {
		this.webfilesFTPReader = webfilesFTPReader;
	}	
	public FTPReader getWebfilesFTPReader() {
		return webfilesFTPReader;
	}

	private NamedParameterJdbcTemplate jdbcTemplate;	
	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	private CommonUtil commonUtil;	
	public CommonUtil getCommonUtil() {
		return commonUtil;
	}
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}
	
	private JdbcTemplate dynamicsTemplate;	
	public void setDynamicsTemplate(JdbcTemplate dynamicsTemplate) {
		this.dynamicsTemplate = dynamicsTemplate;
	}	
	public JdbcTemplate getDynamicsTemplate() {
		return dynamicsTemplate;
	}

	private ItemDAO itemDAO;
	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}
	
	private FTPUser orgillEdiFtpUser;
	public void setOrgillEdiFtpUser(FTPUser orgillEdiFtpUser) {
		this.orgillEdiFtpUser = orgillEdiFtpUser;
	}
	
	private FTPUser orgillEdiAckFtpUser;	
	public void setOrgillEdiAckFtpUser(FTPUser orgillEdiAckFtpUser) {
		this.orgillEdiAckFtpUser = orgillEdiAckFtpUser;
	}

	private FTPWriter ftpWriter;
	public void setFtpWriter(FTPWriter ftpWriter) {
		this.ftpWriter = ftpWriter;
	}
	
	private FreemarkerTemplateUtil templateUtil;
	public void setTemplateUtil(FreemarkerTemplateUtil templateUtil) {
		this.templateUtil = templateUtil;
	}
	
	private EdiParser ediParser;
	public void setEdiParser(EdiParser ediParser) {
		this.ediParser = ediParser;
	}
	
	private List<String> localWarehouses;
	private String localWarehouseList;
	public void setLocalWarehouseList(String lw) {
		this.localWarehouseList = lw;
		if (lw != null && !lw.isEmpty()) {
			this.localWarehouses = new ArrayList<String>(Arrays.asList(this.localWarehouseList.split(",")));
		}
	}
	
	private FTPUser orgillEdiInvoiceFtpUser;	
	public void setOrgillEdiInvoiceFtpUser(FTPUser orgillEdiInvoiceFtpUser) {
		this.orgillEdiInvoiceFtpUser = orgillEdiInvoiceFtpUser;
	}
	
	@Override
	public List<Item> getDeletedProducts() throws IOException {
		List<String> skus = new ArrayList<String>();
		for (String[] csvLine : downloadAndParseFtpFile(WEB_DEPT_SKU_FILE)) {
			skus.add(csvLine[6]);
		}
		List<Item> list = null;
		if (!skus.isEmpty()) {
			list = jdbcTemplate.query("SELECT p1.product_id AS id, sku, p2.name as description FROM product p1 LEFT JOIN product_description p2 ON p2.product_id = p1.product_id AND p2.language_id = 1 WHERE sku NOT IN (:SKUS) AND p1.status = 1",Collections.singletonMap("SKUS", skus),new BeanPropertyRowMapper<Item>(Item.class));
			for (Item item : list) {
				if (item.getImage() == null) {
					item.setImage(commonUtil.getImageUrl(item.getSku(),"jpg"));
				}
				else{
					item.setImage(commonUtil.getImageUrl(item.getImage()));
				}
			}
		}
		return list;
	}
	
	@Override
	public int deleteProducts(RequestVO request) throws Exception {
		int count = jdbcTemplate.update("UPDATE product SET STATUS = 0 WHERE product_id IN (:PRODUCT_IDS)", Collections.singletonMap("PRODUCT_IDS", JSONValue.parse(request.getItemsToModify())));
		return count;
	}
	
	private List<String[]> downloadAndParseFtpFile(String filename) throws IOException {
		filename = webfilesFTPReader.downloadWebFile(filename);
		return parseFtpFile(filename);
	}
	
	private List<String[]> parseFtpFile(String filename) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(filename),'~',CSVWriter.NO_QUOTE_CHARACTER);
		String[] csvLine = null;
		List<String[]> data = new ArrayList<String[]>();
		while ((csvLine = reader.readNext()) != null) {
			data.add(csvLine);
		}
		reader.close();
		return data;
	}
	
	@Override
	public void downloadParseAndPersistWebFiles() throws IOException {
		String file1 = webfilesFTPReader.downloadWebFile(WEB_DEPT_SKU_FILE), file2 = webfilesFTPReader.downloadWebFile(WEB_SKU_COMMON_FILE);
//		if (file1 != null && file2 != null) {
			parseAndPersistWebFiles(file1, file2);
//		}
	}
	
	@Override
	public void parseAndPersistWebFiles(String webDeptSkuFileName, String webSkuCommonFileName) throws IOException {
		Map<String, Map<String, String>> skus = new HashMap<String, Map<String,String>>();
		Map<String, String> detMap = null;
		for (String[] det : parseFtpFile(webDeptSkuFileName)) {
			detMap = new HashMap<String, String>();
			detMap.put("dept", det[0]);
			detMap.put("category", det[2]);
			detMap.put("sku", det[6]);
			skus.put(det[6], detMap);
		}
		dynamicsTemplate.update("UPDATE dbo.WebItem SET IS_UPDATED = 0");
		final String queryString = 
			"IF (select count(*) from WebItem where SKU = ?) = 0 " +
				" INSERT INTO [dbo].[WebItem]([FACTORY],[NOMENCLATURE_EXT],[WIDTH],[HEIGHT],[LENGTH],[WEIGHT],[VENDOR_NUMBER],[CATALOG_VENDOR_NAME],[NOMENCLATURE],[CATALOG_PAGE_NUMBER],[SUGGESTED_RETAIL],[SELLING_UNIT],[BUYER],[IMAGE],[PRO_BENCHMARK_RETAIL],[UPC_CODE],[REG_PRICE],[VP1_PRICE],[VP2_PRICE],[ADV_PRICE],[RETAIL_SENSITIVITY],[BENCHMARK_RETAIL],[QUANTITY_ROUND_OPTION],[OLD_UPC],[PRO_SENSITIVITY_CODE],[VENDOR_UNIT_OF_MEASURE],[CLAIMS_CODE],[CLAIMS_MEMO_FLAG],[REPLACEMENT_ITEM],[SUBSTITUTE_ITEM],[COUNTRY_CODE],[HARMONIZED_CODE_1],[HARMONIZED_CODE_2],[HARMONIZED_CODE_3],[BUYING_DEPT_DESC],[HAZARDOUS_INLAND],[HAZARDOUS_MARINE],[KIT_ITEM_SWITCH],[KIT_ITEM_KEY],[CONTAINER_UPC],[RETAIL_UNIT_OF_MEASURE],[CUBIC_DIVISOR],[SHIP_BY_UPS_FEDEX_USPS],[DEPT_ID],[CATEGORY_ID],[IS_UPDATED],[SKU]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,2,?); " +
			" ELSE " +
//				" UPDATE [dbo].[WebItem] SET [FACTORY] = ?,[NOMENCLATURE_EXT] = ?,[WIDTH] = ?,[HEIGHT] = ?,[LENGTH] = ?,[WEIGHT] = ?,[VENDOR_NUMBER] = ?,[CATALOG_VENDOR_NAME] = ?,[NOMENCLATURE] = ?,[CATALOG_PAGE_NUMBER] = ?,[SUGGESTED_RETAIL] = ?,[SELLING_UNIT] = ?,[BUYER] = ?,[IMAGE] = ?,[PRO_BENCHMARK_RETAIL] = ?,[UPC_CODE] = ?,[REG_PRICE] = ?,[VP1_PRICE] = ?,[VP2_PRICE] = ?,[ADV_PRICE] = ?,[RETAIL_SENSITIVITY] = ?,[BENCHMARK_RETAIL] = ?,[QUANTITY_ROUND_OPTION] = ?,[OLD_UPC] = ?,[PRO_SENSITIVITY_CODE] = ?,[VENDOR_UNIT_OF_MEASURE] = ?,[CLAIMS_CODE] = ?,[CLAIMS_MEMO_FLAG] = ?,[REPLACEMENT_ITEM] = ?,[SUBSTITUTE_ITEM] = ?,[COUNTRY_CODE] = ?,[HARMONIZED_CODE_1] = ?,[HARMONIZED_CODE_2] = ?,[HARMONIZED_CODE_3] = ?,[BUYING_DEPT_DESC] = ?,[HAZARDOUS_INLAND] = ?,[HAZARDOUS_MARINE] = ?,[KIT_ITEM_SWITCH] = ?,[KIT_ITEM_KEY] = ?,[CONTAINER_UPC] = ?,[RETAIL_UNIT_OF_MEASURE] = ?,[CUBIC_DIVISOR] = ?,[SHIP_BY_UPS_FEDEX_USPS] = ?,[DEPT_ID] = ?,[CATEGORY_ID] = ?,[IS_UPDATED] = 1 WHERE [SKU] = ?; ";
			" UPDATE [dbo].[WebItem] SET [IS_UPDATED] = 1 WHERE [SKU] = ? AND IS_UPDATED <> 1; ";
		int count = 0, recordsPerBatch = 300;
		final List<String[]> list = new ArrayList<String[]>();
		logger.debug("Start: Persisting website items");
		for (String[] det : parseFtpFile(webSkuCommonFileName)) {
			if (count >= recordsPerBatch) {
				persistWebItems(queryString, list);
				count = 0;
				list.clear();
			}
			detMap = skus.get(det[0]);
			list.add((String[])ArrayUtils.addAll(det, new String[]{detMap.get("dept"),detMap.get("category")}));
			count+=1;
		}
		if (!list.isEmpty()) {
			persistWebItems(queryString, list);
		}
		logger.debug("End: Persisting website items");
	}
	
	private void persistWebItems(final String queryString, final List<String[]> data) {
		dynamicsTemplate.batchUpdate(queryString, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				ps.setString(1,data.get(rowNum)[0].trim());

				ps.setString(2,data.get(rowNum)[1].trim());
				ps.setString(3,data.get(rowNum)[2].trim());
				ps.setString(4,data.get(rowNum)[3].trim());
				ps.setString(5,data.get(rowNum)[4].trim());
				ps.setString(6,data.get(rowNum)[5].trim());
				ps.setString(7,data.get(rowNum)[6].trim());
				ps.setString(8,data.get(rowNum)[7].trim());
				ps.setString(9,data.get(rowNum)[8].trim());
				ps.setString(10,data.get(rowNum)[9].trim());
				ps.setString(11,data.get(rowNum)[10].trim());
				ps.setString(12,data.get(rowNum)[11].trim());
				ps.setString(13,data.get(rowNum)[12].trim());
				ps.setString(14,data.get(rowNum)[13].trim());
				ps.setString(15,data.get(rowNum)[14].trim());
				ps.setString(16,data.get(rowNum)[15].trim());
				ps.setString(17,data.get(rowNum)[16].trim());
				ps.setString(18,data.get(rowNum)[17].trim());
				ps.setString(19,data.get(rowNum)[18].trim());
				ps.setString(20,data.get(rowNum)[19].trim());
				ps.setString(21,data.get(rowNum)[20].trim());
				ps.setString(22,data.get(rowNum)[21].trim());
				ps.setString(23,data.get(rowNum)[22].trim());
				ps.setString(24,data.get(rowNum)[23].trim());
				ps.setString(25,data.get(rowNum)[24].trim());
				ps.setString(26,data.get(rowNum)[25].trim());
				ps.setString(27,data.get(rowNum)[26].trim());
				ps.setString(28,data.get(rowNum)[27].trim());
				ps.setString(29,data.get(rowNum)[28].trim());
				ps.setString(30,data.get(rowNum)[29].trim());
				ps.setString(31,data.get(rowNum)[30].trim());
				ps.setString(32,data.get(rowNum)[31].trim());
				ps.setString(33,data.get(rowNum)[32].trim());
				ps.setString(34,data.get(rowNum)[33].trim());
				ps.setString(35,data.get(rowNum)[34].trim());
				ps.setString(36,data.get(rowNum)[35].trim());
				ps.setString(37,data.get(rowNum)[36].trim());
				ps.setString(38,data.get(rowNum)[37].trim());
				ps.setString(39,data.get(rowNum)[38].trim());
				ps.setString(40,data.get(rowNum)[39].trim());
				ps.setString(41,data.get(rowNum)[40].trim());
				ps.setString(42,data.get(rowNum)[41].trim());
				ps.setString(43,data.get(rowNum)[42].trim());
				ps.setString(44,data.get(rowNum)[43].trim());
				ps.setString(45,data.get(rowNum)[45].trim());
				ps.setString(46,data.get(rowNum)[46].trim());
				ps.setString(47,data.get(rowNum)[0].trim());				
				ps.setString(48,data.get(rowNum)[0].trim());
			}
			
			@Override
			public int getBatchSize() {
				return data.size();
			}
		});
	}
	
	@Override
	public void downloadParseAndUpdateWebInventory() throws IOException {
		String filename = webfilesFTPReader.downloadWebFile(WEB_INVENTORY_FILE);
//		if (filename != null) {
			parseAndUpdateWebInventory(filename);
//		}
	}
	
	@Override
	public void parseAndUpdateWebInventory(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename),'~',CSVWriter.NO_QUOTE_CHARACTER);
		String[] csvLine = null;
		Map<String, Integer[]> webQty = new HashMap<String, Integer[]>();
		int qty = 0, qtyInLocalWarehouse = 0;
		while ((csvLine = reader.readNext()) != null) {
			qty = Integer.valueOf(csvLine[2]);
			if (localWarehouses == null) {
				qtyInLocalWarehouse = -2;
			}
			else{
				if (localWarehouses.indexOf(csvLine[1]) > -1) {
					qtyInLocalWarehouse = qty;
				}
				else{
					qtyInLocalWarehouse = -1;
				}
			}
			if (!webQty.containsKey(csvLine[0])) {
				webQty.put(csvLine[0], new Integer[]{qty,qtyInLocalWarehouse});
			}
			else{
				webQty.get(csvLine[0])[0]=webQty.get(csvLine[0])[0]+qty;
				if (qtyInLocalWarehouse > -1) {
					webQty.get(csvLine[0])[1]=qtyInLocalWarehouse;
				}
			}
		}
		reader.close();
		updateWebInventory(webQty);
	}
	
	private void updateWebInventory(Map<String, Integer[]> webQty) throws IOException {
//		String query = "SET ARITHABORT ON; declare @req xml; set @req = ?; UPDATE dbo.WebItem set QUANTITY_IN_STOCK = r.qty from (select d.value('@sku','nvarchar(20)') as sku, d.value('@qty','int') as qty from @req.nodes('/request/item') as ref(d)) r inner join dbo.WebItem w on r.sku = w.sku; select 1;";
		String query = "SET ARITHABORT ON; declare @req xml; set @req = ?; UPDATE w set AVAILABLE_QUANTITY = case when IS_SHIPPING_AVAILABLE = 0 and qtyLocal <> -2 then case when r.qtyLocal = 0 then 0 else r.qtyLocal end else r.qty end, PREVIOUS_QUANTITY = AVAILABLE_QUANTITY, MODIFIED_DATE = current_timestamp from (select d.value('@sku','nvarchar(20)') as sku, d.value('@qty','int') as qty, d.value('@qtyLocal','int') as qtyLocal from @req.nodes('/request/item') as ref(d)) r inner join dbo.web_products w on r.sku = w.sku; select 1;";
		int BATCH_SIZE = 100, cnt = 0;
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer[]> qty : webQty.entrySet()) {
			sb.append("<item sku=\"").append(qty.getKey()).append("\" qty=\"").append(qty.getValue()[0]).append("\" qtyLocal=\"").append(qty.getValue()[1] == -1 ? 0 : qty.getValue()[1]).append("\" />");
			cnt += 1;
			if (cnt >= BATCH_SIZE) {
				sb.insert(0, "<request>").append("</request>");
				dynamicsTemplate.queryForInt(query, sb.toString());
				cnt = 0;
				sb.delete(0, sb.length());
			}
		}
		if (sb.length() > 0) {
			sb.insert(0, "<request>").append("</request>");
			dynamicsTemplate.queryForInt(query, sb.toString());
		}
	}
	
	@Override
	public List<Item> getItemsToDeleteAfterSync() {
		List<Item> list = dynamicsTemplate.query("SELECT i.ID as skuId, i.ItemLookupCode as sku, i.Description, NULL as width, NULL as height, NULL as length, i.Weight, i.Price as retailPrice, i.UnitOfMeasure as uom, d.ID as deptId, d.Name as deptName, c.ID as categoryId, c.Name as categoryName from Item i LEFT JOIN Department d ON i.DepartmentID = d.ID LEFT JOIN Category c ON i.CategoryID = c.ID LEFT JOIN WebItem w ON w.SKU = i.ItemLookupCode LEFT JOIN Supplier s ON i.SupplierID = s.ID WHERE (w.SKU IS NULL OR w.IS_UPDATED = 0) AND i.BlockSalesType = 0 AND i.Quantity <= 0 AND s.SupplierName LIKE '%ORGILL%' ORDER BY i.ItemLookupCode", new BeanPropertyRowMapper<Item>(Item.class));
		for (Item item : list) {
			if (item.getImage() == null) {
				item.setImage(commonUtil.getImageUrl(item.getSku(),"jpg"));
			}
			else{
				item.setImage(commonUtil.getImageUrl(item.getImage()));
			}
		}
		return list;
	}
	
	@Override
	public List<Item> getItemsToAddAfterSync() {
		List<Item> list = dynamicsTemplate.query("SELECT i.ID as skuId, w.SKU, w.NOMENCLATURE_EXT as description, w.WIDTH, w.HEIGHT, w.LENGTH, w.WEIGHT, w.REG_PRICE/100 as retailPrice, w.RETAIL_UNIT_OF_MEASURE as uom, d.ID as deptId, d.Name as deptName, c.ID as categoryId, c.Name as categoryName FROM WebItem w LEFT JOIN Department d ON w.DEPT_ID = d.code LEFT JOIN Category c ON w.CATEGORY_ID = c.Code LEFT JOIN Item i on w.SKU = i.ItemLookupCode LEFT JOIN Supplier s on i.SupplierID = s.ID where w.IS_UPDATED in (1,2) AND (i.ID IS NULL or (i.BlockSalesType = 1 AND s.SupplierName LIKE '%ORGILL%')) ORDER BY w.SKU", new BeanPropertyRowMapper<Item>(Item.class));
		for (Item item : list) {
			item.setImage(commonUtil.getOrgillImageUrl(item.getSku()+".jpg"));
		}
		return list;
	}
	
	@Override
	public void parseAndPersistWebCategories(String webDeptFileName) throws IOException {
		final String queryString = 
			"IF (select count(*) from WebCategory where DEPT_CODE = ? and SUB_CODE = ? and CATEGORY_CODE = ?) = 0 " +
				" INSERT INTO [dbo].[WebCategory]([DEPT_CODE],[SUB_CODE],[CATEGORY_CODE],[NAME]) VALUES (?,?,?,?);";
		int count = 0, recordsPerBatch = 300;
		final List<String[]> list = new ArrayList<String[]>();
		logger.debug("Start: Persisting website categories");
		for (String[] det : parseFtpFile(webDeptFileName)) {
			if (det.length >= 13 && det[3].trim().equalsIgnoreCase("000")) {
				if (count >= recordsPerBatch) {
					persistWebCategories(queryString, list);
					count = 0;
					list.clear();
				}
				list.add(det);
				count+=1;
			}
		}
		if (!list.isEmpty()) {
			persistWebCategories(queryString, list);
		}
		logger.debug("End: Persisting website categories");
	}
	
	private void persistWebCategories(final String queryString, final List<String[]> data) {
		dynamicsTemplate.batchUpdate(queryString, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				ps.setString(1,data.get(rowNum)[0].trim());
				ps.setString(2,data.get(rowNum)[1].trim());
				ps.setString(3,data.get(rowNum)[2].trim());
				
				ps.setString(4,data.get(rowNum)[0].trim());
				ps.setString(5,data.get(rowNum)[1].trim());
				ps.setString(6,data.get(rowNum)[2].trim());
				ps.setString(7,data.get(rowNum)[12].trim());
			}
			
			@Override
			public int getBatchSize() {
				return data.size();
			}
		});
	}
	
	@Override
	public void downloadParseAndPersistWebCategories() throws IOException {
		String filename = webfilesFTPReader.downloadWebFile(WEB_DEPT_FILE);
		if (filename != null) {
			parseAndPersistWebCategories(filename);
		}
	}
	
	@Override
	public List<Item> getDeptToAddAfterSync() {
		List<Item> list = dynamicsTemplate.query("SELECT w.DEPT_CODE as deptCode, w.NAME as deptName FROM WebCategory w LEFT JOIN Department d ON w.DEPT_CODE = d.code WHERE d.ID IS NULL AND w.SUB_CODE = '000' AND w.CATEGORY_CODE = '00000'", new BeanPropertyRowMapper<Item>(Item.class));
		return list;
	}
	
	@Override
	public List<Item> getCategoryToAddAfterSync() {
		List<Item> list = dynamicsTemplate.query("SELECT d.ID as deptId, d.Name as deptName, w.NAME as categoryName, w.CATEGORY_CODE as categoryCode FROM WebCategory w LEFT JOIN Department d ON w.DEPT_CODE = d.code LEFT JOIN Category c ON c.Code = w.CATEGORY_CODE WHERE d.ID is not null and c.ID IS NULL AND w.SUB_CODE <> '000' AND w.CATEGORY_CODE <> '00000' ORDER BY d.Name, w.NAME", new BeanPropertyRowMapper<Item>(Item.class));
		return list;
	}
	
	@Override
	public List<Item> getWebsiteItemsToDisableAfterSync() {
		String toDisableItems = dynamicsTemplate.queryForObject("DECLARE @skus nvarchar(MAX); set @skus = (select sku+',' from WebItem where IS_UPDATED = 0 FOR XML PATH('')); SELECT ''''+replace(left(@skus,len(@skus)-1),',',''',''')+'''' as toDisableSkus;", String.class);
		List<Item> list = null;
		if (StringUtils.isNotBlank(toDisableItems)) {
			list = jdbcTemplate.getJdbcOperations().query("SELECT p.`product_id` AS skuId, `sku`, `name` AS Description, NULL AS width, NULL AS height, NULL AS LENGTH, NULL AS weight, `price` AS retailPrice, NULL AS uom, NULL AS deptId, NULL AS deptName, NULL AS categoryId, NULL AS categoryName FROM `product` p INNER JOIN `product_description` pd ON p.`product_id` = pd.`product_id` WHERE sku IN (:SKULIST) AND `quantity` > 0 ORDER BY sku".replaceFirst(":SKULIST", toDisableItems), new BeanPropertyRowMapper<Item>(Item.class));
			for (Item item : list) {
				if (item.getImage() == null) {
					item.setImage(commonUtil.getImageUrl(item.getSku(),"jpg"));
				}
				else{
					item.setImage(commonUtil.getImageUrl(item.getImage()));
				}
			}
		}
		else{
			list = new ArrayList<Item>();
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int deactivateWebsiteItems(RequestVO request) {
		int updateCnt = 0;
		List<String> list = (List)JSONValue.parse(request.getItemsToModify());
		if (!list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String sku : list) {
				sb.append("'").append(sku).append("',");
			}
			sb.deleteCharAt(sb.length()-1);
			updateCnt += jdbcTemplate.getJdbcOperations().update("UPDATE `product` SET `quantity` = 0, `date_modified` = CURRENT_TIMESTAMP, `stock_status_id` = (SELECT `stock_status_id` FROM `stock_status` WHERE `name` = 'Out Of Stock') WHERE `sku` IN (:SKULIST) and `quantity` > 0 ".replaceFirst(":SKULIST", sb.toString()));
		}
		return updateCnt;
	}
	
	@Override
	public List<Item> getWebsiteItemsToActivateAfterSync() {
		final StringBuilder sb = new StringBuilder();
		jdbcTemplate.getJdbcOperations().query("SELECT `sku` FROM `product` p INNER JOIN `stock_status` s ON s.`stock_status_id` = p.`stock_status_id` WHERE `quantity` = 0 AND s.`name` = 'Out Of Stock' ORDER BY p.`sku`", new ResultSetExtractor<Integer>(){
			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				while (rs.next()) {
					sb.append("'").append(rs.getString("sku")).append("',");
				}
				return null;
			}
		});
		List<Item> list = null;
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
			list = dynamicsTemplate.query("SELECT w.SKU, w.NOMENCLATURE_EXT as description, w.WIDTH, w.HEIGHT, w.LENGTH, w.WEIGHT, w.REG_PRICE/100 as retailPrice, w.RETAIL_UNIT_OF_MEASURE as uom, null as deptId, null as deptName, null as categoryId, null as categoryName FROM WebItem w where w.IS_UPDATED in (1,2) AND w.SKU in (:SKULIST) ORDER BY w.SKU".replaceFirst(":SKULIST", sb.toString()), new BeanPropertyRowMapper<Item>(Item.class));
			int iCnt = 0;
			for (Item item : list) {
				item.setId(iCnt++);
				item.setImage(commonUtil.getOrgillImageUrl(item.getSku()+".jpg"));
			}
		}
		else{
			list = new ArrayList<Item>();
		}
		sb.delete(0, sb.length());
		return list;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void activateWebsiteItems(RequestVO request) {
		List<String> list = (List)JSONValue.parse(request.getItemsToModify());
		if (!list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String sku : list) {
				sb.append("'").append(sku).append("',");
			}
			sb.deleteCharAt(sb.length()-1);
			jdbcTemplate.getJdbcOperations().update("UPDATE `product` SET `quantity` = 1000, `date_modified` = CURRENT_TIMESTAMP, `stock_status_id` = (SELECT `stock_status_id` FROM `stock_status` WHERE `name` = 'In Stock') WHERE `sku` IN (:SKULIST)".replaceFirst(":SKULIST", sb.toString()));
		}
	}
	
	@Override
	public List<WebsiteOrder> getWebsiteOrder() {
		final StringBuilder sb = new StringBuilder();
		List<WebsiteOrder> orders =  jdbcTemplate.getJdbcOperations().query("SELECT  od.order_id, od.po_number, od.`invoice_no`, od.`invoice_prefix`, od.`store_name`, case when od.shipping_firstname is null then od.`firstname` else od.shipping_firstname end as firstname, case when od.shipping_lastname is null then od.`lastname` else od.shipping_lastname end as lastname, od.`email`, od.`telephone`, od.`shipping_address_1`, od.`shipping_address_2`, od.`shipping_city`, od.`shipping_postcode`, od.`shipping_country`, od.`shipping_zone`, z.`code` AS shippingZoneCode, od.`total`, od.`date_added`, od.`date_modified`, p.`sku`, op.`quantity`, od.total AS totalBilledAmount, os.name AS orderStatus, od.ship_order_from FROM `order` od INNER JOIN `order_status` os ON od.`order_status_id` = os.order_status_id INNER JOIN `order_product` op ON op.`order_id` = od.`order_id` INNER JOIN `zone` z ON od.`shipping_zone_id` = z.zone_id INNER JOIN `product` p ON op.`product_id` = p.`product_id` WHERE os.`name` IN ('Processing','Pending','Processed') AND (od.`ship_order_from` IS NULL OR od.`ship_order_from` NOT IN ('store')) ORDER BY od.`date_added` DESC, `order_id`", new ResultSetExtractor<List<WebsiteOrder>>(){
			@Override
			public List<WebsiteOrder> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<WebsiteOrder> orders = new ArrayList<WebsiteOrder>();
				WebsiteOrder order = null;
				int pOrdId = -1;
				BeanPropertyRowMapper<WebsiteOrder> mapper = new BeanPropertyRowMapper<WebsiteOrder>(WebsiteOrder.class);
				Item  oItem=null;
				StringBuilder tmpSB = new StringBuilder();
				while (rs.next()) {
					if (rs.getInt("order_id") != pOrdId) {
						order = mapper.mapRow(rs, rs.getRow());
						order.setItemsOnOrder(new ArrayList<Item>());
						if (order.getShipOrderFrom() != null && !order.getShipOrderFrom().equalsIgnoreCase("store") && order.getOrderStatus().equalsIgnoreCase("Processing")) {
							order.setOrderStatus("Awaiting ack");
						}
						if (StringUtils.isBlank(order.getPoNumber())) {
							order.setPoNumber("NA");
						}
						orders.add(order);
					}
					oItem = new Item();
					oItem.setSku(rs.getString("sku"));
					oItem.setOnOrder( rs.getInt("quantity"));
					order.getItemsOnOrder().add(oItem);
					tmpSB.delete(0, tmpSB.length());
					tmpSB.append("<sku>").append(rs.getString("sku")).append("</sku>");
					if (sb.indexOf(tmpSB.toString()) == -1) {
						sb.append("<sku>").append(rs.getString("sku")).append("</sku>");
					}
					pOrdId = rs.getInt("order_id");
				}
				return orders;
			}	
		});
		sb.insert(0, "<request>").append("</request>");
		List<Item> itemList = itemDAO.getItemDetails(sb.toString());
		int idx = -1;
		for (WebsiteOrder o : orders) {
			o.setFulfillOrderAtStore(true);
			for (Item i : o.getItemsOnOrder()) {
				idx = itemList.indexOf(i);
				if (idx > -1) {
					i.setAliases(itemList.get(idx).getAliases());
					i.setCategoryCode(itemList.get(idx).getCategoryCode());
					i.setCategoryName(itemList.get(idx).getCategoryName());
					i.setCostPrice(itemList.get(idx).getCostPrice());
					i.setDeptCode(itemList.get(idx).getDeptCode());
					i.setDeptName(itemList.get(idx).getDeptName());
					i.setDescription(itemList.get(idx).getDescription());
					i.setImage(itemList.get(idx).getImage());
				}
				else {
					o.setFulfillOrderAtStore(false);
				}
				if (o.getFulfillOrderAtStore()) {
					if (itemList.get(idx).getStockQuantity() < i.getOnOrder()) {
						o.setFulfillOrderAtStore(false);
					}
					else{
						itemList.get(idx).setStockQuantity(itemList.get(idx).getStockQuantity() - i.getOnOrder());
					}
				}
			}
		}
		return orders;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void processOrders(String ordersToProcessFrom, String ordersToProcess) throws Exception {
		JSONArray orders = (JSONArray) JSONValue.parse(ordersToProcess);
		String updateQuery = "UPDATE `order` SET `ship_order_from` = ?, `order_status_id` = (SELECT `order_status_id` FROM `order_status` WHERE NAME = case when ? in ('store') then 'Processed' else 'Processing' end) WHERE `order_id` = ?";
		List<Object[]> batchObjects = new ArrayList<Object[]>();
		StringBuilder fileName = new StringBuilder();
		int oId = 0;
		Date cDate = new Date();
		String dateString = DateUtil.convertDate2String(cDate, "yyyyMMdd");
		JSONObject order = null;
		File orderFile = null;
		FTPClient orgillFTPClient = null;
		for (Object object : orders) {
			order = (JSONObject) object;
			oId = Long.valueOf(order.get("orderId").toString()).intValue();
			batchObjects.add(new Object[]{ordersToProcessFrom, ordersToProcessFrom, oId});
			if (ordersToProcessFrom.equals("orgill") && orgillEdiFtpUser != null && orgillEdiFtpUser.getLocalFolderPath() != null && !orgillEdiFtpUser.getLocalFolderPath().equalsIgnoreCase("NA")) {
				order.put("userId", orgillEdiFtpUser.getUsername());
				order.put("orderDate", cDate);
				fileName.replace(0,fileName.length(),dateString).append("_").append(oId).append(orgillEdiFtpUser.getFileExtension());
				orderFile = templateUtil.createFileFromTemplate("edi.orgill.850", orgillEdiFtpUser.getLocalFolderPath(), fileName.toString(), order);
				logger.debug("Created EDI file "+fileName.toString()+" in filepath "+orgillEdiFtpUser.getLocalFolderPath());
				if (orgillEdiFtpUser.getHostname() != null && !orgillEdiFtpUser.getHostname().equalsIgnoreCase("NA")) {
					if (orgillFTPClient == null) {
						orgillFTPClient = ftpWriter.connect(orgillEdiFtpUser);
					}
					ftpWriter.uploadFileToFTP(orgillEdiFtpUser, orgillFTPClient, orderFile);
					logger.debug("Uploaded EDI file "+fileName.toString()+" to FTP site "+orgillEdiFtpUser.getHostname());
					fileName.replace(0, fileName.length(),orderFile.getAbsolutePath()).append(".uploaded");
					orderFile.renameTo(new File(fileName.toString()));
				}
			}
		}
		ftpWriter.disconnect(orgillFTPClient);
		jdbcTemplate.getJdbcOperations().batchUpdate(updateQuery, batchObjects);
	}
	
	@Override
	public String checkAndUpdateOrderAcknowledgement(String ordersToProcessFrom) throws Exception {
		StringBuilder result = new StringBuilder();
		if (ordersToProcessFrom.equalsIgnoreCase("orgill") && orgillEdiAckFtpUser != null) {
			List<WebsiteOrder> ordersToChk = jdbcTemplate.getJdbcOperations().query("select order_id, po_number from `order` where ship_order_from = ? and order_status_id in (select order_status_id from order_status where name in ('Processing'))", new BeanPropertyRowMapper<WebsiteOrder>(WebsiteOrder.class), ordersToProcessFrom);
			if (ordersToChk != null && !ordersToChk.isEmpty()) {
				String updateQuery = "UPDATE `order` SET `order_status_id` = (SELECT `order_status_id` FROM `order_status` WHERE NAME = ?), `date_modified` = current_timestamp WHERE `order_id` = ?", status;
				EdiAcknowledgement tmp = new EdiAcknowledgement();
				tmp.setType("PO");
				int idx = -1;
				List<String> filenames = webfilesFTPReader.downloadFTPFiles(orgillEdiAckFtpUser);
				List<EdiAcknowledgement> acks = ediParser.extractEdiAcknowledgements(orgillEdiAckFtpUser.getLocalFolderPath(), filenames);
				for (WebsiteOrder o : ordersToChk) {
					if (o.getPoNumber() == null) {
						tmp.setTypeId(Integer.valueOf(o.getOrderId()).toString());
					} else {
						tmp.setTypeId(o.getPoNumber());
					}
					idx = acks.indexOf(tmp);
					if (idx > -1) {
						if (acks.get(idx).getStatus().equalsIgnoreCase("A")) {
							status = "Processed";
						}
						else{
							status = "Processing";
						}
						jdbcTemplate.getJdbcOperations().update(updateQuery, status, o.getOrderId());
						logger.debug("Received acknowledgement "+acks.get(idx).toString()+" for order id "+o.getOrderId()+" and update the status on website accordingly.");
						result.append("Received acknowledgement for order id ").append(o.getOrderId()).append(".\n");
					}
				}
			}
			else{
				logger.debug("No orders found to process from "+ordersToProcessFrom+".");
			}
		}
		else{
			logger.warn("No FTP user set for checking order acknowledgements for "+ordersToProcessFrom);
		}
		return result.toString();
	}
	
	@Override
	public void updatePoNumber(RequestVO request) {
		jdbcTemplate.getJdbcOperations().update("update `order` set po_number = ? where order_id = ?", request.getPoNumber(), request.getOrderId());
	}
	
	@Override
	public String checkAndUpdateOrderShipping(String ordersToProcessFrom) throws Exception {
		StringBuilder result = new StringBuilder();
		if (ordersToProcessFrom.equalsIgnoreCase("orgill") && orgillEdiInvoiceFtpUser != null) {
			List<WebsiteOrder> ordersToChk = jdbcTemplate.getJdbcOperations().query("select order_id, po_number from `order` where ship_order_from = ? and order_status_id in (select order_status_id from order_status where name in ('Processed'))", new BeanPropertyRowMapper<WebsiteOrder>(WebsiteOrder.class), ordersToProcessFrom);
			if (ordersToChk != null && !ordersToChk.isEmpty()) {
				String updateQuery = "UPDATE `order` SET `order_status_id` = (SELECT `order_status_id` FROM `order_status` WHERE NAME = ?), `date_modified` = current_timestamp, `comment` = CONCAT(CASE WHEN LENGTH(`comment`) = 0 THEN '' ELSE CONCAT(`comment`,'\n') END,?) WHERE `order_id` = ?", 
						status = "Shipped";
				EdiInvoice tmp = new EdiInvoice();
				int idx = -1;
				List<String> filenames = webfilesFTPReader.downloadFTPFiles(orgillEdiInvoiceFtpUser);
				List<EdiInvoice> invoices = ediParser.extractEdiInvoices(orgillEdiInvoiceFtpUser.getLocalFolderPath(), filenames);
				StringBuilder comments = new StringBuilder();
				for (WebsiteOrder o : ordersToChk) {
					if (o.getPoNumber() == null) {
						tmp.setPoNumber(Integer.valueOf(o.getOrderId()).toString());
					} else {
						tmp.setPoNumber(o.getPoNumber());
					}
					idx = invoices.indexOf(tmp);
					if (idx > -1) {
						comments.delete(0, comments.length());
						comments.append("Shipping Done By: ").append(invoices.get(idx).getShippingDescription().replaceAll("SHIPPING CHARGE", ""))
							.append(",\nShipping Reference Number: ").append(invoices.get(idx).getShippingReferenceId())
							.append(".\n\n");
						jdbcTemplate.getJdbcOperations().update(updateQuery, status, comments.toString(), o.getOrderId());
						logger.debug("Received shipping details "+invoices.get(idx).toString()+" for order id "+o.getOrderId()+" and update the status on website accordingly.");
						result.append("Received shipping details for order id ")
							.append(o.getOrderId())
							.append(". The details are as follows-\n").append(comments);
					}
				}
			}
			else{
				logger.debug("No orders found to check invoices for from "+ordersToProcessFrom+".");
			}
		}
		else{
			logger.warn("No FTP user set for checking order invoices for "+ordersToProcessFrom);
		}
		return result.toString();
	}
}
