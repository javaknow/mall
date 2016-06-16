package com.igomall.wechat.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Order;
import com.igomall.Principal;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Cart;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.Product.OrderType;
import com.igomall.entity.Shop;
import com.igomall.entity.WeChatMember;
import com.igomall.service.AreaService;
import com.igomall.service.BonusCouponService;
import com.igomall.service.CartService;
import com.igomall.service.ConsultationService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.DepositService;
import com.igomall.service.GoodsService;
import com.igomall.service.InComeService;
import com.igomall.service.MemberService;
import com.igomall.service.MessageService;
import com.igomall.service.OrderService;
import com.igomall.service.ProductNotifyService;
import com.igomall.service.ProductService;
import com.igomall.service.ReceiverService;
import com.igomall.service.ReviewService;
import com.igomall.service.ShopService;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.WebUtils;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.entity.AccessTokenTemp;
import com.igomall.wechat.entity.JsonEntity;
import com.igomall.wechat.util.GetAccessTokenUtil;

@Controller("wechatMemberController")
@RequestMapping("/wechat/member")
public class MemberController extends BaseController {
	private static final int NEW_ORDER_COUNT = 6;
	@Resource(name = "weChatMemberServiceImpl")
	private WeChatMemberService weChatMemberService;
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
	@Resource(name = "inComeServiceImpl")
	private InComeService inComeService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 处理用户信息
		HttpSession session = request.getSession();
		String code = request.getParameter("code");

		// 初始化信息
		Member member = null;
		WeChatMember weChatMember = null;

		if (code == null || "".equals(code)) {// 不是通过微信链接进来的
			weChatMember = weChatMemberService.getCurrent();
			member = memberService.getCurrent();
			if (weChatMember == null && member == null) {
				return "redirect:/wechat/login.jhtml";
			} else {
				if (weChatMember != null && member == null) {
					member = weChatMember.getMember();
					if (member == null) {
						member = memberService.createMember(weChatMember);
						weChatMember.setMember(member);
						weChatMemberService.update(weChatMember);
					}
					
					//微信信息同步
					com.igomall.wechat.entity.WeChatMember weChatMember1 = GetAccessTokenUtil.getMemberInfo(weChatMember.getOpenid());
					weChatMember.copyProperties(weChatMember1);
				} else {// 这种情况是没有微信账号 只有member对象

				}
			}
		} else {
			AccessTokenTemp accessTokenTemp = GetAccessTokenUtil.getAccessToken1(CommonWeChatAttributes.APPID,CommonWeChatAttributes.APPSECRET, code);
			String openId = accessTokenTemp.getOpenid();

			weChatMember = weChatMemberService.findByOpenId(openId);

			if (weChatMember == null && openId != null && !"".equals(openId)) {// 系统中不存在该账号，就新建一个
				Member parent = memberService.find(337L);
				weChatMember = weChatMemberService.create(openId, parent);
			}

			member = weChatMember.getMember();
			//微信信息同步
			com.igomall.wechat.entity.WeChatMember weChatMember1 = GetAccessTokenUtil.getMemberInfo(weChatMember.getOpenid());
			weChatMember.copyProperties(weChatMember1);
			
		}

		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
		}

		addCookie(request, response, session, member,weChatMember);

		model.addAttribute("weChatMember", weChatMember);
		model.addAttribute("member", member);

		model.addAttribute("title", "我的商城");
		model.addAttribute("products", getFiverateProduct(24, member));
		model.addAttribute("waitingPaymentOrderCount",orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount",orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount",couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null,null, null, null, null, null));
		model.addAttribute("productNotifyCount",productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount",reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount",consultationService.count(member, null, null));
		model.addAttribute("newOrders",orderService.findList(member, NEW_ORDER_COUNT, null, null));
		model = setInfo(member, model);
		return "wechat/member/index";

	}

	public void addCookie(HttpServletRequest request,HttpServletResponse response, HttpSession session, Member member,WeChatMember weChatMember) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME,member.getUsername());
		
		if(weChatMember!=null){
			session.setAttribute(WeChatMember.PRINCIPAL_ATTRIBUTE_NAME, new Principal(weChatMember.getId(), weChatMember.getOpenid()));
			WebUtils.addCookie(request, response, WeChatMember.OPEN_COOKIE_NAME, weChatMember.getOpenid());
		}
	}

	public ModelMap setInfo(Member member, ModelMap model) {
		Shop shop = shopService.find(1L);
		model.addAttribute("waitingPaymentOrderCount",orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount",orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount",couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null,null, null, null, null, null));
		model.addAttribute("productNotifyCount",productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount",reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount",consultationService.count(member, null, null));
		model.addAttribute("newOrders",orderService.findList(member, NEW_ORDER_COUNT, null, null));
		model.addAttribute("bonusCouponCount", bonusCouponService.count(member,false, false, shop, com.igomall.entity.BonusCoupon.Type.member,false));

		return model;
	}

	/**
	 * 根据搜藏的产品来查找相关分类下的几个产品
	 * 
	 * @param count
	 * @param member
	 * @return
	 */
	public List<Product> getFiverateProduct(Integer count, Member member) {
		List<Order> orders = new ArrayList<Order>();
		// 搜藏的产品
		Set<Product> fiverateProducts = member.getFavoriteProducts();
		List<Product> list = new ArrayList<Product>();
		// 没有搜藏的产品。就按照最新时间查询前面的产品
		if (fiverateProducts == null || fiverateProducts.size() == 0) {
			list = productService.findList(null, null, null, null, null, null,null, true, null, null, null, null, null,OrderType.salesDesc, count, null, orders, true);
		} else {
			for (Product product : fiverateProducts) {
				List<Product> products = productService.findList(product.getProductCategory(), null, null, null, null,null, null, true, null, null, null, null, null,OrderType.salesDesc, count, null, orders, true);
				list.addAll(products);
			}
		}

		if (list.size() < count) {
			List<Product> products = productService.findList(null, null, null,null, null, null, null, true, null, null, null, null, null,OrderType.salesDesc, count - list.size(), null, orders,true);
			list.addAll(products);
		}

		return list;
	}

	/**
	 * 浏览历史纪录
	 * 
	 * @param model
	 * @param memberId
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/history1", method = RequestMethod.GET)
	public @ResponseBody
	List<Product> history1(Long[] ids) {
		return productService.findList(ids);
	}

	@RequestMapping(value = "/center", method = RequestMethod.GET)
	public String center(ModelMap model, Long memberId) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "wechat/member/center";
	}

	@RequestMapping(value = "/myPeople1", method = RequestMethod.POST)
	public @ResponseBody
	List<JsonEntity> myPeople1(ModelMap model, Long id) {
		// [{ id:'031', name:'n3.n1', isParent:true},{ id:'032', name:'n3.n2',
		// isParent:false},{ id:'033', name:'n3.n3', isParent:true},{ id:'034',
		// name:'n3.n4', isParent:false}]
		List<JsonEntity> list = new ArrayList<JsonEntity>();
		if (id == null) {
			Member member = memberService.getCurrent();
			Set<Member> children = member.getChildren();
			if (children != null && children.size() > 0) {
				for (Member member2 : children) {
					JsonEntity jsonEntity = new JsonEntity();
					jsonEntity.setId(member2.getId());
					jsonEntity.setName(member2.getUsername());
					jsonEntity.setIsParent(member2.getChildren().size() > 0 ? true: false);
					list.add(jsonEntity);
				}
			}
		} else {
			Member member = memberService.find(id);
			Set<Member> children = member.getChildren();
			if (children != null && children.size() > 0) {
				for (Member member2 : children) {
					JsonEntity jsonEntity = new JsonEntity();
					jsonEntity.setId(member2.getId());
					jsonEntity.setName(member2.getUsername());
					jsonEntity.setIsParent(member2.getChildren().size() > 0 ? true: false);
					list.add(jsonEntity);
				}
			}
		}

		return list;
	}

	@RequestMapping(value = "/myPeople", method = RequestMethod.GET)
	public String myPeople(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/wechat/member/myPeople";
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(ModelMap model, HttpServletRequest request, Long[] ids) {
		String ids1 = WebUtils.getCookie(request, "historyProduct");

		System.out.println(ids1);

		model.addAttribute("products", productService.findList(ids));
		return "wechat/member/history/list";
	}

	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String feedback(ModelMap model) {

		return "/wechat/member/feedback";
	}
}