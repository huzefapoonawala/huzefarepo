package com.simplydifferent.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.Brand;
import com.simplydifferent.vo.Product;
import com.simplydifferent.vo.ProductBatchStock;

public class ProductManager extends ActionSupport {

	private static final long serialVersionUID = 6310667225686175545L;
	
	private Product product;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	private String result;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	private List<Product> products;	
	public List<Product> getProducts() {
		return products;
	}
	
	private Brand brand;
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	private Boolean success = true;
	public Boolean getSuccess() {
		return success;
	}
	
	private List<ProductBatchStock> batchStockList;
	public List<ProductBatchStock> getBatchStockList() {
		return batchStockList;
	}

	private MasterDAO masterDAO;
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
	
	public String fetchProductsByBrand() {
		products = masterDAO.getProductList(brand);
		return SUCCESS;
	}
	
	public String fetchAllProducts() {
		products = masterDAO.getAllProducts();
		return SUCCESS;
	}
	
	public String save() {
		try {
			masterDAO.saveProduct(product);
			result = "Product saved successfully";
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			success = false;
		}
		return SUCCESS;
	}

	public String delete() {
		try {
			masterDAO.deleteProduct(product);
			result = "Product deleted successfully";
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			success = false;
		}
		return SUCCESS;
	}
	
	public String checkProductName() {
		success = masterDAO.isProductNameValid(product);
		return SUCCESS;
	}
	
	public String edit() {
		try {
			masterDAO.editProduct(product);
			result = "Changes saved successfully";
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			success = false;
		}
		return SUCCESS;
	}
	
	public String fetchBatchDetailsByProduct() throws Exception {
		batchStockList = masterDAO.getBatchDetailsByProduct(product);
		return SUCCESS;
	}
}
