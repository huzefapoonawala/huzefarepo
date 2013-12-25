package com.jh.util;

import java.util.Arrays;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailSenderUtil {

	private static Logger logger = Logger.getLogger(EmailSenderUtil.class);
	
	private JavaMailSender mailSender;
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	private List<String> emailIds;
	public void setEmailIds(List<String> emailIds) {
		this.emailIds = emailIds;
	}
	
	private List<String> notificationEmailIds;	
	public void setNotificationEmailIds(List<String> notificationEmailIds) {
		this.notificationEmailIds = notificationEmailIds;
	}

	public void sendEmail(String subject, String text, List<String> emailIds){
		try {
			if (emailIds != null && !emailIds.isEmpty()) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message,true);
				helper.setSubject(StringUtils.defaultIfBlank(subject, ""));
				for (String emailAdd : emailIds) {
					helper.addTo(new InternetAddress(emailAdd));
				}
				helper.setText(text);
				mailSender.send(message);
			}
		} catch (Exception e) {
			logger.error("Error occurred while sending email.",e);
		}
	}
	
	public void sendEmail(String subject, String text) {
		sendEmail(subject, text, emailIds);
	}
	
	public void sendEmail(String subject, String text, String emailId) {
		sendEmail(subject, text, Arrays.asList(new String[]{emailId}));
	}
	
	public void sendNoficationEmail(String subject, String text) {
		sendEmail(subject, text, notificationEmailIds);
	}
}
