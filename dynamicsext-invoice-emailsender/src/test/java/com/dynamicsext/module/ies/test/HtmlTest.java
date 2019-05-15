package com.dynamicsext.module.ies.test;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes={HtmlTest.class})
@ComponentScan(
		basePackages={"com.dynamicsext"}, 
		excludeFilters = {
				@Filter(type = FilterType.REGEX, pattern="com.dynamicsext.module.ies.service.*"),
				@Filter(type = FilterType.REGEX, pattern="com.dynamicsext.module.ies.bootstrap.*")
		}
)
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class HtmlTest {

	@Autowired private VelocityEngine engine;
	
	@Test
	public void testInvoiceTest() throws Exception {
		String text = VelocityEngineUtils.mergeTemplateIntoString(this.engine,"invoice-template.html", "UTF-8", PDFUtilTest.generateModel());
		FileOutputStream out = new FileOutputStream(new File("./samples/sample-invoice.html"));
		out.write(text.getBytes());
		out.close();
	}
}
