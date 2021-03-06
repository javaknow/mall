/*
 * 
 * 
 * 
 */
package com.igomall.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import com.igomall.entity.Seo;
import com.igomall.entity.Seo.Type;
import com.igomall.service.SeoService;
import com.igomall.util.FreemarkerUtils;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("seoDirective")
public class SeoDirective extends BaseDirective {

	private static final String TYPE_PARAMETER_NAME = "type";

	private static final String VARIABLE_NAME = "seo";

	@Resource(name = "seoServiceImpl")
	private SeoService seoService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Type type = FreemarkerUtils.getParameter(TYPE_PARAMETER_NAME, Type.class, params);

		Seo seo;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		if (useCache) {
			seo = seoService.find(type, cacheRegion);
		} else {
			seo = seoService.find(type);
		}
		setLocalVariable(VARIABLE_NAME, seo, env, body);
	}

}