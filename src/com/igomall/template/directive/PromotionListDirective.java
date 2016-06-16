
package com.igomall.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Promotion;
import com.igomall.service.PromotionService;
import com.igomall.util.FreemarkerUtils;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("promotionListDirective")
public class PromotionListDirective extends BaseDirective {

	private static final String HAS_BEGUN_PARAMETER_NAME = "hasBegun";

	private static final String HAS_ENDED_PARAMETER_NAME = "hasEnded";

	private static final String VARIABLE_NAME = "promotions";

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Boolean hasBegun = FreemarkerUtils.getParameter(HAS_BEGUN_PARAMETER_NAME, Boolean.class, params);
		Boolean hasEnded = FreemarkerUtils.getParameter(HAS_ENDED_PARAMETER_NAME, Boolean.class, params);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion;
		boolean useCache = useCache(env, params);
		String cacheRegion = getCacheRegion(env, params);
		Integer count = getCount(params);
		Long promotionId = getId(params);
		List<Filter> filters = getFilters(params, Promotion.class);
		List<Order> orders = getOrders(params);
		promotion = promotionService.find(promotionId);
		if (useCache) {
			if(promotion==null){
				promotions = promotionService.findList(hasBegun, hasEnded, count, filters, orders, cacheRegion);
			}else{
				promotions.add(promotion);
			}
			
		} else {
			if(promotion==null){
				promotions = promotionService.findList(hasBegun, hasEnded, count, filters, orders);
			}else{
				promotions.add(promotion);
			}
		}
		setLocalVariable(VARIABLE_NAME, promotions, env, body);
	}

}