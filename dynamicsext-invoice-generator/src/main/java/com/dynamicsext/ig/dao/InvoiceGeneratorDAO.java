package com.dynamicsext.ig.dao;

import java.util.List;

import com.dynamicsext.ig.vo.Customer;
import com.dynamicsext.ig.vo.Item;

public interface InvoiceGeneratorDAO {

	List<Customer> getAllCompanies();

	Item getItemByLookupCode(String itemLookupCode);

	void updateQuantityByItemLookupCode(List<Item> items);

	List<Item> getItemsByKeyword(String keyword);

}
