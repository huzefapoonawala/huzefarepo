package com.jh.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.jh.dao.supplier.SupplierDAO;
import com.opensymphony.xwork2.ActionSupport;

public class Supplier extends ActionSupport {

	private static final long serialVersionUID = 4169585998865372591L;
	
	private static Logger logger = Logger.getLogger(Supplier.class);

	private List<com.jh.vo.Supplier> suppliers;
	public List<com.jh.vo.Supplier> getSuppliers() {
		return suppliers;
	}
	
	private SupplierDAO supplierDAO;
	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}
	
	public String fetchAllSuppliers() {
		try {
			suppliers = supplierDAO.getAllSuppliers();
		} catch (Exception e) {
			logger.error("Error in Supplier -> fetchAllSuppliers", e);
		}
		return SUCCESS;
	}
}
