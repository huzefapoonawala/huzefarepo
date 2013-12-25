package com.jh.dao.po;

import java.io.Writer;
import java.util.List;

import com.jh.vo.PurchaseOrderDetails;
import com.jh.vo.RequestVO;

public interface PurchaseOrderDAO {

	List<PurchaseOrderDetails> getDetailsForPurchaseOrder(RequestVO request);

	void generatePO(RequestVO request, Writer writer) throws Exception;

}
