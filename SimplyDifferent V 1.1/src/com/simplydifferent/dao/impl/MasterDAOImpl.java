package com.simplydifferent.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.datasouce.intr.Datasource;
import com.simplydifferent.entity.CategoryMaster;
import com.simplydifferent.entity.ProductMaster;
import com.simplydifferent.entity.VariantMaster;
import com.simplydifferent.util.BeanUtil;
import com.simplydifferent.vo.Brand;
import com.simplydifferent.vo.CategoryVO;
import com.simplydifferent.vo.Customer;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.ProductBatchStock;
import com.simplydifferent.vo.ProductStock;
import com.simplydifferent.vo.ProductVO;
import com.simplydifferent.vo.Supplier;
import com.simplydifferent.vo.VariantVO;

public class MasterDAOImpl implements MasterDAO {
	
	private Datasource datasource;

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}
	 
	@SuppressWarnings("unchecked")
	public List<CategoryVO> getCategoryList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMaster.class)
										.addOrder(Order.asc("categoryName"));
		List<CategoryMaster> list = datasource.getTemplate().findByCriteria(criteria);
		List<CategoryVO> list2 = new ArrayList<CategoryVO>();
		for (CategoryMaster categoryMaster : list) {
			list2.add(new CategoryVO(categoryMaster));
		}
		return list2;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCategory(CategoryMaster categoryMaster) throws Exception {
		if (datasource.getTemplate().get(CategoryMaster.class, categoryMaster.getCategoryName()) != null) {
			throw new Exception("Category already exists.");
		}
		categoryMaster.setCreationDate(new Date());
		categoryMaster.setModifiedDate(new Date());
		datasource.save(categoryMaster);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductVO> getProductList(String categoryName) {
		String queryString = "from ProductMaster "+(categoryName != null ? "where categoryName = ? " : "")+" order by categoryName,productName";
		List<ProductMaster> list = categoryName != null ? datasource.getTemplate().find(queryString, categoryName) : datasource.getTemplate().find(queryString);
		List<ProductVO> list2 = new ArrayList<ProductVO>();
		for (ProductMaster productMaster : list) {
			list2.add(new ProductVO(productMaster));
		}
		return list2;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductList(Brand brand) {
		String queryString = "from Product p where p.brandId = ? order by p.productName";
		return datasource.getTemplate().find(queryString, brand.getBrandId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProducts() {
		return populateProductList(datasource.getTemplate().find("from Product p, Brand b where p.brandId = b.brandId order by b.brandName, p.productName"));
	}
	
	private List<Product> populateProductList(List<Object> pList) {
		List<Product> list = new ArrayList<Product>();
		Product tmpProduct = null;
		for (Object object : pList) {
			Object[] objects = (Object[])object;
			tmpProduct = (Product)objects[0];
			tmpProduct.setBrand((Brand)objects[1]);
			list.add(tmpProduct);
		}
		return list;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveProduct(Product product) throws Exception {
		product.setCreationDate(new Date());
		datasource.save(product);
	}
	
	@SuppressWarnings("unchecked")
	public List<VariantVO> getVariantList(Integer productId) {
		String queryString = "select v,p from VariantMaster v, ProductMaster p where v.productId = p.id "+(productId != null ? " and v.productId = ? " : "")+" order by p.categoryName,p.productName,v.variantName";
		List<Object[]> list = productId != null ? datasource.getTemplate().find(queryString, productId) : datasource.getTemplate().find(queryString);
		List<VariantVO> list2 = new ArrayList<VariantVO>();
		for (Object[] objects : list) {
			VariantVO variantVO = new VariantVO((VariantMaster)objects[0]);
			variantVO.setProduct(new ProductVO((ProductMaster)objects[1]));
			list2.add(variantVO);
		}
		return list2;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveVariant(VariantMaster variantMaster) throws Exception {
		VariantMaster orgVariantMaster = null;
		for (Object object : datasource.getTemplate().find("from VariantMaster where productId = ? and variantName = ?", new Object[]{variantMaster.getProductId(), variantMaster.getVariantName()})) {
			orgVariantMaster = (VariantMaster)object;
		}
		if (orgVariantMaster == null) {
			variantMaster.setCreationDate(new Date());
			variantMaster.setModifiedDate(new Date());
			orgVariantMaster = variantMaster;
		}
		else {
			orgVariantMaster.setPurchaseDiscount(variantMaster.getPurchaseDiscount());
			orgVariantMaster.setVat(variantMaster.getVat());
		}
		datasource.saveOrUpdate(orgVariantMaster);
	}
	
	@SuppressWarnings("unchecked")
	public List<Supplier> getSupplierList() {
		String queryString = "from Supplier order by supplierName";
		return datasource.getTemplate().find(queryString);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveSupplier(Supplier supplier) throws Exception {
		supplier.setCreationDate(new Date());
		datasource.saveOrUpdate(supplier);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Brand> getBrandList() {
		return datasource.getTemplate().find("from Brand");
	}
	
	@Override
	public void deleteProduct(Product product) throws Exception {
		try {
			datasource.delete(datasource.getTemplate().get(Product.class, product.getId()));
		} catch (Exception e) {
			throw new Exception("Due to some problem could not process request");
		}
	}
	
	@Override
	public boolean isProductNameValid(Product product) {
		return datasource.getTemplate().find("from Product where brandId = ? and productName = ?", new Object[]{product.getBrandId(),product.getProductName()}).isEmpty();
	}
	
	@Override
	public void editProduct(Product product) throws Exception {
		try {
			Product productMaster = datasource.getTemplate().get(Product.class, product.getId());
			for (String key : new String[]{"productName","purchasePrice","vat","unitType","reorderLimit"}) {
				PropertyUtils.setProperty(productMaster, key, PropertyUtils.getProperty(product, key));
			}
			datasource.saveOrUpdate(productMaster);
		} catch (Exception e) {
			throw new Exception("Due to some problem could not process request");
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateProductStock(Integer productId, Double deltaStock, boolean isAdd) {
		ProductStock productStock = datasource.getTemplate().get(ProductStock.class, productId);
		double stock = productStock.getStock() == null ? 0 : productStock.getStock();
		stock = stock+(isAdd ? deltaStock : -deltaStock);
		productStock.setStock(stock);
		datasource.update(productStock);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveBrand(Brand brand) {
		brand.setCreationDate(new Date());
		datasource.save(brand);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void editBrand(Brand brand) throws Exception{
		Brand brandMaster = datasource.getTemplate().get(Brand.class, brand.getBrandId());
		BeanUtil.copyPropertyValue(brand, brandMaster, "brandName");
		BeanUtil.copyPropertyValue(brand, brandMaster, "description");
		datasource.update(brandMaster);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteBrand(Brand brand) throws Exception{
		Brand brandMaster = datasource.getTemplate().get(Brand.class, brand.getBrandId());
		datasource.delete(brandMaster);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void editSupplier(Supplier supplier) throws Exception{
		Supplier supplierMaster = datasource.getTemplate().get(Supplier.class, supplier.getId());
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "supplierName");
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "supplierAddress");
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "supplierContact");
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "supplierEmail");
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "supplierWebsite");
		BeanUtil.copyPropertyValue(supplier, supplierMaster, "vatNumber");
		datasource.update(supplierMaster);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteSupplier(Supplier supplier) throws Exception{
		Supplier supplierMaster = datasource.getTemplate().get(Supplier.class, supplier.getId());
		datasource.delete(supplierMaster);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCustomer(Customer customer) throws Exception {
		customer.setCreationDate(new Date());
		datasource.save(customer);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void editCustomer(Customer customer) throws Exception{
		Customer customerMaster = datasource.getTemplate().get(Customer.class, customer.getId());
		for (String key : new String[]{"name","contactPerson","address","contactNumber","vatNumber","email"}) {
			BeanUtil.copyPropertyValue(customer, customerMaster, key);
		}
		datasource.update(customerMaster);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteCustomer(Customer customer) throws Exception{
		Customer customerMaster = datasource.getTemplate().get(Customer.class, customer.getId());
		datasource.delete(customerMaster);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Customer> getAllCustomers() {
		String queryString = "from Customer order by name";
		return datasource.getTemplate().find(queryString);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateProductBatchStock(Integer productId, String batchNumber, Date expiryDate, Double deltaStock, boolean isAdd) {
		ProductBatchStock batchStock = null;
		for (ProductBatchStock bs : (List<ProductBatchStock>)datasource.getTemplate().find("from ProductBatchStock where productId = ? and batchNumber = ? and expiryDate = ?", productId,batchNumber,expiryDate)) {
			batchStock = bs;
		}
		if (batchStock == null) {
			batchStock = new ProductBatchStock();
			batchStock.setBatchNumber(batchNumber);
			batchStock.setProductId(productId);
			batchStock.setExpiryDate(expiryDate);
			batchStock.setCreationDate(new Date());
		}
		double stock = batchStock.getQuantity() == null ? 0 : batchStock.getQuantity();
		stock = stock+(isAdd ? deltaStock : -deltaStock);
		batchStock.setQuantity(stock);
		datasource.saveOrUpdate(batchStock);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductBatchStock> getBatchDetailsByProduct(Product product) {
		return datasource.getTemplate().find("from ProductBatchStock where productId = ? and quantity > 0 order by batchNumber", product.getId());
	}
}
