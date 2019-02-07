package com.jh.etl.datastore.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

	Category findBySupplierAndCode(Supplier supplier, String code);
	
	default Specification<Category> inWithConcatCodeAndSupplier(List<String> inParams){
		return (root,query,cb) -> inParams == null || inParams.isEmpty() ? 
				cb.disjunction() : 
					cb.concat(cb.concat(root.<String>get("code"), ","), root.<String>get("supplier")).in(inParams);
	}
}
