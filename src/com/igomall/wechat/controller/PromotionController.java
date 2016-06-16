package com.igomall.wechat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.ResourceNotFoundException;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.Promotion;
import com.igomall.service.CartService;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.util.DesUtils;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.WechatShare;

@Controller("wechatPromotionController")
@RequestMapping("/wechat/promotion")
public class PromotionController extends BaseController {
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;
	
	
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
	public String index(@PathVariable Long id, ModelMap model) throws Exception {

		Promotion promotion = promotionService.find(id);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		Member member = memberService.getCurrent();
		if(member!=null){
			model.addAllAttributes(DesUtils.encrypt2(member.getId().toString()));
		     model.addAttribute("isLogin", true);
		}
		
		List<Product> products = productService.findList(promotion, 0, 8,true);
		model.addAttribute("products",products);
		model.addAttribute("promotion", promotion);
		return "/wechat/promotion/index";
	}


	
	@RequestMapping(value = "/index/promotionMore", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> promotionMore(Long promotionId, Integer page, ModelMap model) {

		Promotion promotion = promotionService.find(promotionId);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		List<Product> products = new ArrayList<Product>();
		products = productService.findList(promotion, (page-1)*20, 20);
		return products;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) throws Exception {
		return "/wechat/promotion/list";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(ModelMap model, String id,HttpServletRequest request) throws Exception {
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
		
		
		String[] strs = id.split("_");
		model.addAttribute("product", productService.find(Long.parseLong(strs[0])));
		model.addAttribute("member", memberService.getCurrent());
		model.addAttribute("cart",cartService.getCurrent());
		
		//当前产品的连接

		String url= request.getRequestURL()+"?"+parameter;
System.out.println("url:"+url);	
		model.addAllAttributes(WechatShare.sign(url));
		model.addAttribute("appId", CommonWeChatAttributes.APPID);
		model.addAttribute("url", url);
		if (strs.length==3) {
			model.addAttribute("key", strs[2]);
			model.addAttribute("result", strs[1]);
		}else{
			model.addAttribute("key", "");
			model.addAttribute("result", "");
		}
		return "wechat/promotion/detail";
	}
	
	/**
	 * 促销产品的查看详情
	 * @param model
	 * @param id 产品的id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product_introduce", method = RequestMethod.GET)
	public String product_introduce(ModelMap model, Long id) throws Exception {
		return "wechat/promotion/product_introduce";
	}
	

}