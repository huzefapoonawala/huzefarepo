package test;

import javax.annotation.Resource;

import org.junit.Test;

import com.jh.dao.item.ItemDAO;
import com.jh.vo.Item;
import com.jh.vo.RequestVO;

public class ItemDAOTest extends Setup{

	@Resource private ItemDAO itemDAO;
	
	@Test
	public void testItemDetails() {
		RequestVO request = new RequestVO();
		request.setSku("15110");
		request.setAlias("rc");
		for (Item item : itemDAO.getItemDetails(request)) {
			System.out.println(item.toString());
		}
	}
}
