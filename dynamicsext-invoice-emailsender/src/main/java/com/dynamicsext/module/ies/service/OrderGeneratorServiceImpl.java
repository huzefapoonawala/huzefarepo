package com.dynamicsext.module.ies.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.dynamicsext.module.ies.util.CommonUtil;
import com.dynamicsext.module.ies.util.Defaults;
import com.dynamicsext.module.ies.util.PDFUtil;
import com.dynamicsext.module.ies.vo.TransactionEntryVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

@Component
public class OrderGeneratorServiceImpl implements OrderGeneratorService {

	private static final Logger LOG = LoggerFactory.getLogger(OrderGeneratorServiceImpl.class);
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private VelocityEngine engine;
	@Autowired private CommonService commonService;
	
	@Value("${workorder.file.path}") private String workOrderFilePath;
	@Value("${workorder.file.prefix:}") private String workOrderFilePrefix;
	@Value("${quote.file.path}") private String quoteFilePath;
	@Value("${quote.file.prefix:}") private String quoteFilePrefix;
	
	public void generateOrder(Long orderId) {
		LOG.debug(String.format("Start: Generating order for id %s", orderId));
		
		File workOrderFolder = new File(workOrderFilePath), quoteFolder = new File(quoteFilePath);
		if (StringUtils.isBlank(workOrderFilePath) || !workOrderFolder.exists()) {
			LOG.error("'workorder.file.path' is not set or does not exist, hence cannot execute this process");
			System.exit(0);
		}
		if (StringUtils.isBlank(quoteFilePath) || !quoteFolder.exists()) {
			LOG.error("'quote.file.path' is not set or does not exist, hence cannot execute this process");
			System.exit(0);
		}
		
		List<TransactionVO> orders = jdbcTemplate.query("select o.ID as transactionNumber, AccountNumber, o.Time as transactionDate, ch.Name as cashier, [Total] as grandTotal, Tax as salesTax, c.FirstName+' '+c.LastName as billToName, c.Company as billToCompany, ISNULL(c.Address,'')+case when c.Address2 is not null and LEN(c.Address2) > 0 then ', ' else '' end+ISNULL(c.Address2,'') as billToAddress, c.City as billToCity, c.State as billToState, c.Zip as billToZip, c.PhoneNumber as billToPhone, s.Name as shipToName, s.Company as shipToCompany, ISNULL(s.Address,'')+case when s.Address2 is not null and LEN(s.Address2) > 0 then ', ' else '' end+ISNULL(s.Address2,'') as shipToAddress, s.City as shipToCity, s.State as shipToState, s.Zip as shipToZip, s.PhoneNumber as shipToPhone, o.ReferenceNumber as reference, o.Comment, c.EmailAddress, o.Type as orderType, o.Deposit, isnull((select sum(t.Amount) as tenderAmount from OrderHistory oh inner join TenderEntry t on t.OrderHistoryID = oh.ID where oh.OrderID = o.ID),0) as tenderAmount from [Order] o left join Customer c on o.CustomerID = c.ID left join Cashier ch on ch.ID = (select top 1 CashierID from OrderHistory where OrderID = o.ID order by DBTimeStamp desc) left join ShipTo s on s.ID = o.ShipToID where o.ID = ?", new BeanPropertyRowMapper<TransactionVO>(TransactionVO.class), orderId);
		if (orders.isEmpty()) {
			LOG.warn(String.format("Unable to fetch order with id %s.", orderId));
		}
		
		for (TransactionVO o : orders) {
			List<TransactionEntryVO> transactionEntries = jdbcTemplate.query("select i.ItemLookupCode, t.Description+ case when t.Comment is not null and len(t.Comment) > 0 then '<br>&nbsp;'+t.Comment else '' end as description, t.QuantityOnOrder, t.Price, (t.QuantityOnOrder+t.QuantityRTD)*t.Price as extPrice, t.QuantityRTD, t.QuantityOnOrder+t.QuantityRTD as Quantity from OrderEntry t inner join Item i on t.ItemID = i.ID where t.OrderID = ? order by t.ID", new BeanPropertyRowMapper<TransactionEntryVO>(TransactionEntryVO.class), o.getTransactionNumber());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("order", o);
			model.put("transactionEntries", transactionEntries);
			model.put("subTotal", CommonUtil.convertAmountInHtmlFormat(o.getGrandTotal()-o.getSalesTax()));
			
			if (o.getOrderType().intValue() == 2) {
				/*List<TenderVO> tenders = jdbcTemplate.query("select * from ( select t.Description, sum(t.Amount) as Amount, 2 as order_priority from OrderHistory o inner join TenderEntry t on t.OrderHistoryID = o.ID where OrderID = ? group by Description union select 'Deposit' as description, sum(t.Amount) as Amount, 1 as order_priority from OrderHistory o inner join TenderEntry t on t.OrderHistoryID = o.ID where OrderID = ? and t.TransactionNumber = 0 having sum(t.Amount) > 0 ) t order by order_priority;", new BeanPropertyRowMapper<TenderVO>(TenderVO.class), o.getTransactionNumber(), o.getTransactionNumber());
				model.put("tenders", tenders);
				
				double totalTender = 0;
				for (TenderVO te : tenders) {
					totalTender += te.getAmount();
				}*/
				double newBalance = o.getGrandTotal() - o.getTenderAmount();
				model.put("newBalance", CommonUtil.convertAmountInHtmlFormat(newBalance));
			}
			
			boolean isHtml = false;
			commonService.populateStoreDetails(model, isHtml);
			String filename = (o.getOrderType().intValue() == Defaults.WORK_ORDER_TYPE ? workOrderFilePrefix : quoteFilePrefix)+Integer.valueOf(o.getTransactionNumber()).toString()+(isHtml ? Defaults.INVOICE_FILE_EXTENSION : Defaults.PDF_FILE_EXTENSION);
			File toPreviewFile = new File(o.getOrderType().intValue() == Defaults.WORK_ORDER_TYPE ? workOrderFolder : quoteFolder, filename);
			if (isHtml) {
				String text = generateOrder(o.getOrderType(), model);
				commonService.saveFile(toPreviewFile,text);
			} else {
				try {
					model.put("documentType", o.getOrderType());
					PDFUtil.generateQuoteAndWorkOrder(toPreviewFile, model);
				} catch (Exception e) {
					LOG.error("Error occurred while generating PDF for "+(o.getOrderType() == Defaults.WORK_ORDER_TYPE ? "work order" : "quote")+" with id "+orderId, e);
				}
			}
		}
		
		LOG.debug(String.format("End: Generating order for id %s", orderId));
	}
	
	private String generateOrder(int orderType, Map<String, Object> model){
		String text = VelocityEngineUtils.mergeTemplateIntoString(this.engine, orderType == Defaults.WORK_ORDER_TYPE ? "order-template.html" : "quote-template.html", "UTF-8", model);
		return text;
	}
}
