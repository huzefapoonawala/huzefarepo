package test;

import javax.annotation.Resource;

import org.junit.Test;

import com.jh.util.CommonUtil;

public class CommonUtilTest extends Setup {

	@Resource private CommonUtil commonUtil;
	
	@Test
	public void testImageUrl() {
		System.out.println(commonUtil.getImageUrl("1234567890.jpg"));
	}
}
