package test;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jh.util.CommonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jh-main-context.xml","classpath:jh-properties-context.xml"}) 
public class PropertiesTest {
	
	@Value("${jhplugins.path.edi.orgill}") private String orgillEdiPath;
	@Value("${jhplugins.path.properties}") private String path;
	
	@Resource private CommonUtil commonUtil;

	@Test
	public void testExternal() throws Exception {
		System.out.println(path);
		commonUtil.createFile(orgillEdiPath, "test.txt", "This is test\nedi format test".getBytes());
		System.out.println(orgillEdiPath);
	}
}
