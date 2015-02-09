package com.dynamicsext.bdu.processor;

import java.io.FileReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;

import com.dynamicsext.bdu.util.InputReader;

@Component(value="dataProcessor")
public class DataProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(DataProcessor.class);
	
	@Autowired private InputReader inputReader;
	@Autowired private JdbcTemplate dynamicsTemplate;
	
	public void readAndProcessData2Upload() {
		String fileName2Upload = inputReader.getSupplierFile2Upload();
		LOG.info("File name consisting supplier list to upload = "+fileName2Upload);
		CSVReader reader = null;
		String [] csvLine = null;
		int lineNo = 1;
		if (StringUtils.isNotBlank(fileName2Upload)) {
			try {
				reader = new CSVReader(new FileReader(fileName2Upload),',','"',1);
				while ((csvLine = reader.readNext()) != null) {
					lineNo += 1;
					try {
						int count = dynamicsTemplate.update("if (select count(*) from dbo.Supplier where supplierName = ?) = 0 insert into dbo.Supplier (supplierName) values (?) ", csvLine[0], csvLine[0]);
						if (count > 0) {
							LOG.info("Persisted supplier with name '"+csvLine[0]+"'.");
						}
						else{
							LOG.info("Ignored persisting supplier with name '"+csvLine[0]+"' as it already exist.");
						}
					} catch (Exception e) {
						LOG.error("Error occurred while processing line number "+lineNo+".",e);
					}
				}
				reader.close();
			} catch (Exception e) {
				LOG.error("Error occurred while reading and persisting new supplier list.",e);
			}		
		}
		fileName2Upload = inputReader.getProductFile2Upload();
		lineNo = 1;
		LOG.info("File name consisting product list to upload = "+fileName2Upload);
		if (StringUtils.isNotBlank(fileName2Upload)) {
			try {
				String productQuery = "if(select count(*) from dbo.Item where ItemLookupCode = ?) = 0 "+
						" INSERT INTO [dbo].[Item]([BinLocation],[BuydownPrice],[BuydownQuantity],[CommissionAmount],[CommissionMaximum],[CommissionMode],[CommissionPercentProfit],[CommissionPercentSale],[Description],[FoodStampable],[HQID],[ItemNotDiscountable],[LastReceived],[LastUpdated],[Notes],[QuantityCommitted],[SerialNumberCount],[TareWeightPercent],[ItemLookupCode],[DepartmentID],[CategoryID],[MessageID],[Price],[PriceA],[PriceB],[PriceC],[SalePrice],[SaleStartDate],[SaleEndDate],[QuantityDiscountID],[TaxID],[ItemType],[Cost],[Quantity],[ReorderPoint],[RestockLevel],[TareWeight],[SupplierID],[TagAlongItem],[TagAlongQuantity],[ParentItem],[ParentQuantity],[BarcodeFormat],[PriceLowerBound],[PriceUpperBound],[PictureName],[LastSold],[ExtendedDescription],[SubDescription1],[SubDescription2],[SubDescription3],[UnitOfMeasure],[SubCategoryID],[QuantityEntryNotAllowed],[PriceMustBeEntered],[BlockSalesReason],[BlockSalesAfterDate],[Weight],[Taxable],[BlockSalesBeforeDate],[LastCost],[ReplacementCost],[WebItem],[BlockSalesType],[BlockSalesScheduleID],[SaleType],[SaleScheduleID],[Consignment],[LastCounted],[DoNotOrder],[MSRP],[DateCreated],[Content],[UsuallyShip]) " +
						" values (?,0,0,0,0,0,0,0,?,0,0,0,NULL,current_timestamp,NULL,0,0,0,?,isnull((select top 1 ID from Department where Name = ?),0),isnull((select top 1 ID from Category where Name = ?),0),0,?,0,0,0,0,NULL,NULL,0,(select top 1 ID from ItemTax with (NOLOCK) order by ID),0,?,0,0,0,0,isnull((select top 1 ID from Supplier where SupplierName = ?),0),0,0,0,0,3,0,0,?,NULL,'','','','',substring(?,0,5),0,0,0,'',NULL,0,1,NULL,0,0,0,0,0,0,0,0,NULL,0,0,current_timestamp,'',''); "+
						" INSERT INTO SupplierList (MinimumOrder,ItemID,SupplierID,Cost,ReorderNumber,MasterPackQuantity,TaxRate) select 0, ID, SupplierID, Cost, '', 0, 0 from Item where ItemLookupCode = ?; " ,
				aliasQuery =" IF(select count(*) from Alias where Alias = ?) = 0 " +
						" INSERT INTO Alias (ItemID,Alias) SELECT TOP 1 i.ID, ? from Item i where i.ItemLookupCode = ?";
				reader = new CSVReader(new FileReader(fileName2Upload),',','"',1);
				while ((csvLine = reader.readNext()) != null) {
					lineNo += 1;
					try {
						int count = dynamicsTemplate.update(productQuery, csvLine[0], csvLine[9], csvLine[3], csvLine[0], csvLine[1], csvLine[2], csvLine[4], csvLine[5], csvLine[6], csvLine[8], csvLine[7], csvLine[0]);
						if (count > 0) {
							if (StringUtils.isNotBlank(csvLine[10])) {
								for (String alias : csvLine[10].split("\\|")) {
									if (StringUtils.isNotBlank(alias)) {
										dynamicsTemplate.update(aliasQuery, alias, alias, csvLine[0]);
									}
								}
							}
							LOG.info("Persisted product with sku '"+csvLine[0]+"'.");
						}
						else{
							LOG.info("Ignored persisting product with sku '"+csvLine[0]+"' as it already exist.");
						}
					} catch (Exception e) {
						LOG.error("Error occurred while processing product on line number "+lineNo+".",e);
					}
				}
				reader.close();
			} catch (Exception e) {
				LOG.error("Error occurred while reading and persisting new product list.",e);
			}		
		}
	}
}
