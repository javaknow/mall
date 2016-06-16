package com.igomall.wechat.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.Principal;
import com.igomall.ResourceNotFoundException;
import com.igomall.entity.Cart;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.Product.OrderType;
import com.igomall.entity.Promotion;
import com.igomall.entity.WeChatMember;
import com.igomall.service.AdPositionService;
import com.igomall.service.AdService;
import com.igomall.service.ArticleService;
import com.igomall.service.BrandService;
import com.igomall.service.CartService;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.WebUtils;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.entity.AccessTokenTemp;
import com.igomall.wechat.util.GetAccessTokenUtil;

/**
 * 微信商城的入口
 * @author blackboy
 *
 */
@Controller("wechatIndexController")
@RequestMapping("/wechat/index")
public class IndexController extends BaseController {
	@Resource(name = "weChatMemberServiceImpl")
	private WeChatMemberService weChatMemberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "adServiceImpl")
	private AdService adService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	/**
	 * 这里是微信菜单的所有入口
	 * @param model
	 * @param request
	 * @param response
	 * @param method
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response, String method, Long id) throws Exception {
		// 处理用户信息
		HttpSession session = request.getSession();
		
		/**
		 * 通过这个code可以获取到用户的openid。所以如果发现code是空的 一律跳转到报错页面。
		 * 可以简化微信的流程
		 */
		String code = request.getParameter("code");
System.out.println("code:"+code);		
		//如果这里的code是null的那么就是非法路径
		if(code==null){
			return WAP_ERROR_VIEW;
		}

		//跟据code获取当前访问者的openid
		AccessTokenTemp accessTokenTemp = GetAccessTokenUtil.getAccessToken1(CommonWeChatAttributes.APPID,CommonWeChatAttributes.APPSECRET, code);
		String openId = accessTokenTemp.getOpenid();
System.out.println("openId:"+openId);		
		Member member = null;
		WeChatMember weChatMember = weChatMemberService.findByOpenId(openId);
		//如果weChatMember是null的那么就是关注的用户还没在数据库里面，这是需要在数据库里面创建一条数据
		if(weChatMember==null){
			weChatMember = weChatMemberService.create(openId, null);
			member = weChatMember.getMember();
		}else{
			member = weChatMember.getMember();
			if(member==null){
				member = memberService.createMember(weChatMember);
			}
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
		
		return getData(model,method,id);
	}

	
	public String getData(ModelMap model,String method,Long id) throws Exception{
		String result = "";
		if ("content".equals(method)) {// 文章
			model.addAttribute("article", articleService.find(id));
			result = "/wap/article/content";
		} else if ("hotProduct".equals(method)) {// 热门产品
			Pageable pageable = new Pageable(1, 10);
			model.addAttribute("products", productService.findPage(null, null,null, null, null, null, null, true, null, null, null, null,null, OrderType.salesDesc, pageable, true));
			model.addAttribute("title", "热门产品");
			result = "/wap/doublePoint/index";
		} else if ("newProduct".equals(method)) {// 最新产品
			Pageable pageable = new Pageable(1, 10);
			model.addAttribute("products", productService.findPage(null, null,null, null, null, null, null, true, null, null, null, null,null, OrderType.dateDesc, pageable, true));
			model.addAttribute("title", "热门产品");
			result = "/wechat/product/index";
		} else if ("doublePoint".equals(method)) {// 双倍积分
			Promotion promotion = promotionService.find(1L);
			if (promotion == null) {
				throw new ResourceNotFoundException();
			}
			List<Product> products = productService.findList(promotion, 0, 9,true);

			model.addAttribute("products", products);
			model.addAttribute("promotion", promotion);
			return "/wap/promotion/index";
		} else if ("promotionProduct".equals(method)) {// 促销产品
			result = "/wap/promotion/list";
		} else if ("memberInfo".equals(method)) {// 促销产品

			result = "/wap/member/index";
		} else {//首页
			result = "/wap/index";
		}
		
		return result;
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
}