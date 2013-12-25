package test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;

import com.jh.vo.Item;
import com.jh.vo.WebProduct;

@ContextConfiguration(locations={"classpath:jh-test-context.xml","classpath:jh-mongodb-context.xml"}) 
public class MongoTest extends Setup{

	@Resource MongoOperations mongoTemplate;
	
	@Test
	public void testItemRepository() {
		WebProduct item = new WebProduct();
		item.setSku("test1");
//		mongoTemplate.save(item);
//		System.out.println(item);
		
		List<WebProduct> items = mongoTemplate.findAll(WebProduct.class);
		System.out.println(items.size());
		for (WebProduct i : items) {
			System.out.println(i.toString());
		}
		
		Query query = new Query(Criteria.where("sku").is("7633234"));
		item = mongoTemplate.findOne(query, WebProduct.class);
		System.out.println(item.toString());
		
//		mongoTemplate.remove(query, WebItem.class);
	}
}
