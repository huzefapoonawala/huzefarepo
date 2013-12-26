package com.simplydifferent.dao.intr;

import com.simplydifferent.vo.ProductStockReport;
import com.simplydifferent.vo.PurchaseReport;
import com.simplydifferent.vo.ReportRequest;

public interface ReportDAO {

	PurchaseReport getPurchaseReport(ReportRequest request) throws Exception;

	ProductStockReport getProductStockReport(ReportRequest request) throws Exception;

}
