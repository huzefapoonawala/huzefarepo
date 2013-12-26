package com.simplydifferent.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.simplydifferent.dao.intr.MasterDAO;
import com.simplydifferent.vo.Customer;

public class CustomerManager extends ActionSupport {

	private static final long serialVersionUID = 4951393029253404509L;

	private Customer customer;	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	private List<Customer> customers;	
	public List<Customer> getCustomers() {
		return customers;
	}

	private MasterDAO masterDAO;
	public void setMasterDAO(MasterDAO masterDAO) {
		this.masterDAO = masterDAO;
	}
	
	public String save() throws Exception {
		try {
			masterDAO.saveCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		try {
			masterDAO.editCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		try {
			masterDAO.deleteCustomer(customer);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	
	public String fetchAllCustomers() {
		customers = masterDAO.getAllCustomers();
		return SUCCESS;
	}
}
