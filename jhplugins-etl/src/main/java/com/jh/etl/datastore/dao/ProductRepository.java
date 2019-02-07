package com.jh.etl.datastore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jh.etl.common.enums.Supplier;
import com.jh.etl.datastore.entity.Product;
import com.jh.etl.datastore.projection.ProductIdAndSkuAndSupplier;

public interface ProductRepository extends CrudRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	Product findBySkuAndSupplier(String sku, Supplier supplier);
	
	@Query("select id, sku, supplier from product where concat(sku,',',supplier) in (:params)")
	List<ProductIdAndSkuAndSupplier> findBySkusAndSuppliers (@Param("params") List<String> list);
	
	@Query("select id as id, sku as sku, supplier as supplier from product")
	List<ProductIdAndSkuAndSupplier> fetchAllProductIdsAndSkusAndSuppliers();
	
	
	/*List<ProductIdAndSkuAndSupplier> fetchAllProductIdsAndSkusAndSuppliers (Specification<Product> specs);
	
	default List<ProductIdAndSkuAndSupplier> findBySkusAndSuppliers (List<String> inParams){
		return fetchAllProductIdsAndSkusAndSuppliers((root,query,cb) -> inParams == null || inParams.isEmpty() ? 
				cb.disjunction() : 
					cb.concat(cb.concat(root.<String>get("sku"), ","), root.<String>get("supplier")).in(inParams));
	}*/
}
