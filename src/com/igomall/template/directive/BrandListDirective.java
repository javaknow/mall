
package com.igomall.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.igomall.entity.Brand;
import com.igomall.entity.ProductCategory;
import com.igomall.service.BrandService;
import com.igomall.service.ProductCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("brandListDirective")
public class BrandListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "brands";

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Brand> brands;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		Integer count = getCount(params);
		Long productCategoryId = getId(params);
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<ProductCategory> productCategories;
		if (productCategoryId != null && productCategory == null) {
			brands = new ArrayList<Brand>();
			productCategories = new ArrayList<ProductCategory>();
		} else {
			productCategories = productCategoryService.findChildren(productCategory, true);
			productCategories.add(productCategory);
			if (useCache) {
				brands = brandService.findList(productCategories, count, cacheRegion);
			} else {
				brands = brandService.findList(productCategories, count);
			}
		}
		
		setLocalVariable(VARIABLE_NAME, brands, env, body);
	}

}