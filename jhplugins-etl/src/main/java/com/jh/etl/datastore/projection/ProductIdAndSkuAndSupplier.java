package com.jh.etl.datastore.projection;

import com.jh.etl.common.enums.Supplier;

public interface ProductIdAndSkuAndSupplier {

	Integer getId();
	String getSku();
	Supplier getSupplier();
}
