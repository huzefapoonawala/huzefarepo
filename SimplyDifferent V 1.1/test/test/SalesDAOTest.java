package test;

import java.io.FileOutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.dao.intr.PurchaseDAO;
import com.simplydifferent.dao.intr.SalesDAO;
import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.SalesInvoice;

public class SalesDAOTest extends Setup {

	@Resource private SalesDAO salesDAO; 
	@Resource private PurchaseDAO purchaseDAO;
	@Resource private MasterDAO masterDAO;
	 
//	@Test
	public void test() throws Exception {
		FileOutputStream out = new FileOutputStream("C:\\Users\\hpoonaw\\Desktop\\test.pdf");
		SalesInvoice invoice = new SalesInvoice();
		invoice.setInvoiceId(2);
		salesDAO.generateInvoicePdf(invoice, out);
	}
	
//	@Test
	public void testPurchase() {
		Product product = new Product();
		product.setId(1);
		System.out.println(masterDAO.getBatchDetailsByProduct(product).size());
	}
	
//	@Test
	public void testSales() {
		Customer customer = new Customer();
		customer.setId(2);
		Product product = new Product();
		product.setId(1);
		System.out.println(salesDAO.getSalesDetailsForProductByCustomer(customer, product).getSalesPrice());;
		
	}
	
	@Test
	public void testReceipt() {
		SalesInvoice invoice = new SalesInvoice();
		invoice.setCustomerId(3);
		invoice.setInvoiceId(1);
		List<SalesInvoice> list = salesDAO.getInvoicesForSalesReceipt(invoice);
		for (SalesInvoice sr : list) {
			System.out.println(sr.toString());
		}
	}
}
