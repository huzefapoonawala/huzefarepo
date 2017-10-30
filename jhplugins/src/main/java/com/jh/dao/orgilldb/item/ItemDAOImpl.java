package com.jh.dao.orgilldb.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jh.util.CommonUtil;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public class ItemDAOImpl implements ItemDAO {

	private Logger LOG = Logger.getLogger(ItemDAOImpl.class);
	
	private JdbcTemplate dynamicsTemplate;
	public void setDynamicsTemplate(JdbcTemplate dynamicsTemplate) {
		this.dynamicsTemplate = dynamicsTemplate;
	}
	
	private CommonUtil commonUtil;
	public void setCommonUtil(CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	@Value("${query.orgilldb.get.items.byskus}") private String queryGetItemsBySkus;
	@Value("${query.dynamicsdb.copy.item.bysku}") private String queryCopyItemBySku;
	@Value("${query.orgilldb.get.itemdetails.bysku}") private String queryGetItemDetailsBySku;
	
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
	
	@Override
	public Item getItemDetailsBySku(RequestVO request) {
		Item item = dynamicsTemplate.queryForObject(queryGetItemDetailsBySku, new BeanPropertyRowMapper<Item>(Item.class), request.getSku());
		item.setImage(commonUtil.getImageUrl(item.getSku(), "jpg"));
		return item;
	}
}
