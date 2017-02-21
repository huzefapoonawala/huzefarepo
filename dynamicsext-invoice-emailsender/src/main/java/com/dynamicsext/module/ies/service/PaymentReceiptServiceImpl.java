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
import com.dynamicsext.module.ies.vo.PaymentEntryVO;
import com.dynamicsext.module.ies.vo.TenderVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

@Component
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentReceiptServiceImpl.class);
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private CommonService commonService;
	
	@Value("${paymentreceipt.file.path}") private String prFilePath;
	@Value("${paymentreceipt.file.prefix:}") private String prFilePrefix;
	
	public void generatePaymentReceipt(Long prId) {
		LOG.debug(String.format("Start: Generating payment receipt for id %s", prId));
		
		File prFolder = new File(prFilePath);
		if (StringUtils.isBlank(prFilePath) || !prFolder.exists()) {
			LOG.error("'paymentreceipt.file.path' is not set or does not exist, hence cannot execute this process");
			System.exit(0);
		}
		
		List<TransactionVO> paymentReceipts = jdbcTemplate.query("select p.ID as transactionNumber, AccountNumber, p.Time as transactionDate, ch.Name as cashier,  c.FirstName+' '+c.LastName as billToName, c.Company as billToCompany, ISNULL(c.Address,'')+case when c.Address2 is not null and LEN(c.Address2) > 0 then ', ' else '' end+ISNULL(c.Address2,'') as billToAddress, c.City as billToCity, c.State as billToState, c.Zip as billToZip, c.PhoneNumber as billToPhone, s.Name as shipToName, s.Company as shipToCompany, ISNULL(s.Address,'')+case when s.Address2 is not null and LEN(s.Address2) > 0 then ', ' else '' end+ISNULL(s.Address2,'') as shipToAddress, s.City as shipToCity, s.State as shipToState, s.Zip as shipToZip, s.PhoneNumber as shipToPhone, p.Amount as grandTotal, c.EmailAddress, p.CustomerID from Payment p inner join Customer c on p.CustomerID = c.ID left join Cashier ch on ch.ID = p.CashierID left join ShipTo s on s.ID = 0 where p.ID = ?;", new BeanPropertyRowMapper<TransactionVO>(TransactionVO.class), prId);
		if (paymentReceipts.isEmpty()) {
			LOG.warn(String.format("Unable to fetch payment receipt with id %s.", prId));
		}
		
		for (TransactionVO pr : paymentReceipts) {
			String query = "select * from ( select a2.TransactionNumber, a2.Date as invoiceDate, a2.DueDate, a2.OriginalAmount as invoiceAmount, abs(a1.Amount) as payment, a2.Balance as balanceDue from AccountReceivableHistory a1 inner join AccountReceivable a2 on a2.ID = a1.AccountReceivableID where a1.PaymentID = ? union select TransactionNumber, Date as invoiceDate, DueDate, OriginalAmount as invoiceAmount, 0 as payment, Balance as balanceDue from AccountReceivable where CustomerID = ? and Balance > 0 and ID not in (select AccountReceivableID from AccountReceivableHistory where PaymentID = ?) ) t order by invoiceDate;";
			List<PaymentEntryVO> paymentEntries = jdbcTemplate.query(query, new BeanPropertyRowMapper<PaymentEntryVO>(PaymentEntryVO.class), pr.getTransactionNumber(), pr.getCustomerId(), pr.getTransactionNumber());
			double previousBal = 0;
			for (PaymentEntryVO p : paymentEntries) {
				previousBal += p.getPayment()+p.getBalanceDue();
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("pr", pr);
			model.put("paymentEntries", paymentEntries);
			model.put("previousBal", CommonUtil.convertAmountInHtmlFormat(previousBal));
			model.put("newBal", CommonUtil.convertAmountInHtmlFormat(previousBal-pr.getGrandTotal()));
			
			List<TenderVO> tenders = jdbcTemplate.query("select t.Description, sum(t.Amount) as Amount from TenderEntry t where PaymentID = ? group by Description;", new BeanPropertyRowMapper<TenderVO>(TenderVO.class), pr.getTransactionNumber());
			model.put("tenders", tenders);
			
			boolean isHtml = false;
			commonService.populateStoreDetails(model, isHtml);
			
			String filename = prFilePrefix+Integer.valueOf(pr.getTransactionNumber()).toString()+(isHtml ? Defaults.INVOICE_FILE_EXTENSION : Defaults.PDF_FILE_EXTENSION);
			File toPreviewFile = new File(prFolder, filename);
			if (isHtml) {
				String text = commonService.generatePaymentReceipt("paymentreceipt-template.html", model);
				commonService.saveFile(toPreviewFile,text);
			} else {
				try {
					PDFUtil.generateAccountPayment(toPreviewFile, model);
				} catch (Exception e) {
					LOG.error("Error occurred while generating PDF of account payment for id "+prId, e);
				}
			}
		}
		
		LOG.debug(String.format("End: Generating payment receipt for id %s", prId));
	}
}
