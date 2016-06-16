
package com.igomall.controller.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.entity.Cart;
import com.igomall.entity.Member;
import com.igomall.service.CartService;
import com.igomall.service.ConsultationService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.MemberService;
import com.igomall.service.MessageService;
import com.igomall.service.OrderService;
import com.igomall.service.ProductNotifyService;
import com.igomall.service.ProductService;
import com.igomall.service.ReviewService;

@Controller("shopIndexController")
@RequestMapping("/")
public class IndexController extends BaseController {
	
	private static final int NEW_ORDER_COUNT = 6;
	
	@Resource(name="memberServiceImpl")
	private MemberService memberService;
	@Resource(name="cartServiceImpl")
	private CartService cartService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		if(member==null){
			model.addAttribute("cart",cartService.getCurrent());
		}else{
			model.addAttribute("cart",member.getCart());
		}
		model.addAttribute("member",member);
		return "/shop/index";
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public String info(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(member, null, null));
		model.addAttribute("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		return "/shop/index/info";
	}
	
	@RequestMapping(value = "/info1", method = RequestMethod.POST)
	public String info1(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(member, null, null));
		model.addAttribute("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		
		return "/shop/index/info1";
	}
	
	@RequestMapping(value = "/userInfo", method = RequestMethod.POST)
	public String userInfo(ModelMap model,String pageType) {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/index/"+pageType;
	}
	
	@RequestMapping(value = "/checkMember", method = RequestMethod.POST)
	public @ResponseBody
	Member checkMember() {
		Member member = memberService.getCurrent();
		return member;
	}
	
	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	public String cart(ModelMap model) {
		Cart cart = cartService.getCurrent();
		model.addAttribute("cart", cart);
		return "/shop/index/cart2";
	}
	
	@RequestMapping(value = "/getCartInfo", method = RequestMethod.POST)
	public String getCartInfo(ModelMap model) {
		Cart cart = cartService.getCurrent();
		model.addAttribute("cart", cart);
		return "/shop/index/cart3";
	}
	
	/**************************************************************************************************************/
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
	public String index1(ModelMap model) {
		return "/shop/index1";
	}
	@RequestMapping(value = "/cartInfo", method = RequestMethod.GET)
	public String cartInfo(ModelMap model) {
		Cart cart = cartService.getCurrent();
		model.addAttribute("cart", cart);
		return "/shop/index2/cartInfo";
	}
	
	@RequestMapping(value = "/info2", method = RequestMethod.POST)
	public String info2(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(member, null, null));
		model.addAttribute("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		return "/shop/index2/info";
	}
	
	@RequestMapping(value = "/cartInfo2", method = RequestMethod.POST)
	public Map<String,Object> cartInfo2(ModelMap model) {
		Map<String,Object> data = new HashMap<String,Object>();
		Cart cart = cartService.getCurrent();
		data.put("cart", cart);
		data.put("list", cart.getCartItems());
		return data;
	}
	
	/**************************************************************************************************************/
}