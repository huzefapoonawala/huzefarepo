package com.jh.etl.datastore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.datastore.dao.ProductRepository;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.datastore.entity.Product;
import com.jh.etl.datastore.mapper.ProductMapper;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	private static final Log LOG = LogFactory.getLog(ProductServiceImpl.class); 
	
	@Autowired private ProductRepository productRepository;
	@Autowired private CategoryService categoryService;
	@Autowired private ProductMapper productMapper;
	
	@Override
	@Transactional
	public void persistProducts(List<ProductDto> products) {
		LOG.debug("Start: Persisting products...");
		List<String> categoriesList = new ArrayList<>();
		LOG.trace("Fetching existing product list");
		List<String> existingProducts = productRepository
				.fetchAllProductIdsAndSkusAndSuppliers()
				.stream()
				.map(ep -> ep.getSku().concat(",").concat(ep.getSupplier().toString()))
				.collect(Collectors.toList());
		LOG.trace("Fetched existing product list");
		LOG.trace("Checking products 2 save");
		List<Product> products2Save = products
				.parallelStream()
				.filter(p -> !existingProducts.contains(p.concatSkuAndSupplier()))
				.map(p -> {
					categoriesList.add(p.getCategory().concatCodeAndSupplier());
					return productMapper.toProduct(p);
				})
				.collect(Collectors.toList());
		LOG.trace("Checked products 2 save");
		if (!products2Save.isEmpty()) {
			Map<String, Category> cate2Map = categoryService.getCategoriesByCodesAndSuppliers(categoriesList)
					.stream()
					.collect(Collectors.toMap(Category::concatCodeAndSupplier, c->c));
			products2Save.forEach(p -> p.setCategory(cate2Map.get(p.getCategory().concatCodeAndSupplier())));
			productRepository.saveAll(products2Save);
		}
		LOG.debug(String.format("%d products persisted successfully.", products2Save.size()));
		LOG.debug("End: Persisting products.");
	}

}
