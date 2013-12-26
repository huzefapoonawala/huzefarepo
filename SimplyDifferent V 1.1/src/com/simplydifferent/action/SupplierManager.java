package com.simplydifferent.action;


import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.Supplier;

public class SupplierManager extends ActionSupport {

	private static final long serialVersionUID = 138042524118747631L;
	
	private MasterDAO masterDAO;
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
	
	private Supplier supplier;	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	private String result;
	public String getResult() {
		return result;
	}
	
	private List<Supplier> suppliers;
	public List<Supplier> getSuppliers() {
		return suppliers;
	}
	
	public String fetchAllSuppliers() {
		suppliers = masterDAO.getSupplierList();
		return SUCCESS;
	}
	
	public String save() throws Exception {
		try {
			masterDAO.saveSupplier(supplier);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		try {
			masterDAO.editSupplier(supplier);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		try {
			masterDAO.deleteSupplier(supplier);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
}
