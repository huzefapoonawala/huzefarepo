package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.jh.dao.pi.PurchaseInvoiceDAO;
import com.jh.util.FTPReader;
import com.jh.vo.FTPUser;
import com.jh.vo.RequestVO;

@ContextConfiguration(locations={"classpath:jh-test-context.xml","classpath:jh-ftp-context.xml"})
public class FtpTest extends Setup {

	@Resource @Qualifier("purchaseInvoiceFTPReader") private FTPReader ftpReader;
	@Resource @Qualifier("webfilesFTPReader") private FTPReader webfilesFtpReader;
//	@Resource @Qualifier("websiteTemplate") private JdbcTemplate jdbcTemplate;
	@Resource @Qualifier("godaddyFTPReader") private FTPReader godaddyFTPReader;
	
	@Resource private PurchaseInvoiceDAO purchaseInvoiceDAO;
	
	@Resource @Qualifier("orgillEdiAckFtpUser")  private FTPUser ediAckFtpUser;
	
//	@Test
	public void test() throws IOException {
		List<Map<String, String>> files = ftpReader.getAllPIFiles();
		System.out.println(Arrays.toString(files.toArray()));
	}
	
//	@Test
	public void testReadFile() throws Exception {
		RequestVO request = new RequestVO();
		request.setFtpFileName("353987.oin");
		request.setFtpUserIdx(0);
		/*request.setFtpFileName("081877.oin");
		request.setFtpUserIdx(1);*/
		System.out.println(purchaseInvoiceDAO.getFtpFileDetails(request));
	}
	
//	@Test
	public void testWebFtp() throws IOException {
		String tempdir = System.getProperty("java.io.tmpdir");
//		webfilesFtpReader.connect(0);
		/*for (FTPFile file : webfilesFtpReader.getFtpClient().listFiles("orgillftp/webfiles/")) {
			System.out.println(file.getName()+" "+webfilesFtpReader.getFtpClient().retrieveFileStream("orgillftp/webfiles/"+file.getName()).available());
		}*/
//		webfilesFtpReader.getFtpClient().setConnectTimeout(600000);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(webfilesFtpReader.getFtpClient().retrieveFileStream("orgillftp/webfiles/skudescadd.txt")));
//		FileOutputStream out = new FileOutputStream(tempdir+"/WEB_DEPT_SKU.TXT");
//		webfilesFtpReader.getFtpClient().retrieveFile("orgillftp/webfiles/WEB_DEPT_SKU.TXT", out);
//		out.close();
		/*byte[] bs = null;
		InputStream in = webfilesFtpReader.getFtpClient().retrieveFileStream("orgillftp/webfiles/skudescadd.txt");
		while (in.available() > -1) {
			if (in.available() == 0) {
				continue;
			}
			bs = new byte[in.available() > 8192 ? 8192 : in.available()];
			in.read(bs);
			System.out.print(new String(bs));
		}
		System.out.println(in.available());
		bs = new byte[in.available()];
		in.read(bs);
		in.close();
		System.out.println(new String(bs));*/
//		webfilesFtpReader.downloadWebFile("WEB_DEPT.TXT");
//		webfilesFtpReader.disconnect();
		webfilesFtpReader.downloadWebFile("WEB_DEPT_SKU.TXT");
//		webfilesFtpReader.downloadWebFile("WEB_SKU_COMMON.TXT");
		String filename = null;
//		filename = webfilesFtpReader.downloadWebFile("WEB_DEPT.TXT");
		/*filename = "C://Users//hpoonaw//AppData//Local//Temp//WEB_DEPT.TXT";
		System.out.println(filename);
		CSVReader reader = new CSVReader(new FileReader(filename),'~');
		String[] csvLine = null;
		while ((csvLine = reader.readNext()) != null) {
			if (csvLine.length >= 13 && csvLine[3].trim().equalsIgnoreCase("000")) {
				System.out.println(Arrays.toString(csvLine));
				System.out.println(csvLine[0].trim()+" "+csvLine[1].trim()+" "+csvLine[2].trim()+" "+csvLine[12].trim());
			}
		}*/
		/*NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		CSVReader reader = new CSVReader(new FileReader(tempdir+"/WEB_DEPT_SKU.TXT"),'~');
		String[] csvLine = null;
		List<String> skus = new ArrayList<String>();
		List<String> discontinuedSkus = null;
		String query = "SELECT sku FROM product WHERE sku NOT IN (:SKUS)";
		while ((csvLine = reader.readNext()) != null) {
//			System.out.println(csvLine[6]);
			skus.add(csvLine[6]);
			if (skus.size() >= 100) {
				System.out.println(Arrays.toString(skus.toArray()));
				
				skus.clear();
			}
		}
		reader.close();
		if (!skus.isEmpty()) {
			discontinuedSkus = template.queryForList(query,Collections.singletonMap("SKUS", skus),String.class);
			System.out.println(discontinuedSkus.size());
			System.out.println(Arrays.toString(discontinuedSkus.toArray()));
		}*/
	}
	
//	@Test
	public void testGodaddyFTP() throws IOException {
		Assert.assertNotNull(godaddyFTPReader);
		godaddyFTPReader.connect(0);
		File imgDir = new File("E:/Personal/shabbir ezzi website related/data as on 31-May-2012 from ftp folder/web images/images");
		List<File> imgFiles = new ArrayList<File>(Arrays.asList(imgDir.listFiles()));
		System.out.println(imgFiles.size());
		for (File img : imgFiles) {
			System.out.println(img.getName());
		}
		FTPClient client = godaddyFTPReader.getFtpClient();
		String imgFN = "8820979.jpg";
		FileInputStream fin = new FileInputStream("E:/Personal/shabbir ezzi website related/data as on 31-May-2012 from ftp folder/web images/images/"+imgFN);
		client.changeWorkingDirectory("/image/data/");
		boolean isDone = client.appendFile(imgFN, fin);
		System.out.println(isDone);
		/*String [] fns = client.listNames();
		System.out.println(Arrays.toString(fns));*/
		/*FTPFile[] ftpFiles = godaddyFTPReader.getFtpClient().mlistDir("/image/data");
		System.out.println(ftpFiles.length);
		
		for (FTPFile f : ftpFiles) {
			System.out.println(f.getName());
		}*/
		godaddyFTPReader.disconnect();
	}
	
	@Test
	public void testLastUpdateFTPFile() throws IOException {
		webfilesFtpReader.connect(0);
		
		FTPClient client = webfilesFtpReader.getFtpClient();
		
//		FTPFile[] files = client.listFiles("orgillftp/webfiles/WEB_DEPT_SKU.TXT");
//		System.out.println(files[0].getTimestamp().getTime());
		
		webfilesFtpReader.downloadWebFile("WEB_DEPT_SKU.TXT");
		
		FTPUser ftpUser = new FTPUser();
		ftpUser.setFtpFolderPath("orgillftp/webfiles/");
		ftpUser.setLocalFolderPath("E:/Personal/shabbir ezzi website related/plugins related/webfiles/");
		ftpUser.setFileNames(new ArrayList<String>(Arrays.asList(new String[]{"WEB_DEPT_SKU.TXT"})));
		webfilesFtpReader.downloadFTPFiles(client, ftpUser);
		
		webfilesFtpReader.disconnect();
	}
	
	@Test
	public void testAckFileDownload() throws IOException {
		FTPClient ftpClient = webfilesFtpReader.connect(ediAckFtpUser.getUsername(), ediAckFtpUser.getPassword(), ediAckFtpUser.getHostname());
//		FTPClient ftpClient = webfilesFtpReader.connectWithProxy(ediAckFtpUser, "10.40.53.21", 3128);
		List<String> fileNames = ftpReader.downloadFTPFiles(ftpClient, ediAckFtpUser);
		System.out.println(Arrays.toString(fileNames.toArray()));
		webfilesFtpReader.disconnect(ftpClient);
	}
}
