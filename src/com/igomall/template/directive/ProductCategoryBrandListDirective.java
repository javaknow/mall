
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
import com.igomall.util.FreemarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productCategoryBrandListDirective")
public class ProductCategoryBrandListDirective extends BaseDirective {

	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	private static final String VARIABLE_NAME = "brands";

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long productCategoryId = FreemarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Brand> brands;
		List<ProductCategory> productCategories;
		if (productCategoryId != null && productCategory == null) {
			brands = new ArrayList<Brand>();
			productCategories = new ArrayList<ProductCategory>();
		} else {
			boolean useCache = useCache(env, params);
			String cacheRegion = getCacheRegion(env, params);
			Integer count = getCount(params);
			if (useCache) {
				productCategories = productCategoryService.findChildren(productCategory, true);
				productCategories.add(productCategory);
				brands = brandService.findList(productCategories, count, cacheRegion);
			} else {
				productCategories = productCategoryService.findChildren(productCategory, true);
				productCategories.add(productCategory);
				brands = brandService.findList(productCategories, count);
			}
		}
		
		setLocalVariable(VARIABLE_NAME, brands, env, body);
	}

}