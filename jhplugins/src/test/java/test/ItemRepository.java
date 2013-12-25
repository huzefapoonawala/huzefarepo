package test;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jh.vo.Item;

public interface ItemRepository extends MongoRepository<Item, String> {

	public Item findBySku(String sku);
}
