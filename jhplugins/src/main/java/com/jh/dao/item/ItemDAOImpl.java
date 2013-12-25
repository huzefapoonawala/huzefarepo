package com.jh.dao.item;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.jh.util.CommonUtil;
import com.jh.vo.Item;
import com.jh.vo.ReasonCode;
import com.jh.vo.RequestVO;

public class ItemDAOImpl extends JdbcDaoSupport implements ItemDAO{

	private static Logger logger = Logger.getLogger(ItemDAOImpl.class);
	
	private CommonUtil commonUtil;	
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}
	
	@Override
	public List<Item> getItemDetails(RequestVO request) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(request.getSku())) {
			sb.append("<sku>").append(request.getSku()).append("</sku>");
		}
		if (StringUtils.isNotBlank(request.getAlias())) {
			sb.append("<alias>").append(request.getAlias()).append("</alias>");
		}
		sb.insert(0, "<request>").append("</request>");
		return getItemDetails(sb.toString());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Item> getItemDetails(String requestXml) {
		logger.debug("Request = "+requestXml);
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("GetItemDetails")
//				.declareParameters(new SqlParameter("supplierId", Types.NVARCHAR))
				.returningResultSet("items", new BeanPropertyRowMapper<Item>(Item.class));
		Map<String, Object> map = jdbcCall.execute(requestXml);
		List<Item> items = (List<Item>)map.get("items");
		for (Item item : items) {
			if (item.getImage() == null) {
				item.setImage(commonUtil.getImageUrl(item.getSku(),"jpg"));
			}
			else{
				item.setImage(commonUtil.getImageUrl(item.getImage()));
			}
		}
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateItems(final RequestVO request) {
		List<Map<String, Object>> list = (List)JSONValue.parse(request.getItemsToModify());
		for (final Map<String, Object> map : list) {
			getJdbcTemplate().update(
					"UPDATE Item SET Price = "+(request.getUpdateRetailPrice() ? "?" : "Price")+", Cost = "+(request.getUpdateCostPrice() ? "?" : "Cost")+", Quantity = ?, RestockLevel = ?, ReorderPoint = ?, Description = ? WHERE ID = ?", 
					/*map.get("retailPrice"),
					map.get("costPrice"),
					map.get("stockQuantity"),
					map.get("restockLevel"),
					map.get("reorderLevel"),
					map.get("description"),
					map.get("id")*/
					new PreparedStatementSetter(){

						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							int idx = 1;
							if (request.getUpdateRetailPrice()) {
								ps.setString(idx++, map.get("retailPrice").toString());
							}
							if (request.getUpdateCostPrice()) {
								ps.setString(idx++, map.get("costPrice").toString());
							}							
							ps.setString(idx++, map.get("stockQuantity").toString());
							ps.setString(idx++, map.get("restockLevel").toString());
							ps.setString(idx++, map.get("reorderLevel").toString());
							ps.setString(idx++, map.get("description").toString());
							ps.setString(idx++, map.get("id").toString());
							
						}
						
					}
			);
			if (map.containsKey("reasonCodeId") && map.containsKey("changeQuantity")) {
				getJdbcTemplate().update(
					"INSERT INTO [dbo].[InventoryTransferLog] ([ItemID],[DetailID],[Quantity],[DateTransferred],[StoreID],[ReferenceID],[ReasonCodeID],[CashierID],[Type],[ReferenceEntryID],[Cost],[BatchNumber]) VALUES (?,0,?,current_timestamp,0,0,?,1,5,0,?,0)",
					map.get("id"),
					map.get("changeQuantity"),
					map.get("reasonCodeId"),
					map.get("costPrice")
				);
			}
		}
	}
	
	@Override
	public Boolean checkAliasUniqueness(RequestVO request) {
		return getJdbcTemplate().queryForInt("SELECT sum(cnt) as cnt FROM (select count(*) as cnt from Alias where Alias = ? UNION all SELECT count(*) as cnt from Item where ItemLookupCode = ?) t", 
					request.getAlias(),
					request.getAlias()
				) == 0;		
	}
	
	@Override
	public void addAlias(RequestVO request) {
		getJdbcTemplate().update("INSERT INTO [Alias]([ItemID],[Alias]) VALUES (?,?)", 
				request.getItemId(),
				request.getAlias());
	}
	
	@Override
	public void deleteAlias(RequestVO request) {
		getJdbcTemplate().update("delete from [Alias] where Alias = ?", 
				request.getAlias());
	}
	
	@Override
	public List<ReasonCode> getReasonCodes() {
		return getJdbcTemplate().query("SELECT ID,Code,Description FROM ReasonCode", new BeanPropertyRowMapper<ReasonCode>(ReasonCode.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deactivateItems(RequestVO request) {
		List<String> list = (List)JSONValue.parse(request.getItemsToModify());
		if (!list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String sku : list) {
				sb.append("'").append(sku).append("',");
			}
			sb.deleteCharAt(sb.length()-1);
			getJdbcTemplate().update("update Item set BlockSalesType = 1, BlockSalesReason = 'Discontinued.', BlockSalesAfterDate = dateadd(day,-1,current_timestamp) where ItemLookupCode in (SKUS)".replaceFirst("SKUS", sb.toString()));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addOrActivateItems(RequestVO request, String imagePath) {
		/*Integer supplierId = 0;
		for (Integer sId : getJdbcTemplate().queryForList("select top 1 ID from dbo.Supplier where SupplierName like '%ORGILL%'", Integer.class)) {
			supplierId = sId;
		}*/
		String queryString = "if(select count(*) from dbo.Item where ItemLookupCode = ?) = 0 "+
				" INSERT INTO [dbo].[Item]([BinLocation],[BuydownPrice],[BuydownQuantity],[CommissionAmount],[CommissionMaximum],[CommissionMode],[CommissionPercentProfit],[CommissionPercentSale],[Description],[FoodStampable],[HQID],[ItemNotDiscountable],[LastReceived],[LastUpdated],[Notes],[QuantityCommitted],[SerialNumberCount],[TareWeightPercent],[ItemLookupCode],[DepartmentID],[CategoryID],[MessageID],[Price],[PriceA],[PriceB],[PriceC],[SalePrice],[SaleStartDate],[SaleEndDate],[QuantityDiscountID],[TaxID],[ItemType],[Cost],[Quantity],[ReorderPoint],[RestockLevel],[TareWeight],[SupplierID],[TagAlongItem],[TagAlongQuantity],[ParentItem],[ParentQuantity],[BarcodeFormat],[PriceLowerBound],[PriceUpperBound],[PictureName],[LastSold],[ExtendedDescription],[SubDescription1],[SubDescription2],[SubDescription3],[UnitOfMeasure],[SubCategoryID],[QuantityEntryNotAllowed],[PriceMustBeEntered],[BlockSalesReason],[BlockSalesAfterDate],[Weight],[Taxable],[BlockSalesBeforeDate],[LastCost],[ReplacementCost],[WebItem],[BlockSalesType],[BlockSalesScheduleID],[SaleType],[SaleScheduleID],[Consignment],[LastCounted],[DoNotOrder],[MSRP],[DateCreated],[Content],[UsuallyShip]) " +
				" SELECT '',0,0,0,0,0,0,0,NOMENCLATURE_EXT,0,0,0,NULL,current_timestamp,NULL,0,0,0,SKU,d.ID,c.ID,0,w.REG_PRICE/100,0,0,0,0,NULL,NULL,0,2,0,ADV_PRICE/100,0,0,0,0,isnull(s.ID,0),0,0,0,0,3,0,0,w.IMAGE,NULL,'',FACTORY,'','',substring(w.RETAIL_UNIT_OF_MEASURE,0,5),0,0,0,'',NULL,0,1,NULL,0,0,0,0,0,0,0,0,NULL,0,0,current_timestamp,'','' from dbo.WebItem w LEFT JOIN dbo.Department d ON d.code = w.DEPT_ID LEFT JOIN dbo.Category c ON c.Code = w.CATEGORY_ID OUTER APPLY(SELECT TOP 1 ID from dbo.Supplier where SupplierName LIKE '%ORGILL%') s WHERE w.SKU = ? "+
				" else "+
					" update [dbo].[Item] set BlockSalesType = 0, BlockSalesReason = '', BlockSalesAfterDate = null where ItemLookupCode = ?; " +
				" INSERT INTO SupplierList (MinimumOrder,ItemID,SupplierID,Cost,ReorderNumber,MasterPackQuantity,TaxRate) select 0, ID, SupplierID, Cost, '', 0, 0 from Item where ItemLookupCode = ?; " +
				" IF(select count(*) from Alias where Alias in (SELECT UPC_CODE from WebItem where SKU = ?)) = 0 " +
					" INSERT INTO Alias (ItemID,Alias) SELECT TOP 1 i.ID, w.UPC_CODE from WebItem w inner JOIN Item i ON w.SKU = i.ItemLookupCode and w.SKU = ?; ";
		List<String> list = (List<String>)JSONValue.parse(request.getItemsToModify());
		int recordsPerBatch = 300, size = list.size()/recordsPerBatch;
		if (list.size()%recordsPerBatch > 0) {
			size +=1;
		}
		logger.debug("Total batch size for adding new items = "+size);
		logger.debug("Start: Adding new items");
		for (int i = 0; i < size; i++) {
			addItemsInBatch(queryString, list.subList(i*recordsPerBatch, i == size-1 ? list.size() : ((i*recordsPerBatch)+recordsPerBatch)));
		}
		String imageUrl = null, imageFile = null;
		for (String sku : list) {
			try {
				imageUrl = commonUtil.getOrgillImageUrl(sku)+".jpg";
				imageFile = imagePath+(imagePath.endsWith("//")? "" : "//")+sku+".jpg";
				FileUtils.copyURLToFile(new URL(imageUrl), new File(imageFile));
			} catch (Exception e) {
				logger.error("Could not find image "+imageUrl);
			}
		}
		logger.debug("End: Adding new items");
	}
	
	private void addItemsInBatch(final String queryString, final List<String> data) {
		getJdbcTemplate().batchUpdate(queryString, new BatchPreparedStatementSetter(){

			@Override
			public int getBatchSize() {
				return data.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int rowNum)
					throws SQLException {
				ps.setString(1, data.get(rowNum));
				ps.setString(2, data.get(rowNum));
				ps.setString(3, data.get(rowNum));
				ps.setString(4, data.get(rowNum));
				ps.setString(5, data.get(rowNum));
				ps.setString(6, data.get(rowNum));
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addDepartments(RequestVO request) {
		List<Map<String, Object>> list = (List)JSONValue.parse(request.getItemsToModify());
		String queryString = "UPDATE dbo.Department SET code = ? WHERE Name = ?; " +
				"if(@@rowcount = 0)	" +
					"insert INTO dbo.Department(Name,code) VALUES (?,?);";
		for (Map<String, Object> map : list) {
			getJdbcTemplate().update(queryString, map.get("deptCode"), map.get("deptName"), map.get("deptName"), map.get("deptCode"));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addCategories(RequestVO request) {
		List<Map<String, Object>> list = (List)JSONValue.parse(request.getItemsToModify());
		String queryString = "UPDATE dbo.Category SET code = ? WHERE DepartmentID = ? AND Name = ? AND Code = ''; " +
				"if(@@rowcount = 0) " +
					"insert INTO dbo.Category(Code,DepartmentID,Name) VALUES (?,?,?);";
		for (Map<String, Object> map : list) {
			getJdbcTemplate().update(queryString, map.get("categoryCode"), map.get("deptId"), map.get("categoryName"), map.get("categoryCode"), map.get("deptId"), map.get("categoryName"));
		}
	}
}
