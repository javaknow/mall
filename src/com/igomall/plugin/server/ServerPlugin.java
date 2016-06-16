package com.igomall.plugin.server;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.igomall.FileInfo;
import com.igomall.entity.PluginConfig;
import com.igomall.plugin.StoragePlugin;

@Component("serverPlugin")
public class ServerPlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "服务器存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "";
	}

	@Override
	public String getSiteUrl() {
		return "";
	}

	@Override
	public String getInstallUrl() {
		return "server/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "server/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "server/setting.jhtml";
	}
	
	@Override
	public void upload(String path, File file, String contentType) {	
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			init(pluginConfig);
			String path1 = pluginConfig.getAttribute("path");
			File destFile = new File(path1+path);
			try {
				FileUtils.moveFile(file, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getUrl(String path) {
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			init(pluginConfig);
			String urlPrefix = pluginConfig.getAttribute("url");
			return urlPrefix + path;
		}
		return null;
	}

	@Override
	public List<FileInfo> browser(String path) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			init(pluginConfig);
			File directory = new File(pluginConfig.getAttribute("path")+path);
			String url = pluginConfig.getAttribute("url");
			if (directory.exists() && directory.isDirectory()) {
				for (File file : directory.listFiles()) {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setName(file.getName());
					fileInfo.setUrl(url + path + file.getName());
					fileInfo.setIsDirectory(file.isDirectory());
					fileInfo.setSize(file.length());
					fileInfo.setLastModified(new Date(file.lastModified()));
					fileInfos.add(fileInfo);
				}
			}
			return fileInfos;
		}
		return null;
	}
	
	public void init(PluginConfig pluginConfig){
		//pluginConfig.setAttribute("url","http://localhost:8080/FileServer");
		//pluginConfig.setAttribute("path","D:/Java/apache-tomcat-7.0.67/webapps/FileServer");
	}

}