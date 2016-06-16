
package com.igomall.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.igomall.entity.Ad;
import com.igomall.entity.AdPosition;
import com.igomall.service.AdPositionService;
import com.igomall.service.AdService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("adListDirective")
public class AdListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "ads";

	@Resource(name = "adServiceImpl")
	private AdService adService;
	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Ad> ads;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		Integer count = getCount(params);
		Long adPositionId = getId(params);
		AdPosition adPosition = adPositionService.find(adPositionId);
		if (useCache) {
			ads = adService.findList(adPosition,count, null, null, null,cacheRegion);
		} else {
			ads = adService.findList(adPosition,count, null, null,null);
		}
		setLocalVariable(VARIABLE_NAME, ads, env, body);
	}

}