package com.dynamicsext.ig.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dynamicsext.ig.vo.Customer;
import com.dynamicsext.ig.vo.Item;

@Component
public class InvoiceGeneratorDAOImpl implements InvoiceGeneratorDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceGeneratorDAOImpl.class);
	
	@Autowired private JdbcTemplate dynamicsTemplate;
	
	public List<Customer> getAllCompanies() {
		return dynamicsTemplate.query("select Company, FirstName, LastName, State, City, Zip, Address, Address2, AccountNumber from Customer order by Company;", new BeanPropertyRowMapper<Customer>(Customer.class));
	}
	
	public Item getItemByLookupCode(String itemLookupCode) {
		Item item = null;
		try {
			item = dynamicsTemplate.queryForObject("select ID, ItemLookupCode, Description, Price from Item where ItemLookupCode = ? or ID in (select ItemID from Alias where Alias = ?);", new BeanPropertyRowMapper<Item>(Item.class), itemLookupCode, itemLookupCode);
		} catch (EmptyResultDataAccessException e) {
			
		}
		return item;
	}
	
	public void updateQuantityByItemLookupCode(List<Item> items) {
		String query = "update Item set Quantity = Quantity - ? where ItemLookupCode = ?";
		for (Item i : items) {
			try {
				dynamicsTemplate.update(query, i.getQuantity(), i.getItemLookupCode());
				LOGGER.info("Quantity of item with lookup code "+i.getItemLookupCode()+" updated by "+i.getQuantity()+".");
			} catch (Exception e) {
				LOGGER.error("Error occurred while updating quantity of item with lookup code "+i.getItemLookupCode()+".",e);
			}
		}
	}
	
	public List<Item> getItemsByKeyword(String keyword) {
		keyword = "%"+keyword+"%";
		String query = "select ID, ItemLookupCode, Description, Quantity, Price from Item where ItemLookupCode like ? or Description like ? or ID in (select ItemID from Alias where Alias like ?);";
		List<Item> items = dynamicsTemplate.query(query, new BeanPropertyRowMapper<Item>(Item.class), keyword, keyword, keyword);
		return items;
	}
}
