/*
 * 
 * 
 * 
 */
package com.igomall.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.igomall.entity.ArticleCategory;
import com.igomall.service.ArticleCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("articleCategoryRootListDirective")
public class ArticleCategoryRootListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "articleCategories";

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<ArticleCategory> articleCategories = new ArrayList<ArticleCategory>();
		ArticleCategory articleCategory=null;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		Integer count = getCount(params);
		Long id = getId(params);
		
		if(id!=null){
			articleCategory = articleCategoryService.find(id);
			if(articleCategory!=null){
				articleCategories.add(articleCategory);
			}else{
				if (useCache) {
					articleCategories = articleCategoryService.findRoots(count, cacheRegion);
				} else {
					articleCategories = articleCategoryService.findRoots(count);
				}
			}
		}else{
			if (useCache) {
				articleCategories = articleCategoryService.findRoots(count, cacheRegion);
			} else {
				articleCategories = articleCategoryService.findRoots(count);
			}
		}
		setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
	}

}