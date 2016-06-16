
package com.igomall.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.igomall.Setting;
import com.igomall.service.MailService;
import com.igomall.util.BackUpMySQL;
import com.igomall.util.SettingUtils;


@Component("backupJob")
@Lazy(false)
public class BackupJob {
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Scheduled(cron = "${job.backup.cron}")
	public void backup() {
		String filePath = "D:\\backup\\" + System.currentTimeMillis() + ".sql";
		String cmd = "d:/Program  Files/MySQL/MySQL Server 5.5/bin/";
		String ip = "127.0.0.1";
		String username = "root";
		String password = "I_Gomall20085121428";
		String databaseName = "igomall";

		BackUpMySQL.backup(cmd, ip, username, password, databaseName, filePath,"utf8");

		List<String> attachments = new ArrayList<String>();
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		filePath = file.getAbsolutePath().replace("\\", "\\\\");
		attachments.add(filePath);
		Setting setting = SettingUtils.get();
		mailService.sendBackupMail(setting.getSmtpFromMail(), setting.getSmtpHost(), setting.getSmtpPort(), setting.getSmtpUsername(), setting.getSmtpPassword(), "431820031@qq.com", attachments);
		
	}
	
	public static void main(String[] args) {
		new BackupJob().backup();
		System.out.println("===================");
	}

}