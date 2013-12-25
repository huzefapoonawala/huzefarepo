package com.jh.dao.supplier;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jh.vo.Supplier;

public class SupplierDAOImpl implements SupplierDAO {

	private JdbcTemplate dynamicsTemplate;
	public void setDynamicsTemplate(JdbcTemplate dynamicsTemplate) {
		this.dynamicsTemplate = dynamicsTemplate;
	}
	
	@Override
	public List<Supplier> getAllSuppliers() {
		return dynamicsTemplate.query("select id, supplierName from dbo.Supplier order by supplierName", new BeanPropertyRowMapper<Supplier>(Supplier.class));
	}
}
