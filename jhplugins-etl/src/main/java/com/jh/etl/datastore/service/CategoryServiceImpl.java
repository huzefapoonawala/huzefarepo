package com.jh.etl.datastore.service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.datastore.dao.CategoryRepository;
import com.jh.etl.datastore.entity.Category;
import com.jh.etl.datastore.mapper.CategoryMapper;

@Service(value="categoryService")
public class CategoryServiceImpl implements CategoryService {

	private static final Log LOG = LogFactory.getLog(CategoryServiceImpl.class);
	
	@Autowired private CategoryRepository categoryRepository;
	
	@Autowired private CategoryMapper categoryMapper;
	
	@Override
	@Transactional
	public void persistCategories(List<CategoryDto> categories) {
		LOG.debug("Start: Persisting categories...");
		Map<String, Category> existingCategories = categoryRepository
				.findBySuppliersAndCodes(
						categories.stream().map(CategoryDto::concatCodeAndSupplier).collect(Collectors.toList())
				)
				.stream()
				.collect(Collectors.toMap(Category::concatCodeAndSupplier, c->c));
		Map<String,Category> categories2Save = categories
				.stream()
				.filter(Predicate.not(cate -> existingCategories.containsKey(cate.concatCodeAndSupplier())))
				.map(m -> categoryMapper.toCategory(m))
				.collect(Collectors.toMap(Category::concatCodeAndSupplier, c->c));
		categories2Save.values().stream().filter(c -> c.getParent() != null)
			.forEach(c -> {
				String key = c.getParent().concatCodeAndSupplier();
				c.setParent(existingCategories.getOrDefault(key, categories2Save.getOrDefault(key, null)));
			});
		categoryRepository.saveAll(categories2Save.values());
		LOG.debug(String.format("%d categories persisted.", categories2Save.size()));
		LOG.debug("End: Persisting categories.");
	}
}
