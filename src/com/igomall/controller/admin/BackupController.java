package com.igomall.controller.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.igomall.Message;
import com.igomall.Setting;
import com.igomall.service.BonusCouponService;
import com.igomall.service.MailService;
import com.igomall.service.TemplateService;
import com.igomall.util.BackUpMySQL;
import com.igomall.util.SettingUtils;



@Controller("adminBackupController")
@RequestMapping("/admin/backup")
public class BackupController extends BaseController {

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;
	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;
	
	private static Map<String, String> map = new HashMap<String, String>();

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) throws FileNotFoundException,
			IOException {
		String filePath = "D:\\backup\\";

		map = readfile(filePath);
		model.addAttribute("map", map);
		return "/admin/backup/list";
	}

	public static Map<String, String> readfile(String filepath) {
		Map<String, String> map = new HashMap<String, String>();
		File file = new File(filepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] tempList = file.listFiles();
		if (tempList != null && tempList.length > 0) {
			for (int i = 0; i < tempList.length; i++) {
				if (tempList[i].isFile()) {
					map.put(tempList[i].getName(),
							tempList[i].getAbsolutePath());
				}
				if (tempList[i].isDirectory()) {
				}
			}
		}

		return map;
	}

	@RequestMapping(value = "/backup", method = RequestMethod.POST)
	public @ResponseBody
	Message backup() {
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
		return Message.success("备份成功！");
	}

	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody
	Message restore(String fileName) {
		String cmd = "d:/Program Files/MySQL/MySQL Server 5.5/bin/";
		String ip = "127.0.0.1:6540";
		String username = "root";
		String password = "I_Gomall20085121428";
		String databaseName = "igomall";
		BackUpMySQL.load(cmd,ip,username,password,databaseName,fileName,"utf8");
		return Message.success("还原成功！");
	}
	
	@RequestMapping(value = "/deliveryButton", method = RequestMethod.POST)
	public @ResponseBody
	Message deliveryButton(String fileName) {
		bonusCouponService.deveryBonusCoupns();
		return Message.success("分红成功！");
	}
	
}