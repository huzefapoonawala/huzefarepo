package com.dynamicsext.module.ies.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.dynamicsext.module.ies.util.CommonUtil;
import com.dynamicsext.module.ies.util.Defaults;
import com.dynamicsext.module.ies.vo.AccountStatementDetailsVO;
import com.dynamicsext.module.ies.vo.TransactionVO;

@Component
public class AccountStatementServiceImpl implements AccountStatementService {

	private static final Logger LOG = LoggerFactory.getLogger(AccountStatementServiceImpl.class);
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private CommonService commonService;
	
	@Value("${accountstatement.file.path}") private String asFilePath;
	
	private static String INPUT_DATE_FORMAT = "yyyy-MM-dd";
	
	@Override
	public void generateAccountStatement(Long customerId, String fromDate, String toDate) {
		LOG.debug(String.format("Start: Generating account statement for customer with id %s", customerId));
		
		Date //fDate = null, 
				tDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(INPUT_DATE_FORMAT);
			//fDate = sdf.parse(fromDate);
			tDate = sdf.parse(toDate);
			tDate.setTime(tDate.getTime()+86399000);
		} catch (Exception e) {
			LOG.error("Invalid input date formats (expected date format is 'yyyy-MM-dd').");
			System.exit(-1);
		}
		
		File asFolder = new File(asFilePath);
		if (StringUtils.isBlank(asFilePath) || !asFolder.exists()) {
			LOG.error("'accountstatement.file.path' is not set or does not exist, hence cannot execute this process");
			System.exit(-1);
		}
		
		List<TransactionVO> accountStatements = jdbcTemplate.query("select AccountNumber, c.FirstName+' '+c.LastName as billToName, c.Company as billToCompany, ISNULL(c.Address,'')+case when c.Address2 is not null and LEN(c.Address2) > 0 then ', ' else '' end+ISNULL(c.Address2,'') as billToAddress, c.City as billToCity, c.State as billToState, c.Zip as billToZip, c.PhoneNumber as billToPhone, c.EmailAddress, c.ID as customerId from Customer c where c.ID = ?;", new BeanPropertyRowMapper<TransactionVO>(TransactionVO.class), customerId);
		if (accountStatements.isEmpty()) {
			LOG.warn(String.format("Unable to fetch account statement for customer with id %s.", customerId));
		}
		
		for (final TransactionVO as : accountStatements) {		
			String query = "select dayRange, sum(Balance) as balance from( select *, case when daysPast <= 0 or Balance < 0 then 0 when daysPast between 1 and 30 then 30 when daysPast between 31 and 60 then 60 when daysPast between 61 and 90 then 90 else -1 end as dayRange from( select Balance, Date, DATEDIFF(dd, Date, ?) as daysPast from AccountReceivable where Balance <> 0 and CustomerID = ? and Date <= ? ) t ) r group by dayRange;";

			jdbcTemplate.query(query, new ResultSetExtractor<String>(){
				@Override
				public String extractData(ResultSet rs) throws SQLException,
						DataAccessException {
					as.setGrandTotal(0.0);
					while (rs.next()) {
						switch (rs.getInt("dayRange")) {
						case 0:
							as.setRange0(rs.getDouble("balance"));
							break;
							
						case 30:
							as.setRange1To30(rs.getDouble("balance"));
							break;
						
						case 60:
							as.setRange31To60(rs.getDouble("balance"));
							break;
							
						case 90:
							as.setRange61To90(rs.getDouble("balance"));
							break;
							
						case -1:
							as.setRange90(rs.getDouble("balance"));
							break;
							
						default:
							break;
						}
						as.setGrandTotal(as.getGrandTotal()+rs.getDouble("balance"));
					}
					return null;
				}}, tDate, as.getCustomerId(), tDate);
			
			query = "select case TransactionNumber when 0 then 'DC-'+cast(ID as nvarchar(30)) else 'TR-'+cast(TransactionNumber as nvarchar(30)) end as invoiceNumber, Balance, Date as invoiceDate, DueDate, type, case when Balance > 0 then 'New Charge' else case when TransactionNumber <> 0 then 'Return' else 'Payment received' end end as details from AccountReceivable where Balance <> 0 and CustomerID = ? and Date <= ? order by Date;";
			
			List<AccountStatementDetailsVO> details = jdbcTemplate.query(query, new BeanPropertyRowMapper<AccountStatementDetailsVO>(AccountStatementDetailsVO.class), as.getCustomerId(), tDate);
			double totalDebit = 0, totalCredit = 0;
			for (AccountStatementDetailsVO a : details) {
				if (a.getBalance() < 0) {
					totalCredit += Math.abs(a.getBalance());
				}
				else{
					totalDebit += a.getBalance();
				}
			}
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("customer", as);
			model.put("toDate", CommonUtil.convertDateInHtmlFormat(tDate));
			model.put("details", details);
			model.put("totalDebit", CommonUtil.convertAmountInHtmlFormat(totalDebit));
			model.put("totalCredit", CommonUtil.convertAmountInHtmlFormat(totalCredit));
			commonService.populateStoreDetails(model);			
			String text = commonService.generatePaymentReceipt("accountstatement-template.html", model);
			
			String filename = Integer.valueOf(as.getCustomerId()).toString()+Defaults.INVOICE_FILE_EXTENSION;
			File toPreviewFile = new File(asFolder, filename);
			
			saveAccountStatement(toPreviewFile,text, Integer.valueOf(as.getCustomerId()).toString());
		}
		
		LOG.debug(String.format("End: Generating account statement for customer with id %s", customerId));		
	}
	
	private void saveAccountStatement(File file, String text, String prId) {
		try {
			LOG.debug(String.format("Account statement for customer with id '%s' is stored at location '%s' for preview", prId, file.getParent()));
			FileOutputStream out = new FileOutputStream(file);
			out.write(text.getBytes());
			out.close();
		} catch (Exception e) {
			LOG.error("Error occurred while saving order for id "+prId+".", e);
		}
	}
}
