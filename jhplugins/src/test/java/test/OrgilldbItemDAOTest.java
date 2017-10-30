package test;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.jh.dao.orgilldb.item.ItemDAO;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public class OrgilldbItemDAOTest extends Setup {

	@Resource private ItemDAO orgilldbItemDAO;
	
	@Test
	public void testGetItemsBySkus() {
		List<String> skus = Arrays.asList(new String[]{"6605414","3265089","ABC1234"});
		List<Item> items = orgilldbItemDAO.getItemsBySkus(skus);
		for (Item item : items) {
			Assert.assertNotEquals(-1, skus.indexOf(item.getSku()));
		}
	}
	
	@Test
	public void testCopyItemBySku() {
		List<String> skus = Arrays.asList(new String[]{/*"0010124",*/"0010447","0010611"});
		RequestVO request = new RequestVO();
		request.setSkus(skus);
		orgilldbItemDAO.copyItemsFromOrgillDB(request);
	}
	
	@Test
	public void testGetItemdetails() {
		RequestVO request = new RequestVO();
		request.setSku("00101241");
		Item item = orgilldbItemDAO.getItemDetailsBySku(request);
		Assert.assertNull(item);
		request.setSku("0010124");
		item = orgilldbItemDAO.getItemDetailsBySku(request);
		Assert.assertNotNull(item);
//		System.out.println(item.getSku()+" "+item.getIsItemExist()+" "+item.getImage());
	}
}
