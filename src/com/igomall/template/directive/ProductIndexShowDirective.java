
package com.igomall.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.igomall.Order;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;
import com.igomall.util.FreemarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productIndexShowDirective")
public class ProductIndexShowDirective extends BaseDirective {

	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	private static final String VARIABLE_NAME = "products";
	
	private static final String ORDER_TYPE_PARAMETER_NAME = "orderType";

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long productCategoryId = FreemarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		OrderType orderType = FreemarkerUtils.getParameter(ORDER_TYPE_PARAMETER_NAME, OrderType.class, params);
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Product> products = null;
		
		if ((productCategoryId != null && productCategory == null)) {
			products = new ArrayList<Product>();
		} else {
			boolean useCache = useCache(env, params);
			String cacheRegion = getCacheRegion(env, params);
			Integer count = getCount(params);
			List<Order> orders = getOrders(params);
			
			if (useCache) {
				products = productService.findList(productCategory, null, null, null, null, null, null, true, true, null, false, null, null, orderType, count, null, orders, cacheRegion);
			} else {
				products = productService.findList(productCategory, null, null, null, null, null, null, true, true, null, false, null, null, orderType, count, null, orders);
			}
		}
		setLocalVariable(VARIABLE_NAME, products, env, body);
	}

}