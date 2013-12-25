package com.jh.dao.website;

import java.io.IOException;
import java.util.List;

import com.jh.vo.Item;
import com.jh.vo.RequestVO;
import com.jh.vo.WebsiteOrder;

public interface WebsiteProductsDAO {

	List<Item> getDeletedProducts() throws IOException;

	int deleteProducts(RequestVO request) throws Exception;

	void downloadParseAndPersistWebFiles() throws IOException;

	void parseAndPersistWebFiles(String webDeptSkuFileName, String webSkuCommonFileName) throws IOException;

	List<Item> getItemsToDeleteAfterSync();

	List<Item> getItemsToAddAfterSync();

	void parseAndPersistWebCategories(String webDeptFileName) throws IOException;

	void downloadParseAndPersistWebCategories() throws IOException;

	List<Item> getDeptToAddAfterSync();

	List<Item> getCategoryToAddAfterSync();

	List<Item> getWebsiteItemsToDisableAfterSync();

	int deactivateWebsiteItems(RequestVO request);

	List<Item> getWebsiteItemsToActivateAfterSync();

	void activateWebsiteItems(RequestVO request);

	List<WebsiteOrder> getWebsiteOrder();

	void processOrders(String ordersToProcessFrom, String ordersToProcess) throws Exception;

	void downloadParseAndUpdateWebInventory() throws IOException;

	void parseAndUpdateWebInventory(String filename) throws IOException;

	String checkAndUpdateOrderAcknowledgement(String ordersToProcessFrom) throws Exception;

	void updatePoNumber(RequestVO request);

	String checkAndUpdateOrderShipping(String ordersToProcessFrom) throws Exception;

}
