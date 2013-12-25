package test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import com.jh.dao.po.PurchaseOrderDAO;
import com.jh.util.DateUtil;
import com.jh.vo.PurchaseOrderDetails;
import com.jh.vo.RequestVO;

@ActiveProfiles("development")
public class POTest extends Setup {

	@Resource private PurchaseOrderDAO purchaseOrderDAO;
	
	@Test
	public void testPO() throws Exception {
//		testing  git commit
		RequestVO request = new RequestVO();
		request.setSupplierId(1);
		request.setFromDate1(DateUtil.convertString2Date("2002-01-01", "yyyy-MM-dd"));
		request.setToDate1(DateUtil.convertString2Date("2003-12-31", "yyyy-MM-dd"));
		List<PurchaseOrderDetails> list = purchaseOrderDAO.getDetailsForPurchaseOrder(request);
		for (PurchaseOrderDetails purchaseOrderDetails : list) {
			System.out.println(purchaseOrderDetails.toString());
		}
	}
}
