
package com.igomall.listener;

import java.io.File;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.igomall.service.CacheService;
import com.igomall.service.PluginService;
import com.igomall.service.ProductService;
import com.igomall.service.SearchService;
import com.igomall.service.StaticService;
import com.igomall.wechat.TokenThread;

@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

	private static final String INSTALL_INIT_CONFIG_FILE_PATH = "/install_init.conf";

	private static final Logger logger = Logger.getLogger(InitListener.class.getName());

	private ServletContext servletContext;

	@Value("${system.version}")
	private String systemVersion;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (servletContext != null && contextRefreshedEvent.getApplicationContext().getParent() == null) {
			String info = "I|n|i|t|i|a|l|i|z|i|n|g| |I|-|G|O|M|A|L|L| |" + systemVersion;
			logger.info(info.replace("|", ""));
			File installInitConfigFile = new File(servletContext.getRealPath(INSTALL_INIT_CONFIG_FILE_PATH));
			if (installInitConfigFile.exists()) {
				cacheService.clear();
				staticService.buildAll();
				searchService.purge();
				searchService.index();
				installInitConfigFile.delete();
			} else {
				staticService.buildIndex();
				staticService.buildOther();
			}
		}
		
		new Thread(new TokenThread()).start();
		
	}

}