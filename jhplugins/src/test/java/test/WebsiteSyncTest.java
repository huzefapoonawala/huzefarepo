package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import com.jh.dao.website.WebsiteProductsSync;
import com.jh.util.DateUtil;
import com.jh.util.FTPWriter;
import com.jh.vo.FTPUser;

@ActiveProfiles(profiles={"website_sync"})
public class WebsiteSyncTest extends Setup{

	@Resource private WebsiteProductsSync websiteProductsSync;
	@Resource private FTPWriter ftpWriter;
	@Resource private FTPUser websiteImageFTPUser;
	
	@Test
	public void testDownloadAndSync() throws IOException {
		websiteProductsSync.downloadParseAndSyncAllFiles();
//		websiteProductsSync.addNewProducts2Website();
//		websiteProductsSync.downloadParseAndUpdateWebInventory();
		
		/*FTPClient ftpClient = ftpWriter.connect(websiteImageFTPUser);
		System.out.println(websiteImageFTPUser.getFtpFolderPath());
		ftpClient.changeWorkingDirectory(websiteImageFTPUser.getFtpFolderPath());
		System.out.println(ftpClient.printWorkingDirectory());
		ftpWriter.checkAndUploadFile2FTP(websiteImageFTPUser, ftpClient, new File("E:/Personal/shabbir ezzi website related/plugins related/webfiles/images/0011221.jpg"));
		ftpWriter.checkAndUploadFile2FTP(websiteImageFTPUser, ftpClient, new File("E:/Personal/shabbir ezzi website related/plugins related/webfiles/images/3387776.jpg"));
		ftpWriter.checkAndUploadFile2FTP(websiteImageFTPUser, ftpClient, new File("E:/Personal/shabbir ezzi website related/plugins related/webfiles/images/0039107.jpg"));
		for (String name : ftpClient.listNames()) {
			System.out.println(name);
		}
		ftpWriter.disconnect(ftpClient);*/
		
//		Assert.assertNotNull(websiteProductsSync.getLocalWarehouses());
		
//		System.out.println(Arrays.toString(websiteProductsSync.getLocalWarehouses().toArray()));
//		websiteProductsSync.downloadParseAndSyncProducts();
//		websiteProductsSync.downloadParseAndUpdateWebInventory();
	}
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("( [Aa][Nn][Dd] )");
		String cname = "Power Tools aNd Accessories";
		Matcher matcher = pattern.matcher(cname);
		System.out.println(matcher.replaceAll(" & "));
		matcher = pattern.matcher("Hand Tools");
		System.out.println(matcher.replaceAll(" &amp; "));
	}
}
