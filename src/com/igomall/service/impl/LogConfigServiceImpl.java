/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.igomall.CommonAttributes;
import com.igomall.LogConfig;
import com.igomall.service.LogConfigService;

@Service("logConfigServiceImpl")
public class LogConfigServiceImpl implements LogConfigService {

	@SuppressWarnings("unchecked")
	@Cacheable("logConfig")
	public List<LogConfig> getAll() {
		try {
			//File igomallXmlFile = new ClassPathResource(CommonAttributes.LX_XML_PATH).getFile();
			File igomallXmlFile = new File(CommonAttributes.LX_XML_PATH);
			Document document = new SAXReader().read(igomallXmlFile);
			List<org.dom4j.Element> elements = document.selectNodes("/lx/logConfig");
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			for (org.dom4j.Element element : elements) {
				String operation = element.attributeValue("operation");
				String urlPattern = element.attributeValue("urlPattern");
				LogConfig logConfig = new LogConfig();
				logConfig.setOperation(operation);
				logConfig.setUrlPattern(urlPattern);
				logConfigs.add(logConfig);
			}
			return logConfigs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}