package com.jh.etl;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jh.etl.common.ftputil.FTPReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FTPReaderImplTest {

	@Autowired private FTPReader orgillProductDataFTPReader;
	@Value("${ftpuser.orgillproductdata.localfolderpath}") private String tempFolderPath;
	
	@Test
	public void testValidDownloadFile() throws Exception {
		String filename = "WEB_DEPT.TXT";
		Path expectedFilePath = Paths.get(tempFolderPath).resolve(filename);
		assertEquals(expectedFilePath.toString(), orgillProductDataFTPReader.downloadFile(filename));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testInvalidDownloadFile() throws Exception {
		orgillProductDataFTPReader.downloadFile("abc.123");
	}
}
