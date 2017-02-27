package com.dynamicsext.module.ies.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dynamicsext.module.ies.util.CommonUtil;
import com.dynamicsext.module.ies.util.Defaults;
import com.dynamicsext.module.ies.util.PDFUtil;
import com.dynamicsext.module.ies.vo.TenderVO;
import com.dynamicsext.module.ies.vo.TransactionEntryVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

@Component
public class InvoiceGeneratorServiceImpl implements InvoiceGeneratorService {

	private static final Logger LOG = LoggerFactory.getLogger(InvoiceGeneratorServiceImpl.class);
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private CommonService commonService;
	
	@Value("${invoice.file.path}") private String invoiceFilePath;
	@Value("${invoice.file.prefix:}") private String invoiceFilePrefix;
	
	@Override
	public void generateInvoice(Long transactionNumber) {
		LOG.debug(String.format("Start: Generating invoice for transaction number %s", transactionNumber));
		File invoiceFolder = new File(invoiceFilePath);
		if (StringUtils.isBlank(invoiceFilePath) || !invoiceFolder.exists()) {
			LOG.error("'invoice.file.path' is not set or does not exist, hence cannot execute this process");
			System.exit(0);
		}
		
		List<TransactionVO> transactions = jdbcTemplate.query("select TransactionNumber, AccountNumber, t.Time as transactionDate, ch.Name as cashier, [Total] as grandTotal, [SalesTax], c.FirstName+' '+c.LastName as billToName, c.Company as billToCompany, ISNULL(c.Address,'')+case when c.Address2 is not null and LEN(c.Address2) > 0 then ', ' else '' end+ISNULL(c.Address2,'') as billToAddress, c.City as billToCity, c.State as billToState, c.Zip as billToZip, c.PhoneNumber as billToPhone, s.Name as shipToName, s.Company as shipToCompany, ISNULL(s.Address,'')+case when s.Address2 is not null and LEN(s.Address2) > 0 then ', ' else '' end+ISNULL(s.Address2,'') as shipToAddress, s.City as shipToCity, s.State as shipToState, s.Zip as shipToZip, s.PhoneNumber as shipToPhone, t.ReferenceNumber as reference, t.Comment, c.EmailAddress, case c.CustomNumber3 when 1 then 1 else 0 end as isOptForEmail, c.CustomNumber4 as fileFormat from [Transaction] t inner join Customer c on t.CustomerID = c.ID left join Cashier ch on t.CashierID = ch.ID left join ShipTo s on s.ID = t.ShipToID where t.TransactionNumber = ?;", new BeanPropertyRowMapper<TransactionVO>(TransactionVO.class), transactionNumber);
		if (transactions.isEmpty()) {
			LOG.debug(String.format("Unable to fetch invoice with transaction number %s.", transactionNumber));
		}
		
		for (TransactionVO t : transactions) {
			List<TenderVO> tenders = jdbcTemplate.query("select TransactionNumber, Description, Amount from TenderEntry where TransactionNumber = ?;", new BeanPropertyRowMapper<TenderVO>(TenderVO.class), t.getTransactionNumber());
			double totalTender = 0;
			for (TenderVO te : tenders) {
				totalTender += te.getAmount();
			}
			double changeDue = totalTender - t.getGrandTotal();
			
			List<TransactionEntryVO> transactionEntries = jdbcTemplate.query("select i.ItemLookupCode, i.Description+ case when t.Comment is not null and len(t.Comment) > 0 then '<br>&nbsp;('+t.Comment+')' else '' end + case when r.Code is not null and len(r.Code) > 0 then '<br>&nbsp;(Return code: '+r.Code+')' else '' end as description, t.Quantity, t.Price, t.Quantity*t.Price as extPrice from TransactionEntry t inner join Item i on t.ItemID = i.ID left join ReasonCode r on t.ReturnReasonCodeID = r.ID where TransactionNumber = ?;", new BeanPropertyRowMapper<TransactionEntryVO>(TransactionEntryVO.class), t.getTransactionNumber());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("transaction", t);
			model.put("tenders", tenders);
			model.put("transactionEntries", transactionEntries);
			model.put("changeDue", CommonUtil.convertAmountInHtmlFormat(changeDue));
			model.put("subTotal", CommonUtil.convertAmountInHtmlFormat(t.getGrandTotal()-t.getSalesTax()));
			
			commonService.populateStoreDetails(model, false);
			
			File invoiceFile = new File(invoiceFilePath, invoiceFilePrefix+Integer.valueOf(t.getTransactionNumber()).toString()+"-"+t.getBillToCompany()+Defaults.PDF_FILE_EXTENSION);
			try {
				PDFUtil.generateInvoice(invoiceFile, model);
			} catch (Exception e) {
				LOG.error("Error occurred while generating PDF of invoice for transaction number "+transactionNumber, e);
			}
		}
		LOG.debug(String.format("End: Generating invoice for transaction number %s", transactionNumber));
	}
}
