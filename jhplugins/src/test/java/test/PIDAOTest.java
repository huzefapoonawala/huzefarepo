package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

import com.jh.dao.pi.PurchaseInvoiceDAO;
import com.jh.vo.RequestVO;

public class PIDAOTest extends Setup {

	@Resource private PurchaseInvoiceDAO purchaseInvoiceDAO;
	
	@Test
	public void testValidateInvoice() throws Exception {
//		purchaseInvoiceDAO.validateInvoice(1, new File("E:\\Personal\\shabbir ezzi website related\\plugins related\\Invoice Uploader\\ftp file\\082812-081877.OIN"));
		System.out.println(purchaseInvoiceDAO.validateInvoice(new File("/Users/hpoonaw/Personal/Laptop Backups/ICT-Dell-2016_11_22/HP_Data_D_2016-11-22/Personal/Work/shabbir ezzi website related/plugins related/Invoice Uploader/ftp file/testlatest.oin")));
	}
	
//	@Test
	public void saveInvoiceTest() {
		RequestVO request = new RequestVO();
		request.setSupplierId(1);
		request.setRequisitioner("huzefa");
		request.setInvoiceData("[{\"billTo\":\"353987\",\"invoiceDate\":\"083012\",\"invoiceDetails\":[{\"retailPrice\":\"4.99\",\"costPrice\":\"2.55\",\"lineNumber\":\"0001\",\"quantity\":6,\"sku\":\"7882426\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"3.33\",\"lineNumber\":\"0002\",\"quantity\":6,\"sku\":\"2457232\"},{\"retailPrice\":\"12.99\",\"costPrice\":\"7.44\",\"lineNumber\":\"0003\",\"quantity\":2,\"sku\":\"8565681\"},{\"retailPrice\":\"11.99\",\"costPrice\":\"6.50\",\"lineNumber\":\"0004\",\"quantity\":2,\"sku\":\"6109946\"},{\"retailPrice\":\"9.19\",\"costPrice\":\"6.77\",\"lineNumber\":\"0005\",\"quantity\":4,\"sku\":\"1745710\"},{\"retailPrice\":\"5.79\",\"costPrice\":\"3.28\",\"lineNumber\":\"0006\",\"quantity\":4,\"sku\":\"4168068\"},{\"retailPrice\":\"5.79\",\"costPrice\":\"3.28\",\"lineNumber\":\"0007\",\"quantity\":4,\"sku\":\"6104244\"},{\"retailPrice\":\"6.99\",\"costPrice\":\"3.91\",\"lineNumber\":\"0008\",\"quantity\":6,\"sku\":\"6967244\"},{\"retailPrice\":\"25.99\",\"costPrice\":\"19.04\",\"lineNumber\":\"0009\",\"quantity\":6,\"sku\":\"4812558\"},{\"retailPrice\":\"16.99\",\"costPrice\":\"8.89\",\"lineNumber\":\"0010\",\"quantity\":6,\"sku\":\"5886346\"},{\"retailPrice\":\"16.99\",\"costPrice\":\"9.49\",\"lineNumber\":\"0011\",\"quantity\":6,\"sku\":\"0698837\"},{\"retailPrice\":\"16.99\",\"costPrice\":\"8.18\",\"lineNumber\":\"0012\",\"quantity\":6,\"sku\":\"0677203\"},{\"retailPrice\":\"7.50\",\"costPrice\":\"4.61\",\"lineNumber\":\"0013\",\"quantity\":5,\"sku\":\"9679887\"},{\"retailPrice\":\"9.99\",\"costPrice\":\"4.58\",\"lineNumber\":\"0014\",\"quantity\":5,\"sku\":\"2208239\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.11\",\"lineNumber\":\"0015\",\"quantity\":12,\"sku\":\"7035967\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.11\",\"lineNumber\":\"0016\",\"quantity\":12,\"sku\":\"1849561\"},{\"retailPrice\":\"3.89\",\"costPrice\":\"1.81\",\"lineNumber\":\"0017\",\"quantity\":24,\"sku\":\"2168623\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.56\",\"lineNumber\":\"0018\",\"quantity\":20,\"sku\":\"6159230\"},{\"retailPrice\":\"1.39\",\"costPrice\":\"0.79\",\"lineNumber\":\"0019\",\"quantity\":12,\"sku\":\"6780381\"},{\"retailPrice\":\"16.99\",\"costPrice\":\"13.73\",\"lineNumber\":\"0020\",\"quantity\":5,\"sku\":\"6144836\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.73\",\"lineNumber\":\"0021\",\"quantity\":6,\"sku\":\"6911663\"},{\"retailPrice\":\"2.89\",\"costPrice\":\"1.33\",\"lineNumber\":\"0022\",\"quantity\":5,\"sku\":\"6911853\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"1.86\",\"lineNumber\":\"0023\",\"quantity\":6,\"sku\":\"6391387\"},{\"retailPrice\":\"3.29\",\"costPrice\":\"1.47\",\"lineNumber\":\"0024\",\"quantity\":6,\"sku\":\"6142111\"},{\"retailPrice\":\"1.15\",\"costPrice\":\"0.50\",\"lineNumber\":\"0025\",\"quantity\":50,\"sku\":\"6141584\"},{\"retailPrice\":\"46.99\",\"costPrice\":\"20.88\",\"lineNumber\":\"0026\",\"quantity\":2,\"sku\":\"4506358\"},{\"retailPrice\":\"1.49\",\"costPrice\":\"0.52\",\"lineNumber\":\"0027\",\"quantity\":25,\"sku\":\"6141741\"},{\"retailPrice\":\"1.99\",\"costPrice\":\"0.69\",\"lineNumber\":\"0028\",\"quantity\":20,\"sku\":\"6319123\"},{\"retailPrice\":\"4.29\",\"costPrice\":\"1.93\",\"lineNumber\":\"0029\",\"quantity\":3,\"sku\":\"3979549\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.24\",\"lineNumber\":\"0030\",\"quantity\":2,\"sku\":\"8907636\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.74\",\"lineNumber\":\"0031\",\"quantity\":5,\"sku\":\"6205421\"},{\"retailPrice\":\"2.29\",\"costPrice\":\"0.93\",\"lineNumber\":\"0032\",\"quantity\":10,\"sku\":\"1232966\"},{\"retailPrice\":\"2.39\",\"costPrice\":\"0.86\",\"lineNumber\":\"0033\",\"quantity\":16,\"sku\":\"1335009\"},{\"retailPrice\":\"1.99\",\"costPrice\":\"0.58\",\"lineNumber\":\"0034\",\"quantity\":5,\"sku\":\"4202024\"},{\"retailPrice\":\"1.99\",\"costPrice\":\"0.58\",\"lineNumber\":\"0035\",\"quantity\":5,\"sku\":\"4614681\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"2.23\",\"lineNumber\":\"0036\",\"quantity\":6,\"sku\":\"1104660\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.77\",\"lineNumber\":\"0037\",\"quantity\":4,\"sku\":\"6316608\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.97\",\"lineNumber\":\"0038\",\"quantity\":4,\"sku\":\"6316681\"},{\"retailPrice\":\"1.79\",\"costPrice\":\"0.56\",\"lineNumber\":\"0039\",\"quantity\":10,\"sku\":\"6161178\"},{\"retailPrice\":\"12.59\",\"costPrice\":\"7.93\",\"lineNumber\":\"0040\",\"quantity\":5,\"sku\":\"6735161\"},{\"retailPrice\":\"14.59\",\"costPrice\":\"7.80\",\"lineNumber\":\"0041\",\"quantity\":2,\"sku\":\"6245187\"},{\"retailPrice\":\"7.59\",\"costPrice\":\"3.05\",\"lineNumber\":\"0042\",\"quantity\":5,\"sku\":\"3193034\"},{\"retailPrice\":\"7.59\",\"costPrice\":\"2.67\",\"lineNumber\":\"0043\",\"quantity\":3,\"sku\":\"6530844\"},{\"retailPrice\":\"4.49\",\"costPrice\":\"2.44\",\"lineNumber\":\"0044\",\"quantity\":2,\"sku\":\"3451614\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"2.74\",\"lineNumber\":\"0045\",\"quantity\":2,\"sku\":\"6104384\"},{\"retailPrice\":\"15.59\",\"costPrice\":\"7.50\",\"lineNumber\":\"0046\",\"quantity\":4,\"sku\":\"8757288\"},{\"retailPrice\":\"22.59\",\"costPrice\":\"13.23\",\"lineNumber\":\"0047\",\"quantity\":1,\"sku\":\"4122115\"},{\"retailPrice\":\"9.99\",\"costPrice\":\"3.29\",\"lineNumber\":\"0048\",\"quantity\":3,\"sku\":\"7087752\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.62\",\"lineNumber\":\"0049\",\"quantity\":5,\"sku\":\"7086408\"},{\"retailPrice\":\"10.99\",\"costPrice\":\"6.78\",\"lineNumber\":\"0050\",\"quantity\":3,\"sku\":\"7089667\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.96\",\"lineNumber\":\"0051\",\"quantity\":5,\"sku\":\"6547335\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.94\",\"lineNumber\":\"0052\",\"quantity\":5,\"sku\":\"0042408\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"1.74\",\"lineNumber\":\"0053\",\"quantity\":2,\"sku\":\"8976391\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"1.33\",\"lineNumber\":\"0054\",\"quantity\":3,\"sku\":\"9485160\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.94\",\"lineNumber\":\"0055\",\"quantity\":5,\"sku\":\"1587575\"},{\"retailPrice\":\"6.89\",\"costPrice\":\"3.50\",\"lineNumber\":\"0056\",\"quantity\":2,\"sku\":\"6720486\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.97\",\"lineNumber\":\"0057\",\"quantity\":2,\"sku\":\"0917062\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.97\",\"lineNumber\":\"0058\",\"quantity\":2,\"sku\":\"8802290\"},{\"retailPrice\":\"11.99\",\"costPrice\":\"7.74\",\"lineNumber\":\"0059\",\"quantity\":2,\"sku\":\"8566127\"},{\"retailPrice\":\"7.50\",\"costPrice\":\"4.61\",\"lineNumber\":\"0060\",\"quantity\":0,\"sku\":\"1045939\"},{\"retailPrice\":\"0.01\",\"costPrice\":\"7.86\",\"lineNumber\":\"0061\",\"quantity\":2,\"sku\":\"7198971\"}],\"totalCost\":\"1211.25\",\"supplierAddress\":\"ORGILL, INC. PO BOX 1000 DEPT 7 MEMPHIS TN\",\"billToAddress\":\"JAMAICA HDW & PAINTS131-01 JAMAICA AVE RICHMOND HILL NY\",\"shippingCost\":\"122.85\",\"shipTo\":\"353987\",\"invoiceNumber\":\"4611862\",\"po\":\"JAMAICA\"},{\"billTo\":\"353987\",\"invoiceDate\":\"083012\",\"invoiceDetails\":[{\"retailPrice\":\"18.99\",\"costPrice\":\"9.87\",\"lineNumber\":\"0001\",\"quantity\":4,\"sku\":\"8977001\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.51\",\"lineNumber\":\"0002\",\"quantity\":4,\"sku\":\"2510659\"},{\"retailPrice\":\"10.99\",\"costPrice\":\"6.80\",\"lineNumber\":\"0003\",\"quantity\":4,\"sku\":\"7452527\"},{\"retailPrice\":\"6.99\",\"costPrice\":\"3.19\",\"lineNumber\":\"0004\",\"quantity\":6,\"sku\":\"8976995\"},{\"retailPrice\":\"13.99\",\"costPrice\":\"7.44\",\"lineNumber\":\"0005\",\"quantity\":4,\"sku\":\"8566127\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.54\",\"lineNumber\":\"0006\",\"quantity\":6,\"sku\":\"4942769\"},{\"retailPrice\":\"18.99\",\"costPrice\":\"8.89\",\"lineNumber\":\"0007\",\"quantity\":6,\"sku\":\"5886346\"},{\"retailPrice\":\"140.00\",\"costPrice\":\"80.52\",\"lineNumber\":\"0008\",\"quantity\":1,\"sku\":\"6603740\"},{\"retailPrice\":\"16.99\",\"costPrice\":\"10.24\",\"lineNumber\":\"0009\",\"quantity\":2,\"sku\":\"2199818\"},{\"retailPrice\":\"24.99\",\"costPrice\":\"10.10\",\"lineNumber\":\"0010\",\"quantity\":0,\"sku\":\"6840748\"},{\"retailPrice\":\"12.99\",\"costPrice\":\"7.34\",\"lineNumber\":\"0011\",\"quantity\":2,\"sku\":\"0637132\"},{\"retailPrice\":\"13.99\",\"costPrice\":\"8.35\",\"lineNumber\":\"0012\",\"quantity\":1,\"sku\":\"6260228\"},{\"retailPrice\":\"19.99\",\"costPrice\":\"10.10\",\"lineNumber\":\"0013\",\"quantity\":6,\"sku\":\"5375480\"},{\"retailPrice\":\"7.99\",\"costPrice\":\"4.52\",\"lineNumber\":\"0014\",\"quantity\":4,\"sku\":\"6261309\"},{\"retailPrice\":\"11.99\",\"costPrice\":\"6.87\",\"lineNumber\":\"0015\",\"quantity\":2,\"sku\":\"9377458\"},{\"retailPrice\":\"15.99\",\"costPrice\":\"6.27\",\"lineNumber\":\"0016\",\"quantity\":2,\"sku\":\"9906298\"},{\"retailPrice\":\"24.99\",\"costPrice\":\"12.00\",\"lineNumber\":\"0017\",\"quantity\":1,\"sku\":\"6964951\"},{\"retailPrice\":\"39.99\",\"costPrice\":\"21.94\",\"lineNumber\":\"0018\",\"quantity\":1,\"sku\":\"0834788\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"2.68\",\"lineNumber\":\"0019\",\"quantity\":2,\"sku\":\"6112957\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.13\",\"lineNumber\":\"0020\",\"quantity\":5,\"sku\":\"8921538\"},{\"retailPrice\":\"7.99\",\"costPrice\":\"3.85\",\"lineNumber\":\"0021\",\"quantity\":1,\"sku\":\"4470803\"},{\"retailPrice\":\"8.99\",\"costPrice\":\"3.37\",\"lineNumber\":\"0022\",\"quantity\":2,\"sku\":\"6144539\"},{\"retailPrice\":\"1.99\",\"costPrice\":\"0.65\",\"lineNumber\":\"0023\",\"quantity\":10,\"sku\":\"9674888\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.96\",\"lineNumber\":\"0024\",\"quantity\":5,\"sku\":\"5787346\"},{\"retailPrice\":\"1.79\",\"costPrice\":\"0.28\",\"lineNumber\":\"0025\",\"quantity\":20,\"sku\":\"2801959\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"0.58\",\"lineNumber\":\"0026\",\"quantity\":5,\"sku\":\"6964720\"},{\"retailPrice\":\"8.99\",\"costPrice\":\"4.97\",\"lineNumber\":\"0027\",\"quantity\":4,\"sku\":\"6739882\"},{\"retailPrice\":\"6.99\",\"costPrice\":\"3.61\",\"lineNumber\":\"0028\",\"quantity\":1,\"sku\":\"6002836\"},{\"retailPrice\":\"1.99\",\"costPrice\":\"0.58\",\"lineNumber\":\"0029\",\"quantity\":10,\"sku\":\"4614681\"},{\"retailPrice\":\"8.99\",\"costPrice\":\"5.41\",\"lineNumber\":\"0030\",\"quantity\":1,\"sku\":\"6177786\"},{\"retailPrice\":\"2.49\",\"costPrice\":\"1.18\",\"lineNumber\":\"0031\",\"quantity\":10,\"sku\":\"5454830\"},{\"retailPrice\":\"3.49\",\"costPrice\":\"1.51\",\"lineNumber\":\"0032\",\"quantity\":2,\"sku\":\"6170740\"},{\"retailPrice\":\"0.39\",\"costPrice\":\"23.31\",\"lineNumber\":\"0033\",\"quantity\":1,\"sku\":\"0543728\"},{\"retailPrice\":\"3.79\",\"costPrice\":\"1.67\",\"lineNumber\":\"0034\",\"quantity\":5,\"sku\":\"4850335\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"2.69\",\"lineNumber\":\"0035\",\"quantity\":4,\"sku\":\"6315949\"},{\"retailPrice\":\"14.69\",\"costPrice\":\"8.94\",\"lineNumber\":\"0036\",\"quantity\":1,\"sku\":\"1977222\"},{\"retailPrice\":\"1.29\",\"costPrice\":\"0.56\",\"lineNumber\":\"0037\",\"quantity\":10,\"sku\":\"3658929\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.63\",\"lineNumber\":\"0038\",\"quantity\":2,\"sku\":\"2163376\"},{\"retailPrice\":\"5.79\",\"costPrice\":\"1.78\",\"lineNumber\":\"0039\",\"quantity\":1,\"sku\":\"3669603\"},{\"retailPrice\":\"7.99\",\"costPrice\":\"3.04\",\"lineNumber\":\"0040\",\"quantity\":2,\"sku\":\"8425571\"},{\"retailPrice\":\"13.99\",\"costPrice\":\"7.68\",\"lineNumber\":\"0041\",\"quantity\":2,\"sku\":\"5408737\"},{\"retailPrice\":\"21.99\",\"costPrice\":\"12.58\",\"lineNumber\":\"0042\",\"quantity\":2,\"sku\":\"1992627\"},{\"retailPrice\":\"24.99\",\"costPrice\":\"16.22\",\"lineNumber\":\"0043\",\"quantity\":1,\"sku\":\"6123012\"},{\"retailPrice\":\"19.98\",\"costPrice\":\"9.18\",\"lineNumber\":\"0044\",\"quantity\":1,\"sku\":\"6645014\"},{\"retailPrice\":\"8.49\",\"costPrice\":\"5.03\",\"lineNumber\":\"0045\",\"quantity\":1,\"sku\":\"9069832\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"0.94\",\"lineNumber\":\"0046\",\"quantity\":10,\"sku\":\"0042408\"},{\"retailPrice\":\"4.99\",\"costPrice\":\"2.08\",\"lineNumber\":\"0047\",\"quantity\":6,\"sku\":\"6134837\"},{\"retailPrice\":\"2.99\",\"costPrice\":\"1.36\",\"lineNumber\":\"0048\",\"quantity\":4,\"sku\":\"6841092\"},{\"retailPrice\":\"5.99\",\"costPrice\":\"2.38\",\"lineNumber\":\"0049\",\"quantity\":3,\"sku\":\"6597892\"},{\"retailPrice\":\"4.29\",\"costPrice\":\"1.55\",\"lineNumber\":\"0050\",\"quantity\":2,\"sku\":\"5624010\"},{\"retailPrice\":\"3.99\",\"costPrice\":\"1.52\",\"lineNumber\":\"0051\",\"quantity\":0,\"sku\":\"0506337\"},{\"retailPrice\":\"13.99\",\"costPrice\":\"6.93\",\"lineNumber\":\"0052\",\"quantity\":0,\"sku\":\"5691381\"},{\"retailPrice\":\"10.99\",\"costPrice\":\"5.29\",\"lineNumber\":\"0053\",\"quantity\":1,\"sku\":\"6479315\"},{\"retailPrice\":\"10.59\",\"costPrice\":\"6.18\",\"lineNumber\":\"0054\",\"quantity\":1,\"sku\":\"8584054\"},{\"retailPrice\":\"9.99\",\"costPrice\":\"5.30\",\"lineNumber\":\"0055\",\"quantity\":1,\"sku\":\"4002192\"},{\"retailPrice\":\"12.99\",\"costPrice\":\"8.65\",\"lineNumber\":\"0056\",\"quantity\":1,\"sku\":\"6974489\"},{\"retailPrice\":\"10.99\",\"costPrice\":\"6.57\",\"lineNumber\":\"0057\",\"quantity\":0,\"sku\":\"8506560\"},{\"retailPrice\":\"99\",\"costPrice\":\"0.43\",\"lineNumber\":\"0058\",\"quantity\":24,\"sku\":\"6264337\"}],\"totalCost\":\"811.70\",\"supplierAddress\":\"ORGILL, INC. PO BOX 1000 DEPT 7 MEMPHIS TN\",\"billToAddress\":\"JAMAICA HDW & PAINTS131-01 JAMAICA AVE RICHMOND HILL NY\",\"shippingCost\":\"51.32\",\"shipTo\":\"353987\",\"invoiceNumber\":\"4613070\",\"po\":\"EMPIRE\"}]");
		purchaseInvoiceDAO.saveInvoices(request);
	}
	
	public static void main(String[] args) {
		/*String skus = "aaa,sss,ddd,ggg,fff";
		for (String sku : "ddd,sss".split(",")) {
			skus = skus.replaceAll(sku+"[\\,]*", "");
		}
		
		System.out.println(skus);*/
		JSONArray array = new JSONArray();
		array.add((JSONObject)JSONValue.parse("{abc:\"12345\",isCredit:true}"));
		array.add((JSONObject)JSONValue.parse("{sku:\"abcde\"}"));
//		System.out.println(array.indexOf((JSONObject)JSONValue.parse("{sku:\"1245\"}")));
		System.out.println(array.indexOf(JSONValue.parse("{skus:\"abcde\"}")));
	}
}
