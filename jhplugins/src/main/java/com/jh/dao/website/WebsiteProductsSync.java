package com.jh.dao.website;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.jh.util.EmailSenderUtil;
import com.jh.util.FTPWriter;
import com.jh.vo.FTPUser;
import com.jh.vo.RequestVO;
import com.jh.vo.WebCategory;
import com.jh.vo.WebProduct;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class WebsiteProductsSync extends WebsiteProductsDAOImpl {

	private static Logger logger = Logger.getLogger(WebsiteProductsSync.class);
	
	private static final int CHECK_SIZE = 500;
	private static final int BATCH_SIZE = 200;
	
	private static Pattern replaceAndPattern = Pattern.compile("( [Aa][Nn][Dd] )");
	
	private StringBuilder errorLog = new StringBuilder(), emailText = new StringBuilder();
	
	private EmailSenderUtil emailSenderUtil;
	public void setEmailSenderUtil(EmailSenderUtil emailSenderUtil) {
		this.emailSenderUtil = emailSenderUtil;
	}
	
	private boolean isEmailLogs;
	public void setEmailLogs(boolean isEmailLogs) {
		this.isEmailLogs = isEmailLogs;
	}
	
	private boolean isNotifyOnError;
	public void setNotifyOnError(boolean isNotifyOnError) {
		this.isNotifyOnError = isNotifyOnError;
	}
	
	private String productImagePath;
	public void setProductImagePath(String productImagePath) {
		this.productImagePath = productImagePath;
	}
	
	private FTPUser websiteImageFTPUser;
	public void setWebsiteImageFTPUser(FTPUser websiteImageFTPUser) {
		this.websiteImageFTPUser = websiteImageFTPUser;
	}
	
	private FTPWriter ftpWriter;
	public void setFtpWriter(FTPWriter ftpWriter) {
		this.ftpWriter = ftpWriter;
	}

	public void downloadParseAndSyncAllFiles() {
		try {
//			getWebfilesFTPReader().connect(0);
			
			logger.debug("Start : Dowloading, parsing & syncing categories");
			downloadParseAndSyncCategories();
			logger.debug("End : Dowloading, parsing & syncing categories");
			
			logger.debug("Start : Syncing web categories");
			checkAndSyncWebCategories();
			logger.debug("End : Syncing web categories");
			
			logger.debug("Start : Dowloading, parsing & syncing products");
			downloadParseAndSyncProducts();
			logger.debug("End : Dowloading, parsing & syncing products");
			
			logger.debug("Start : Updating product inventory");
			downloadParseAndUpdateWebInventory();
			logger.debug("End : Updating product inventory");
			
			logger.debug("Start : Syncing web products");
			checkAndSyncWebProducts();
			logger.debug("End : Syncing web products");
			
//			getWebfilesFTPReader().disconnect();
			if (isEmailLogs && emailText.length() > 0) {
				emailSenderUtil.sendEmail("Jamaica Hardware - website sync logs", emailText.toString());
				emailText.delete(0, emailText.length());
			}
		} catch (Exception e) {
			logger.error("",e);
			if (isNotifyOnError) {
				errorLog.append("Error occurred while syncing website [").append(ExceptionUtils.getStackTrace(e)).append("].\n\n");
			}
		}
		if (isNotifyOnError && errorLog.length() > 0) {
			emailSenderUtil.sendNoficationEmail("Jamaica Hardware - website sync error logs", errorLog.toString());
			errorLog.delete(0, errorLog.length());
		}
	}
	
	private void downloadParseAndSyncCategories() {
		try {
			String filename = getWebfilesFTPReader().downloadWebFile("WEB_DEPT.TXT");
			CSVReader reader = new CSVReader(new FileReader(filename),'~',CSVWriter.NO_QUOTE_CHARACTER);
			String[] csvLine = null;
			List<WebCategory> data = new ArrayList<WebCategory>();
			WebCategory cate = null;
			int chkCnt = 0;
			StringBuilder sb = new StringBuilder(), newCategoryList = new StringBuilder(), cateToChk = new StringBuilder();
			List<Object[]> toCreateList = new ArrayList<Object[]>();
			boolean isCreated = false;
			while ((csvLine = reader.readNext()) != null) {
				sb.replace(0,sb.length(),csvLine[0].trim()).append(csvLine[1].trim()).append(csvLine[2].trim()).append(csvLine[3].trim()).append(csvLine[4].trim());
				cate = new WebCategory();
				cate.setCategoryId(sb.toString());
				sb.replace(0,sb.length(),csvLine[6].trim()).append(csvLine[7].trim()).append(csvLine[8].trim()).append(csvLine[9].trim()).append(csvLine[10].trim());
				cate.setParentId(sb.toString());
				cate.setCategoryName(csvLine[12].trim());
				for (int i = 0; i < 5; i++) {
					if (Integer.valueOf(csvLine[i]) > 0) {
						cate.setHierarchyLevel(cate.getHierarchyLevel()+1);
					}
				}
				data.add(cate);
				cateToChk.append("<category id=\"").append(cate.getCategoryId()).append("\" />");
				chkCnt += 1;
				if (chkCnt >= CHECK_SIZE) {
					cateToChk.insert(0, "<request>").append("</request>");
					if (checkAndInsertNewCategories(cateToChk.toString(), data, toCreateList, newCategoryList) && !isCreated) {
						isCreated = true;
					} 
					cateToChk.delete(0, cateToChk.length());
					chkCnt = 0;
				}
			}
			if (chkCnt > 0) {
				cateToChk.insert(0, "<request>").append("</request>");
				if (checkAndInsertNewCategories(cateToChk.toString(), data, toCreateList, newCategoryList) && !isCreated) {
					isCreated = true;
				}
			}
			reader.close();
			if (isCreated) {
				if (isEmailLogs) {
					emailText.append(newCategoryList).append("\n\n");
				}
				logger.debug("New categories created = \n"+newCategoryList.toString());
			}
			else{
				logger.debug("No new categories created");
			}
		} catch (Exception e) {
			logger.error("Error occurred while syncing categories.",e);
			if (isNotifyOnError) {
				errorLog.append("Error occurred while syncing categories [").append(ExceptionUtils.getStackTrace(e)).append("].\n\n");
			}
		}
	}
	
	private boolean checkAndInsertNewCategories(String cateToChk, List<WebCategory> data, List<Object[]> toCreateList, StringBuilder newCategoryList) {
		String query = "SET ARITHABORT ON; declare @request xml; set @request = ?; select cateId from (select d.value('@id','nvarchar(20)') as cateId from @request.nodes('/request/category') as ref(d)) c left join web_categories w on c.cateId = w.CATEGORY_ID where w.CATEGORY_ID is null";
		List<String> newCateList = getDynamicsTemplate().queryForList(query, String.class, cateToChk);
		Matcher replaceAndMatcher = null;
		
		for (Iterator<WebCategory> it = data.iterator(); it.hasNext();) {
			WebCategory wc = it.next();
			if (newCateList.indexOf(wc.getCategoryId()) == -1) {
				it.remove();
			}
			else{
				replaceAndMatcher = replaceAndPattern.matcher(WordUtils.capitalizeFully(wc.getCategoryName()));
				wc.setCategoryName(StringEscapeUtils.escapeHtml(replaceAndMatcher.replaceAll(" & ")));
				toCreateList.add(new Object[]{wc.getCategoryId(), wc.getCategoryName(), wc.getParentId(), wc.getHierarchyLevel()});
				newCategoryList.append(wc.getCategoryId()).append("=").append(wc.getCategoryName()).append("\n");
			}
		}
		data.clear();
		if (!toCreateList.isEmpty()) {
			query = "INSERT INTO [dbo].[web_categories]([CATEGORY_ID],[NAME],[PARENT_ID],[WEBSITE_CATEGORY_ID],HIERARCHY_LEVEL) VALUES (?,?,?,0,?)";
			getDynamicsTemplate().batchUpdate(query, toCreateList);
			toCreateList.clear();
			return true;
		}
		else{
			return false;
		}
	}
	
	private void checkAndSyncWebCategories() {
		final Timestamp currentDate = new Timestamp(new Date().getTime());
		List<WebCategory> categories2Create = null;
		final String query = "{call jh_create_category(?,?,?,?)}", 
				query2 = "update dbo.web_categories set WEBSITE_CATEGORY_ID = ?, WEBSITE_CREATION_DATE = ? where CATEGORY_ID = ?;";
		while (true) {
			categories2Create = getDynamicsTemplate().query("SELECT w.CATEGORY_ID, w.NAME as categoryName, w.PARENT_ID, w.WEBSITE_CATEGORY_ID, w.HIERARCHY_LEVEL, isnull(p.WEBSITE_CATEGORY_ID,0) as webParentId FROM web_categories w left JOIN web_categories p ON w.PARENT_ID = p.CATEGORY_ID where w.WEBSITE_CATEGORY_ID = 0 AND (w.HIERARCHY_LEVEL = 1 OR (w.HIERARCHY_LEVEL <= 3 AND (p.WEBSITE_CATEGORY_ID IS NOT NULL AND p.WEBSITE_CATEGORY_ID > 0)))", new BeanPropertyRowMapper<WebCategory>(WebCategory.class));
			if (!categories2Create.isEmpty()) {
				for (final WebCategory w : categories2Create) {
					try {
						w.setWebCategoryId(getJdbcTemplate().getJdbcOperations().queryForInt(query, w.getCategoryId(), w.getCategoryName(), w.getWebParentId(), currentDate));
						getDynamicsTemplate().update(query2, w.getWebCategoryId(), currentDate, w.getCategoryId());
					} catch (Exception e) {
						if (isNotifyOnError) {
							errorLog.append("Error occurred while creating web category ").append(w.getCategoryId()).append(" with error message [").append(e.getMessage()).append("]. \n");
						}
						logger.error("Error occurred while creating web category "+w.getCategoryId()+".",e);
					}
				}
			}
			else{
				break;
			}
		}
	}
	
	public void downloadParseAndSyncProducts() {
		try {
			StringBuilder prod2Chk = new StringBuilder(), sb = new StringBuilder();
			String[] csvLine = null;
			int chkCnt = 0;
			
			getDynamicsTemplate().update("update web_products set IS_AVAILABLE = 0, MODIFIED_DATE = current_timestamp where IS_AVAILABLE = 1");
			
			String filename = getWebfilesFTPReader().downloadWebFile("WEB_DEPT_SKU.TXT");
			CSVReader reader = new CSVReader(new FileReader(filename),'~',CSVWriter.NO_QUOTE_CHARACTER);
			while ((csvLine = reader.readNext()) != null) {
				sb.replace(0,sb.length(),csvLine[0].trim()).append(csvLine[1].trim()).append(csvLine[2].trim()).append(csvLine[3].trim()).append(csvLine[4].trim());
				prod2Chk.append("<product sku=\"").append(csvLine[6]).append("\" category=\"").append(sb.toString()).append("\" />");
				chkCnt += 1;
				if (chkCnt >= CHECK_SIZE) {
					prod2Chk.insert(0, "<request>").append("</request>");
					checkAndInsertProducts(prod2Chk.toString());
					prod2Chk.delete(0, prod2Chk.length());
					chkCnt = 0;
				}
			}
			if (chkCnt > 0) {
				prod2Chk.insert(0, "<request>").append("</request>");
				checkAndInsertProducts(prod2Chk.toString());
			}
			reader.close();
			
			List<String> products = getDynamicsTemplate().queryForList("SELECT SKU FROM web_products where IS_AVAILABLE = 2", String.class);
			
			if (!products.isEmpty()) {				
				parseAndPersistProductDescWithCallback(
						getWebfilesFTPReader().downloadWebFile("WEB_SKU_COMMON.TXT"), 
						"UPDATE [dbo].[web_products] SET [MODEL_NUMBER] = ?,[UPC] = ?,[VENDOR_CODE] = ?,[VENDOR_NAME] = ?,[IS_SHIPPING_AVAILABLE] = ?,[RETAIL_PRICE] = ?,[WEIGHT] = ?,[LENGTH] = ?,[WIDTH] = ?,[HEIGHT] = ?,[UOM] = ?,DISPLAY_NAME = ?,[MODIFIED_DATE] = current_timestamp WHERE SKU = ?", 
						products, 
						new CsvCallback() {
							@Override
							public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
								if (products2Chk.indexOf(currentCsvLine[0]) > -1) {
									data2Persist.add(new Object[]{
											currentCsvLine[1].trim(), //MODEL_NUMBER
											currentCsvLine[16].trim(), //UPC
											currentCsvLine[7].trim(), //VENDOR_CODE
											currentCsvLine[8].trim(), //VENDOR_NAME
											currentCsvLine[43].trim().equalsIgnoreCase("Y") ? 1 : 0, //IS_SHIPPING_AVAILABLE
											Float.valueOf(currentCsvLine[15].trim())/100, //RETAIL_PRICE
											Float.valueOf(currentCsvLine[6].trim()), //WEIGHT
											Float.valueOf(currentCsvLine[5].trim()), //LENGTH
											Float.valueOf(currentCsvLine[3].trim()), //WIDTH
											Float.valueOf(currentCsvLine[4].trim()), //HEIGHT
											currentCsvLine[12].trim(), //UOM
											currentCsvLine[2].trim(), //DISPLAY_NAME
											currentCsvLine[0].trim() //SKU
									});
								}
							}
						}
				);
				
				parseAndPersistProductDescWithCallback(
						getWebfilesFTPReader().downloadWebFile("WEB_SKU_DC.TXT"), 
						"UPDATE web_products SET MINIMUM_ORDER_QUANTITY = ?, MODIFIED_DATE = current_timestamp WHERE SKU = ?", 
						products, 
						new CsvCallback() {
							@Override
							public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
								if (products2Chk.indexOf(currentCsvLine[0]) > -1 && (previousCsvLine == null || !currentCsvLine[0].equalsIgnoreCase(previousCsvLine[0]))) {
									data2Persist.add(new Object[]{Integer.valueOf(currentCsvLine[2].trim()), currentCsvLine[0]});
								}
							}
						}
				);
				
				
				getDynamicsTemplate().update("update web_products set DESCRIPTION = ? where IS_AVAILABLE = 2", StringEscapeUtils.escapeHtml("<p>{PRODUCT_DESC}</p>"));
				parseAndPersistProductDescWithCallback(
						getWebfilesFTPReader().downloadWebFile("skuhead.txt"), 
						"UPDATE web_products SET DESCRIPTION = DESCRIPTION+? WHERE SKU = ?", 
						products, 
						new CsvCallback() {
							@Override
							public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
								
								if (products2Chk.indexOf(currentCsvLine[0]) > -1) {
									tmp.replace(0, tmp.length(), "");
									for (int i = 1; i < 12; i++) {
										if (currentCsvLine.length >= i && !currentCsvLine[i].trim().isEmpty()) {
											tmp.append("<li>").append(currentCsvLine[i]).append("</li>");
										}
									}
									if (tmp.length() > 0) {
										tmp.insert(0, "<ul>").append("</ul>");
										data2Persist.add(new Object[]{StringEscapeUtils.escapeHtml(tmp.toString()), currentCsvLine[0]});
									}
								}
							}
						}
				);
				
				parseAndPersistProductDescWithCallback(
						getWebfilesFTPReader().downloadWebFile("orgill_skudesc.txt"), 
						"UPDATE web_products SET DESCRIPTION = replace(DESCRIPTION,'{PRODUCT_DESC}',?) WHERE SKU = ?", 
						products, 
						new CsvCallback() {
							@Override
							public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
								
								if (products2Chk.indexOf(currentCsvLine[0]) > -1) {
									tmp.replace(0, tmp.length(), "");
									for (int i = 1; i < 4; i++) {
										if (currentCsvLine.length >= i && !currentCsvLine[i].trim().isEmpty()) {
											tmp.append(currentCsvLine[i]);
										}
									}
									if (tmp.length() > 0) {
										tmp.append("{PRODUCT_DESC}");
										data2Persist.add(new Object[]{StringEscapeUtils.escapeHtml(tmp.toString()), currentCsvLine[0]});
									}
								}
							}
						}
				);
				
				parseAndPersistProductDescWithCallback(
						getWebfilesFTPReader().downloadWebFile("skudescadd.txt"), 
						"UPDATE web_products SET DESCRIPTION = replace(DESCRIPTION,'{PRODUCT_DESC}',?) WHERE SKU = ?", 
						products, 
						new CsvCallback() {
							@Override
							public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
								
								if (products2Chk.indexOf(currentCsvLine[0]) > -1) {
									tmp.replace(0, tmp.length(), "");
									for (int i = 2; i < 5; i++) {
										if (currentCsvLine.length >= i && !currentCsvLine[i].trim().isEmpty()) {
											tmp.append(currentCsvLine[i]);
										}
									}
									if (tmp.length() > 0) {
										tmp.append("{PRODUCT_DESC}");
										data2Persist.add(new Object[]{StringEscapeUtils.escapeHtml(tmp.toString()), currentCsvLine[0]});
									}
								}
							}
						}
				);
				getDynamicsTemplate().update("update web_products set DESCRIPTION = replace(DESCRIPTION,?,'') where IS_AVAILABLE = 2", "{PRODUCT_DESC}");
			}
			parseAndPersistProductDescWithCallback(
					getWebfilesFTPReader().downloadWebFile("skudescadd.txt"), 
					"UPDATE web_products SET DESCRIPTION = replace(DESCRIPTION,'{PRODUCT_DESC}',?) WHERE SKU = ?", 
					products, 
					new CsvCallback() {
						@Override
						public void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk) {
							
							if (products2Chk.indexOf(currentCsvLine[0]) > -1) {
								tmp.replace(0, tmp.length(), "");
								for (int i = 2; i < 5; i++) {
									if (currentCsvLine.length >= i && !currentCsvLine[i].trim().isEmpty()) {
										tmp.append(currentCsvLine[i]);
									}
								}
								if (tmp.length() > 0) {
									tmp.append("{PRODUCT_DESC}");
									data2Persist.add(new Object[]{StringEscapeUtils.escapeHtml(tmp.toString()), currentCsvLine[0]});
								}
							}
						}
					}
			);
		} catch (Exception e) {
			logger.error("Error occurred while syncing products.",e);
			if (isNotifyOnError) {
				errorLog.append("Error occurred while syncing products [").append(ExceptionUtils.getStackTrace(e)).append("]. \n");
			}
		}
	}
	
	private void checkAndInsertProducts(String prod2Chk) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("SET ARITHABORT ON; ")
				.append("DECLARE @request xml; ")
				.append("SET @request = ?; ")
				.append("DECLARE @pt table (sku nvarchar(10), cateId nvarchar(20)); ")
				.append("INSERT INTO @pt ")
				.append("SELECT d.value('@sku','nvarchar(10)'), d.value('@category','nvarchar(20)') from @request.nodes('/request/product') as ref(d); ")
				.append("UPDATE w SET IS_AVAILABLE = 1, MODIFIED_DATE = current_timestamp from @pt p INNER JOIN web_products w ON p.sku = w.SKU where w.IS_AVAILABLE <> 2; ")
				.append("INSERT INTO web_products (SKU, CATEGORY_ID, IS_AVAILABLE) ")
				.append("SELECT p.sku, cateId, 2 from @pt p LEFT JOIN web_products w ON p.sku = w.SKU where w.SKU IS NULL; ")
				.append("select 1; ");
			getDynamicsTemplate().queryForInt(query.toString(), prod2Chk);
		} catch (Exception e) {
			logger.error("Error occurred while checking and creating products ("+prod2Chk+").",e);
			if (isNotifyOnError) {
				errorLog.append("Error occurred while checking and creating products (").append(prod2Chk).append(") with error message [").append(ExceptionUtils.getStackTrace(e)).append("]. \n");
			}
		}
	}
	
	private void parseAndPersistProductDescWithCallback(String filename, String query, List<String> products2Chk, CsvCallback callback) throws Exception{
		boolean isLast = false;
		CSVReader reader = new CSVReader(new FileReader(filename),'~',CSVWriter.NO_QUOTE_CHARACTER);
		String[] previousCsvLine = null, currentCsvLine = null;
		List<Object[]> data2Persist = new ArrayList<Object[]>();
		StringBuilder tmp = new StringBuilder();
		while (true) {
			if (isLast) {
				break;
			}
			isLast = (currentCsvLine = reader.readNext()) == null;
			if (!isLast) {
				callback.callbackWithProducts2Chk(currentCsvLine, previousCsvLine, tmp, data2Persist, products2Chk);
			}
			if (data2Persist.size() >= BATCH_SIZE || (isLast && !data2Persist.isEmpty())) {
				updateProductDesc(query, data2Persist);
				data2Persist.clear();
			}
			previousCsvLine = currentCsvLine;
		}
	}
	
	private void updateProductDesc(String query, List<Object[]> data) {
		getDynamicsTemplate().batchUpdate(query, data);
	}
	
	public interface CsvCallback{		
		void callbackWithProducts2Chk(String[] currentCsvLine, String[] previousCsvLine, StringBuilder tmp, List<Object[]> data2Persist, List<String> products2Chk);
	}
	
	private void checkAndSyncWebProducts() throws IOException {
		List<String> list = getDynamicsTemplate().queryForList("SELECT SKU from dbo.web_products where IS_AVAILABLE = 0 OR AVAILABLE_QUANTITY = 0", String.class);
		RequestVO request = new RequestVO();
		request.setItemsToModify(JSONValue.toJSONString(list));
		
		int cnt = deactivateWebsiteItems(request);
		logger.debug(cnt+" products successfully deactivated on website.");
		if (isEmailLogs) {
			emailText.append(cnt).append("  products successfully deactivated on website.\n\n");
		}
		
		cnt = activateWebsiteItems();
		logger.debug(cnt+" products successfully updated on website");
		if (isEmailLogs) {
			emailText.append("Quantity of ").append(cnt).append(" products successfully updated on website.\n\n");
		}
		
		cnt = addNewProducts2Website();
		if (isEmailLogs) {
			emailText.append(cnt).append(" products successfully created on website.\n\n");
		}
	}
	
	private int activateWebsiteItems(){
		List<WebProduct> list = getDynamicsTemplate().query("SELECT SKU, AVAILABLE_QUANTITY FROM web_products with (nolock) where IS_AVAILABLE = 1 AND AVAILABLE_QUANTITY > 0 AND AVAILABLE_QUANTITY <> PREVIOUS_QUANTITY", new BeanPropertyRowMapper<WebProduct>(WebProduct.class));
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		String query = "UPDATE (SUBQUERY) t inner join `product` p on p.sku = t.sku SET p.`quantity` = t.qty, `date_modified` = CURRENT_TIMESTAMP, `stock_status_id` = (SELECT `stock_status_id` FROM `stock_status` WHERE `name` = 'In Stock') ";
		for (WebProduct w : list) {
			sb.append("select '").append(w.getSku()).append("' as sku, ").append(w.getAvailableQuantity()).append(" as qty union ");
			cnt += 1;
			if (cnt >= CHECK_SIZE) {
				sb.delete(sb.lastIndexOf("union"), sb.length());
				getJdbcTemplate().getJdbcOperations().update(query.replaceFirst("SUBQUERY", sb.toString()));
				sb.delete(0, sb.length());
				cnt = 0;
			}
		}
		if (sb.length() > 0) {
			sb.delete(sb.lastIndexOf("union"), sb.length());
		}
		return list.size();
	}
	
	public int addNewProducts2Website() {
		FTPClient websiteImageFTPClient = null;
		StringBuilder query = new StringBuilder()
			.append("SET ARITHABORT ON; ")
			.append("DECLARE @pt table (sku nvarchar(10), categoryId nvarchar(20)); ")
			.append("INSERT INTO @pt  ")
			.append("SELECT SKU, CATEGORY_ID from web_products WITH (NOLOCK) where IS_AVAILABLE = 2 AND (IS_ON_WEBSITE IS NULL OR IS_ON_WEBSITE = 0); ")
			.append("DECLARE @request xml; ")
			.append("SET @request = (SELECT DISTINCT categoryId as \"@id\" FROM @pt FOR XML PATH ('category'),ROOT('request')); ")
			.append("DECLARE @ct table (categoryId nvarchar(20), parentWebIds nvarchar(MAX)); ")
			.append("INSERT INTO @ct ")
			.append("SELECT * FROM dbo.GetWebCategories(@request); ")
			.append("SELECT w.*, parentWebIds from @pt p INNER JOIN @ct c ON p.categoryId = c.categoryId INNER JOIN web_products w WITH (NOLOCK) ON w.SKU = p.sku  ");
		
		List<WebProduct> list = getDynamicsTemplate().query(query.toString(), new BeanPropertyRowMapper<WebProduct>(WebProduct.class));
		String imagePath = websiteImageFTPUser.getFtpFolderPath().replaceFirst("./image/", "")+(websiteImageFTPUser.getFtpFolderPath().endsWith("/") ? "" : "/");
		StringBuilder tmp = new StringBuilder();
		String[] pcIds = null;
		for (WebProduct w : list) {
			try {
				websiteImageFTPClient = ftpWriter.connect(websiteImageFTPUser);
				downloadAndUploadImageToWebsite(w.getSku(), websiteImageFTPClient);
				ftpWriter.disconnect(websiteImageFTPClient);
				logger.debug("Checked & uploaded image for sku "+w.getSku());
				if (w.getParentWebIds() != null) {
					tmp.replace(0, tmp.length(), "INSERT INTO `product_to_category` (`product_id`,`category_id`)  ")
						.append("SELECT product_id, categoryId FROM product p JOIN ( ");
					pcIds = w.getParentWebIds().split(",");
					for (int i = 0; i < pcIds.length; i++) {
						tmp.append("SELECT ").append(pcIds[i]).append(" AS categoryId ");
						if (i < pcIds.length-1) {
							tmp.append(" UNION ");
						}
					}
					tmp.append(" ) c WHERE p.product_id = ?;");
				}
				else{
					tmp.replace(0, tmp.length(), "update product set status = 1 where product_id = ?;");
				}
				getJdbcTemplate().getJdbcOperations().queryForInt("{call jh_create_product(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}",
					w.getModelNumber(),
					w.getSku(),
					w.getUpc(),
					w.getAvailableQuantity(),
					imagePath+w.getSku()+".jpg",
					w.getVendorName(),
					w.getRetailPrice(),
					w.getWeight(),
					w.getLength(),
					w.getWidth(),
					w.getHeight(),
					w.getMinimumOrderQuantity(), 
					w.getDisplayName(),
					w.getDescription(),
					tmp.toString()
				);
				getDynamicsTemplate().update("update dbo.web_products set IS_AVAILABLE = 1, IS_ON_WEBSITE = 1, MODIFIED_DATE = current_timestamp where SKU = ?", w.getSku());
				logger.debug("Created product with sku "+w.getSku()+" on website.");
				if (isEmailLogs) {
					emailText.append("Product with sku ").append(w.getSku()).append(" successfully created on website.\n");
				}
			} catch (Exception e) {
				logger.error("Error occurred while creating product with sku "+w.getSku()+". ",e);
				if (isNotifyOnError) {
					errorLog.append("Error occurred while creating product with sku ").append(w.getSku()).append(" with error message [").append(e.getLocalizedMessage()).append("]. \n");
				}
			}
		}
		return list.size();
	}
	
	private void downloadAndUploadImageToWebsite(String sku, FTPClient client) {
		String image = sku+".jpg";
		try {
			String imageUrl = getCommonUtil().getOrgillImageUrl(image);
			String imageFile = productImagePath+(productImagePath.endsWith("//")? "" : "//")+image;
			File file = new File(imageFile);
			if (!file.isFile()) {
				FileUtils.copyURLToFile(new URL(imageUrl), file);
			}
			ftpWriter.checkAndUploadFile2FTP(websiteImageFTPUser, client, file);
		} catch (Exception e) {
			logger.error("Error ocurred while uploading image of sku "+sku+" ["+e.getLocalizedMessage()+"].");
			if (isNotifyOnError) {
				errorLog.append("Error ocurred while uploading image of sku = ").append(sku).append(" with error message [").append(e.getLocalizedMessage()).append("]. \n");
			}
		}
	}
}
