
package com.igomall.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.service.CartService;
import com.igomall.service.ConsultationService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.MemberService;
import com.igomall.service.MessageService;
import com.igomall.service.OrderService;
import com.igomall.service.ProductNotifyService;
import com.igomall.service.ProductService;
import com.igomall.service.ReviewService;
import com.igomall.util.WebUtils;

@Controller("ajaxIndexController")
@RequestMapping("/ajax")
public class AjaxController extends BaseController {

	private static final int NEW_ORDER_COUNT = 6;
	
	@Resource(name="memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;
	@Resource(name="cartServiceImpl")
	private CartService cartService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> index(ModelMap model,String act,String op,HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		if("index".equals(act)&&"viewed_info".equals(op)){
			data = getViewInfo(request);
		}else if("cart".equals(act)&&"ajax_load".equals(op)){//购物车信息
			data.put("cart", cartService.getCurrent());
		}
		
		
		
		
		
		
		return data;
	}

	public Map<String,Object> getViewInfo(HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		List<Product> products = new ArrayList<Product>();
		String historyProduct = WebUtils.getCookie(request,"historyProduct");
		Member member = memberService.getCurrent();
		if(member!=null){
			data.put("member", member);
			data.put("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
			data.put("waitingShippingOrderCount", orderService.waitingShippingCount(member));
			data.put("messageCount", messageService.count(member, false));
			data.put("couponCodeCount", couponCodeService.count(null, member, null, false, false));
			data.put("favoriteCount", productService.count(member, null, null, null, null, null, null));
			data.put("productNotifyCount", productNotifyService.count(member, null, null, null));
			data.put("reviewCount", reviewService.count(member, null, null, null));
			data.put("consultationCount", consultationService.count(member, null, null));
			data.put("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		}
		if(historyProduct==null){
			historyProduct="";
		}
		String[] historyProductIds = historyProduct.split(",");
		for (String id : historyProductIds) {
			try {
				Product product = productService.find(Long.parseLong(id));
				if(product!=null){
					products.add(product);
				}
			} catch (NumberFormatException e) {
				
			}
		}
		data.put("products", products);
		
		
		return data;
	}
	
}