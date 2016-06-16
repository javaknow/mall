
package com.igomall.wechat.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Pageable;
import com.igomall.entity.OrderItem;
import com.igomall.entity.Product;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.BrandService;
import com.igomall.service.CartService;
import com.igomall.service.MemberService;
import com.igomall.service.OrderItemService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.SearchService;
import com.igomall.service.TagService;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.WechatShare;

@Controller("wechatProductController")
@RequestMapping("/wechat/product")
public class ProductController extends BaseController {
	
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	
	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;
	
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(ModelMap model, String id,HttpServletRequest request,Long orderItemId) throws Exception {
		StringBuffer parameter = new StringBuffer();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null) {
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String parameterName = entry.getKey();
				if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
					String[] parameterValues = entry.getValue();
					if (parameterValues != null) {
						for (String parameterValue : parameterValues) {
							if(parameter.length()==0){
								parameter.append(parameterName + "=" + parameterValue);
							}else{
								parameter.append("&"+parameterName + "=" + parameterValue);
							}
							
						}
					}
				}
			}
		}
		String[] strs = null;
		if(orderItemId!=null){
			OrderItem orderItem = orderItemService.find(orderItemId);
			model.addAttribute("product", orderItem.getProduct());
		}else{
			strs = id.split("_");
			model.addAttribute("product", productService.find(Long.parseLong(strs[0])));
		}
		
		model.addAttribute("member", memberService.getCurrent());
		model.addAttribute("cart",cartService.getCurrent());
		
		//当前产品的连接

		String url= request.getRequestURL()+"?"+parameter;
		model.addAllAttributes(WechatShare.sign(url));
		model.addAttribute("appId", CommonWeChatAttributes.APPID);
		model.addAttribute("url", url);
		if (strs!=null&&strs.length==3) {
			model.addAttribute("key", strs[2]);
			model.addAttribute("result", strs[1]);
		}else{
			model.addAttribute("key", "");
			model.addAttribute("result", "");
		}
		return "wechat/promotion/detail";
	}
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(){
		return "wechat/product/list";
	}
	
	@RequestMapping(value = "/getDate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getDate(ModelMap model, Long productCategoryId) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		data.put("datas", productService.findList(productCategoryService.find(productCategoryId), null, null, null, null));
		return data;
	}
	
	
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(ModelMap model,Long[] ids) {
		model.addAttribute("products", productService.findList(ids));
		return "wechat/member/history/list";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, ModelMap model) {
		System.out.println(keyword);
		if (StringUtils.isEmpty(keyword)) {
			return ERROR_VIEW;
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("productKeyword", keyword);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
model.addAttribute("page", searchService.search(keyword, startPrice, endPrice, orderType, pageable));
		//model.addAttribute("page", productService.findPage(pageable));
		return "wechat/product/search";
	}
	
	@RequestMapping(value = "/product_introduce", method = RequestMethod.GET)
	public String product_introduce(ModelMap model, Long id) throws Exception {
		Product product = productService.find(id);
		model.addAttribute("product", product);
		return "wechat/product/product_introduce";
	}
	
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public 
	@ResponseBody
	List<Product> data(ModelMap model, Long promotionId,Integer currentPage) throws Exception {
		List<Product> products = productService.findList(promotionService.find(promotionId), (currentPage-1)*8, 8,true);
		
		return products;
	}
}