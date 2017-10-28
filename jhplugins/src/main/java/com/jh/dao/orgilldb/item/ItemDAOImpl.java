package com.jh.dao.orgilldb.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public class ItemDAOImpl implements ItemDAO {

	private Logger LOG = Logger.getLogger(ItemDAOImpl.class);
	
	private JdbcTemplate dynamicsTemplate;
	public void setDynamicsTemplate(JdbcTemplate dynamicsTemplate) {
		this.dynamicsTemplate = dynamicsTemplate;
	}

	@Value("${query.orgilldb.get.items.byskus}") private String queryGetItemsBySkus;
	@Value("${query.dynamicsdb.copy.item.bysku}") private String queryCopyItemBySku;
	
	@Override
	public List<Item> getItemsBySkus(List<String> skus2Search) {
		List<Item> items = new ArrayList<Item>();
		if (null != skus2Search) {
			StringBuilder sb = new StringBuilder();
			for (String sku : skus2Search) {
				sb.append("'").append(sku).append("',");
			}
			sb.deleteCharAt(sb.length()-1);
			LOG.trace(String.format("Checking items in orgilldb for skus %s", sb.toString()));
			String query = queryGetItemsBySkus.replaceFirst("#SKUS#", sb.toString());
			items.addAll(dynamicsTemplate.query(query, new BeanPropertyRowMapper<Item>(Item.class)));
		}
		return items;
	}
	
	@Override
	public void copyItemsFromOrgillDB(RequestVO request) {
		List<String> skus = request.getSkus();
		for (String sku : skus) {
			dynamicsTemplate.queryForInt(queryCopyItemBySku, sku);
			LOG.debug(String.format("Successfully copied item with SKU '%s' from orgill db to dynamics db.", sku));
		}
	}
}
