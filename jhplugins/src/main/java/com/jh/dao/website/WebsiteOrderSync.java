package com.jh.dao.website;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.jh.util.EmailSenderUtil;

public class WebsiteOrderSync extends WebsiteProductsDAOImpl {

	private static Logger logger = Logger.getLogger(WebsiteOrderSync.class);
	
	private boolean isEmailLogs;	
	public void setEmailLogs(boolean isEmailLogs) {
		this.isEmailLogs = isEmailLogs;
	}
	
	private boolean isNotifyOnError;
	public void setNotifyOnError(boolean isNotifyOnError) {
		this.isNotifyOnError = isNotifyOnError;
	}
	
	private EmailSenderUtil emailSenderUtil;
	public void setEmailSenderUtil(EmailSenderUtil emailSenderUtil) {
		this.emailSenderUtil = emailSenderUtil;
	}

	private List<String> orders2ProcessFrom;
	public void setOrders2ProcessFrom(List<String> orders2ProcessFrom) {
		this.orders2ProcessFrom = orders2ProcessFrom;
	}

	public void checkAndUpdateOrderAcknowledgement() {
		if (orders2ProcessFrom != null && !orders2ProcessFrom.isEmpty()) {
			for (String o : orders2ProcessFrom) {
				logger.debug("Start: Checking and updating acknowledgement for orders from "+o);
				try {
					String result = checkAndUpdateOrderAcknowledgement(o);
					if (isEmailLogs && !result.isEmpty()) {
						emailSenderUtil.sendEmail("Jamaica Hardware - website order sync logs", result);
					}
				} catch (Exception e) {
					logger.error("Error occurred while checking order ack for "+o+".",e);
					if (isNotifyOnError) {
						emailSenderUtil.sendNoficationEmail("Jamaica Hardware - website order sync error logs", "Error occurred while checking order ack for "+o+" ["+ExceptionUtils.getFullStackTrace(e)+"].");
					}
				}
				logger.debug("End: Checking and updating acknowledgement for orders from "+o);
			}
		}
	}
	
	public void checkAndUpdateOrderShippingDetails() {
		if (orders2ProcessFrom != null && !orders2ProcessFrom.isEmpty()) {
			for (String o : orders2ProcessFrom) {
				logger.debug("Start: Checking and updating shipping details for orders from "+o);
				try {
					String result = checkAndUpdateOrderShipping(o);
					if (isEmailLogs && !result.isEmpty()) {
						emailSenderUtil.sendEmail("Jamaica Hardware - website order shipping details logs", result);
					}
				} catch (Exception e) {
					logger.error("Error occurred while checking order shipping details for "+o+".",e);
					if (isNotifyOnError) {
						emailSenderUtil.sendNoficationEmail("Jamaica Hardware - website order shipping details error logs", "Error occurred while checking order shipping details for "+o+" ["+ExceptionUtils.getFullStackTrace(e)+"].");
					}
				}
				logger.debug("End: Checking and updating shipping details for orders from "+o);
			}
		}
	}
}
