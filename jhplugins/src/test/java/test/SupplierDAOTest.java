package test;

import javax.annotation.Resource;

import org.junit.Test;

import com.jh.dao.supplier.SupplierDAO;
import com.jh.vo.Supplier;

public class SupplierDAOTest extends Setup{

	@Resource private SupplierDAO supplierDAO;
	
	@Test
	public void testSupplierList() {
		for (Supplier supplier : supplierDAO.getAllSuppliers()) {
			System.out.println(supplier.getId()+" "+supplier.getSupplierName());
		}
	}
}
