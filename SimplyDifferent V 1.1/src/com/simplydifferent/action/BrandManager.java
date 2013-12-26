package com.simplydifferent.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.Brand;

public class BrandManager extends ActionSupport {

	private static final long serialVersionUID = 6136605631501393124L;

	private List<Brand> list;
	public List<Brand> getList() {
		return list;
	}
	
	private Brand brand;	
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	private MasterDAO masterDAO;
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
	
	public String fetchBrandList() {
		list = masterDAO.getBrandList();
		return SUCCESS;
	}
	
	public String save() throws Exception {
		masterDAO.saveBrand(brand);
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		masterDAO.editBrand(brand);
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		masterDAO.deleteBrand(brand);
		return SUCCESS;
	}
}
