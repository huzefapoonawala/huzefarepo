package com.jh.dao.orgilldb.item;

import java.util.List;

import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public interface ItemDAO {

	List<Item> getItemsBySkus(List<String> skus2Search);

	void copyItemsFromOrgillDB(RequestVO request);

	Item getItemDetailsBySku(RequestVO request);

}
