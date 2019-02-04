package com.jh.etl.datastore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	Category findBySupplierAndCode(Supplier supplier, String code);
	
	@Query(nativeQuery=true, value="select c.* from Category c where concat(CODE,',',SUPPLIER) in (:params)")
	List<Category> findBySuppliersAndCodes(@Param("params") List<String> params);
	
}
