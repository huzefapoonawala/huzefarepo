package com.simplydifferent.dao.intr;

import java.util.Date;
import java.util.List;

import com.simplydifferent.entity.CategoryMaster;
import com.simplydifferent.entity.VariantMaster;
import com.simplydifferent.vo.Brand;
import com.simplydifferent.vo.CategoryVO;
import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.ProductBatchStock;
import com.simplydifferent.vo.ProductVO;
import com.simplydifferent.vo.Supplier;
import com.simplydifferent.vo.VariantVO;

public interface MasterDAO {

	public List<CategoryVO> getCategoryList();
	
	public void saveCategory(CategoryMaster categoryMaster) throws Exception;
	
	public List<ProductVO> getProductList(String categoryName);
	
	public void saveProduct(Product product) throws Exception;
	
	public List<VariantVO> getVariantList(Integer productId);
	
	public void saveVariant(VariantMaster variantMaster) throws Exception;
	
	List<Supplier> getSupplierList();

	List<Brand> getBrandList();

	List<Product> getProductList(Brand brand);

	List<Product> getAllProducts();

	void deleteProduct(Product product) throws Exception;

	boolean isProductNameValid(Product product);

	void editProduct(Product product) throws Exception;

	void updateProductStock(Integer productId, Double deltaStock, boolean isAdd);

	void deleteBrand(Brand brand) throws Exception;

	void editBrand(Brand brand) throws Exception;

	void saveBrand(Brand brand);

	void saveSupplier(Supplier supplier) throws Exception;

	void editSupplier(Supplier supplier) throws Exception;

	void deleteSupplier(Supplier supplier) throws Exception;

	void saveCustomer(Customer customer) throws Exception;

	void editCustomer(Customer customer) throws Exception;

	void deleteCustomer(Customer customer) throws Exception;

	List<Customer> getAllCustomers();

	void updateProductBatchStock(Integer productId, String batchNumber, Date expiryDate, Double deltaStock, boolean isAdd);

	List<ProductBatchStock> getBatchDetailsByProduct(Product product);
}
