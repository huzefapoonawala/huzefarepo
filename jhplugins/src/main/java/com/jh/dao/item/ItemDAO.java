package com.jh.dao.item;

import java.util.List;

import com.jh.vo.Item;
import com.jh.vo.ReasonCode;
import com.jh.vo.RequestVO;

public interface ItemDAO {

	List<Item> getItemDetails(RequestVO request);

	Boolean checkAliasUniqueness(RequestVO request);

	void addAlias(RequestVO request);

	void deleteAlias(RequestVO request);

	List<ReasonCode> getReasonCodes();

	void updateItems(RequestVO request);

	List<Item> getItemDetails(String requestXml);
	
	void deactivateItems(RequestVO request);

	void addDepartments(RequestVO request);

	void addCategories(RequestVO request);

	void addOrActivateItems(RequestVO request, String imagePath);

}
