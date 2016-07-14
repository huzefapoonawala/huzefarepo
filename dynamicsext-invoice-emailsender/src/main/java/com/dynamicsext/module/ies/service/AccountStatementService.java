package com.dynamicsext.module.ies.service;

public interface AccountStatementService {

	void generateAccountStatement(Long customerId, String fromDate, String toDate);

}
