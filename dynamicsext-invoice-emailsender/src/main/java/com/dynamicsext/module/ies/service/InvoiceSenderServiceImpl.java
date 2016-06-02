package com.dynamicsext.module.ies.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.dynamicsext.module.ies.util.CommonUtil;
import com.dynamicsext.module.ies.util.Defaults;
import com.dynamicsext.module.ies.vo.TenderVO;
import com.dynamicsext.module.ies.vo.TransactionEntryVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

@Component
public class InvoiceSenderServiceImpl implements InvoiceSenderService {

	private static final Logger LOG = LoggerFactory.getLogger(InvoiceSenderServiceImpl.class);
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private JavaMailSender javaMailSender;
	@Autowired private VelocityEngine engine;
	
	@Value("${invoice.email.subject}") private String emailSubject;
	@Value("${invoice.email.bcc}") private String emailBccTo;
	@Value("${invoice.store.address.show}") private Boolean showStoreAddress;
	@Value("${invoice.store.logo}") private String storeLogoImg;
	
	@Value("${invoice.topreview.file.path}") private String toPreviewFilePath;
	@Value("${invoice.previewed.file.path}") private String previewedFilePath;
	
	public void fetchInvoiceAndSendEmails() {
		File toPreviewFolder = new File(toPreviewFilePath), previewedFolder = new File(previewedFilePath);
		if (StringUtils.isBlank(toPreviewFilePath) || StringUtils.isBlank(previewedFilePath)) {
			LOG.error("'invoice.topreview.file.path' and/or 'invoice.previewed.file.path' is not set, hence cannot execute this process");
			System.exit(0);
		}
		if (!toPreviewFolder.exists() || !previewedFolder.exists()) {
			LOG.error(String.format("'%s' and/or '%s' does not exist, hence cannot execute this process", toPreviewFilePath, previewedFilePath));
			System.exit(0);
		}
		LOG.debug(String.format("Fetching transactions for sending emails"));
		StringBuilder transactions2Update = new StringBuilder();
		List<TransactionVO> transactions = jdbcTemplate.query("select TransactionNumber, AccountNumber, t.Time as transactionDate, ch.Name as cashier, [Total] as grandTotal, [SalesTax], c.FirstName+' '+c.LastName as billToName, c.Company as billToCompany, ISNULL(c.Address,'')+case when c.Address2 is not null and LEN(c.Address2) > 0 then ', ' else '' end+ISNULL(c.Address2,'') as billToAddress, c.City as billToCity, c.State as billToState, c.Zip as billToZip, c.PhoneNumber as billToPhone, s.Name as shipToName, s.Company as shipToCompany, ISNULL(s.Address,'')+case when s.Address2 is not null and LEN(s.Address2) > 0 then ', ' else '' end+ISNULL(s.Address2,'') as shipToAddress, s.City as shipToCity, s.State as shipToState, s.Zip as shipToZip, s.PhoneNumber as shipToPhone, t.ReferenceNumber as reference, t.Comment, c.EmailAddress, case c.CustomNumber3 when 1 then 1 else 0 end as isOptForEmail  from [Transaction] t inner join Customer c on t.CustomerID = c.ID left join Cashier ch on t.CashierID = ch.ID left join ShipTo s on s.ID = t.ShipToID where t.IsEmailSent = 0;", new BeanPropertyRowMapper<TransactionVO>(TransactionVO.class));
		if (transactions.isEmpty()) {
			LOG.debug("No transactions found to send emails");
		}
		else{
			for (TransactionVO t : transactions) {
				if (StringUtils.isNotBlank(t.getEmailAddress()) && t.getIsOptForEmail()) {
					String filename = t.getBillToCompany()+"-"+Integer.valueOf(t.getTransactionNumber()).toString()+Defaults.INVOICE_FILE_EXTENSION;
					File toPreviewFile = new File(toPreviewFolder, filename), previewedFile = new File(previewedFolder, filename);
					if (toPreviewFile.isFile() || previewedFile.isFile()) {
						LOG.debug(String.format("Invoice with transaction number %s is already generated", t.getTransactionNumber()));
					} else {
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
						model.put("showStoreAddress", showStoreAddress);
						model.put("storeLogoImg", storeLogoImg);
						
						String text = generateInvoice(model);
						saveInvoice(toPreviewFile ,text, Integer.valueOf(t.getTransactionNumber()).toString());
					}
					
					if (previewedFile.isFile()) {
						String text = extractInvoiceText(previewedFile, Integer.valueOf(t.getTransactionNumber()).toString());
						if (StringUtils.isNotBlank(text)) {
							if (sendMail(t.getEmailAddress(), text, t.getTransactionNumber())) {
								transactions2Update.append(t.getTransactionNumber()).append(",");
								previewedFile.delete();
							}
						}
					}
				}
				else{
					LOG.debug(String.format("Ignoring invoice with transaction number '%s' as customer email not set or customer with name '%s' has not opted for emails", t.getTransactionNumber(), t.getBillToCompany()));
					transactions2Update.append(t.getTransactionNumber()).append(",");
				}
			}
			if (transactions2Update.length() > 0) {
				transactions2Update.deleteCharAt(transactions2Update.length()-1);
				jdbcTemplate.update(String.format("update [Transaction] set IsEmailSent = 1 where TransactionNumber in (%s)", transactions2Update.toString()));
				LOG.debug(String.format("Updated transactions [%s]", transactions2Update.toString()));
			}
			else{
				LOG.debug("No transactions to update");
			}
		}
		
	}
	
	private boolean sendMail(String emailAddress, String text, Integer transactionNumber) {
		boolean isMailSent = true;
		MimeMessage mail = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setTo(emailAddress);
			if (StringUtils.isNotBlank(emailBccTo)) {
				helper.setBcc(emailBccTo);
			}
			helper.setSubject(emailSubject);
			helper.setText(text, true);
			javaMailSender.send(mail);
			LOG.debug(String.format("Sent invoice with transaction number %s to %s", transactionNumber, emailAddress));
		} catch (Exception e) {
			LOG.error(String.format("Error occurred while sending invoice with transaction number %s to %s ", transactionNumber, emailAddress), e);
			isMailSent = false;
		} finally {}
		return isMailSent;
	}
	
	private String generateInvoice(Map<String, Object> model){
		String text = VelocityEngineUtils.mergeTemplateIntoString(this.engine,"invoice-template.html", "UTF-8", model);
		return text;
	}
	
	private void saveInvoice(File file, String text, String transactionNumber) {
		try {
			LOG.debug(String.format("Invoice with transaction number '%s' stored at location '%s' for preview", transactionNumber, toPreviewFilePath));
			FileOutputStream out = new FileOutputStream(file);
			out.write(text.getBytes());
			out.close();
		} catch (Exception e) {
			LOG.error("Error occurred while saving invoice for transaction "+transactionNumber+".", e);
		}
	}
	
	private String extractInvoiceText(File file, String transactionNumber) {
		StringBuilder text = new StringBuilder();
		try {
			LOG.debug(String.format("Extracting invoice data with transaction number '%s' stored at location '%s' as it is previewed", transactionNumber, previewedFilePath));
			FileReader in = new FileReader(file);
			int ch;
			while ((ch = in.read()) != -1) {
				text.append((char)ch);
			}
			in.close();
		} catch (Exception e) {
			LOG.error("Error occurred while extracting invoice data for transaction "+transactionNumber+".", e);
		}
		return text.toString();
	}
}
