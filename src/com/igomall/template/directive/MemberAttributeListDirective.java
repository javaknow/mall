/*
 * 
 * 
 * 
 */
package com.igomall.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.igomall.entity.MemberAttribute;
import com.igomall.service.MemberAttributeService;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("memberAttributeListDirective")
public class MemberAttributeListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "memberAttributes";

	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<MemberAttribute> memberAttributes;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		if (useCache) {
			memberAttributes = memberAttributeService.findList(cacheRegion);
		} else {
			memberAttributes = memberAttributeService.findList();
		}
		setLocalVariable(VARIABLE_NAME, memberAttributes, env, body);
	}

}