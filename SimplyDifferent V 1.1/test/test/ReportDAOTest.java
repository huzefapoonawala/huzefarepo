package test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.simplydifferent.dao.intr.ReportDAO;
import com.simplydifferent.vo.ReportRequest;

public class ReportDAOTest extends Setup{

	@Resource private ReportDAO reportDAO;
	
//	@Test
	public void testPurchaseReport() throws Exception {
		ReportRequest request = new ReportRequest();
		System.out.println(reportDAO.getPurchaseReport(request).getInvoices().size());;
		request = new ReportRequest();
		request.setSupplierId(1);
		request.setFromDate(new Date());
		System.out.println(reportDAO.getPurchaseReport(request).getInvoices().size());;
		
	}
	
	@Test
	public void testProductStockReport() throws Exception {
		ReportRequest request = new ReportRequest();
//		request.setBrandId(2);
		request.setProductName("milk");
		System.out.println(reportDAO.getProductStockReport(request).getProducts().size());;
	}
}
