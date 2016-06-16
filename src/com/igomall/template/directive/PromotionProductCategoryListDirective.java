
package com.igomall.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.service.PromotionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("promotionProductCategoryListDirective")
public class PromotionProductCategoryListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "productCategories";

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		
		Promotion promotion;
		boolean useCache = useCache(env, params);
		Integer count = getCount(params);
		Long promotionId = getId(params);
		promotion = promotionService.find(promotionId);
		if (useCache) {
			if(promotion==null){
				
			}else{
				
				if(count>=productCategories.size()){
					productCategories.addAll(promotion.getProductCategories());
				}else{
					Integer index=0;
					for (ProductCategory productCategory : productCategories) {
						if(index<count){
							productCategories.add(productCategory);
							index++;
						}else{
							break;
						}
					}
					
				}
			}
			
		} else {
			if(promotion==null){
			}else{
				if(count>=productCategories.size()){
					productCategories.addAll(promotion.getProductCategories());
				}else{
					Integer index=0;
					for (ProductCategory productCategory : productCategories) {
						if(index<count){
							productCategories.add(productCategory);
							index++;
						}else{
							break;
						}
					}
					
				}
			}
		}
		setLocalVariable(VARIABLE_NAME, productCategories, env, body);
	}

}