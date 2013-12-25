package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.jh.dao.website.WebsiteOrderSync;
import com.jh.dao.website.WebsiteProductsDAO;
import com.jh.dao.website.WebsiteProductsSync;
import com.jh.util.EdiParser;
import com.jh.util.FTPReader;
import com.jh.vo.FTPUser;
import com.jh.vo.Item;
import com.jh.vo.WebsiteOrder;
import com.jh.vo.edi.x12.EdiInvoice;

@ActiveProfiles(profiles={"website_sync"})
public class WebsiteDAOTest extends Setup {

	private static final String WEB_DEPT_SKU_FILE = "WEB_DEPT_SKU.TXT";
	private static final String WEB_SKU_COMMON_FILE = "WEB_SKU_COMMON.TXT";
	
	@Resource private WebsiteProductsDAO websiteProductsDAO;
	@Resource private NamedParameterJdbcTemplate websiteTemplate;
	@Resource private FTPReader webfilesFTPReader;
	@Resource @Qualifier("webOrderSync") private WebsiteOrderSync websiteOrderSync; 
	@Resource private WebsiteProductsSync webProductsSync;
	
	@Resource private FTPUser orgillEdiInvoiceFtpUser;	
	@Resource EdiParser ediParser;
	
	/*@Resource @Value("#{'${jhplugins.localwarehouse.orgill}'.split(',')}")*/ private List<String> localWarehouses;
	@Value("${jhplugins.localwarehouse.orgill}")
	public void setLocalWarehouses(String lw) {
		if (!lw.isEmpty()) {
			localWarehouses = new ArrayList<String>(Arrays.asList(lw.split(",")));
		}
	}
	
	
	
//	@Test
	public void test() throws IOException {
		List<Item> list = websiteProductsDAO.getDeletedProducts();
		for (Item item : list) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
	public void testPersist() throws IOException {
//		websiteProductsDAO.parseAndPersistWebFiles(System.getProperty("java.io.tmpdir")+WEB_DEPT_SKU_FILE,System.getProperty("java.io.tmpdir")+WEB_SKU_COMMON_FILE);
//		System.out.println(System.getProperty("java.io.tmpdir"));
		websiteProductsDAO.downloadParseAndPersistWebCategories();
	}
	
//	@Test
	public void testGodaddyDbConnection() {
		Assert.assertNotNull(websiteTemplate);
		System.out.println(websiteTemplate.getJdbcOperations().queryForInt("select count(*) from product"));;
		System.out.println(websiteTemplate.getJdbcOperations().update("UPDATE product SET STATUS = 0 WHERE product_id in (53)"));
	}
	
//	@Test
	public void testWebItemManager() {
		/*for (Item item : websiteProductsDAO.getWebsiteItemsToDisableAfterSync()) {
			System.out.println(item.toString());
		}*/
		for (Item item : websiteProductsDAO.getWebsiteItemsToActivateAfterSync()) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
	public void testWebItemsToOrder() {
		for (WebsiteOrder o : websiteProductsDAO.getWebsiteOrder()) {
			for (Item i : o.getItemsOnOrder()) {
				System.out.println(o.getOrderId()+" "+o.isFulfillOrderAtStore()+/*" "+i.toString()+*/" "+o.getShippingAddress()+" "+o.getOrderStatus()+" "+o.getFirstName()+" "+o.getLastName());
			}
		}
	}
	
//	@Test
	public void testInventory() throws IOException {
	/*	websiteProductsDAO.downloadParseAndPersistWebCategories();
		websiteProductsDAO.downloadParseAndPersistWebFiles();
		websiteProductsDAO.downloadParseAndUpdateWebInventory();
		webfilesFTPReader.downloadWebFile("skudescadd.txt");
		CSVReader reader = new CSVReader(new FileReader("E:/Personal/shabbir ezzi website related/plugins related/webfiles/skudescadd.txt"),'~',CSVWriter.NO_QUOTE_CHARACTER);
		String[] csvLine = null;
		List<String[]> data = new ArrayList<String[]>();
		while ((csvLine = reader.readNext()) != null) {
			System.out.println(Arrays.toString(csvLine));
		}
		reader.close();*/
//		webProductsSync.parseAndUpdateWebInventory("E:/Personal/shabbir ezzi website related/plugins related/webfiles/WEB_INVENTORY.TXT");
//		System.out.println(localWarehouses.get(0));
		websiteProductsDAO.downloadParseAndUpdateWebInventory();
	}
	
//	@Test
	public void testEDIAcks() throws Exception {
		websiteProductsDAO.checkAndUpdateOrderAcknowledgement("orgill");
//		websiteOrderSync.checkAndUpdateOrderAcknowledgement();
	}
	
	@Test
	public void testEDIInvoices() throws Exception {
		/*List<String> filenames = webfilesFTPReader.downloadFTPFiles(orgillEdiInvoiceFtpUser);
		List<EdiInvoice> invoices = ediParser.extractEdiInvoices(orgillEdiInvoiceFtpUser.getLocalFolderPath(), filenames);
		for (EdiInvoice ediInvoice : invoices) {
			System.out.println(ediInvoice.toString());
		}*/
		String result = websiteOrderSync.checkAndUpdateOrderShipping("orgill");
		System.out.println(result);
	}
}
