
package com.igomall.service;

import java.util.List;
import java.util.Map;

import com.igomall.entity.ProductNotify;
import com.igomall.entity.SafeKey;

public interface MailService {

	void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);

	void send(String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);

	void send(String toMail, String subject, String templatePath, Map<String, Object> model);

	void send(String toMail, String subject, String templatePath);

	void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail);

	void sendFindPasswordMail(String toMail, String username, SafeKey safeKey);

	void sendProductNotifyMail(ProductNotify productNotify);

	void sendBackupMail(String smtpFromMail, String smtpHost, Integer smtpPort,String smtpUsername, String smtpPassword, String string,List<String> attachments);

}