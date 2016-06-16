
package com.igomall.controller.shop.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.Setting.SignInType;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.InCome;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;
import com.igomall.entity.InCome.Type;
import com.igomall.service.BonusCouponService;
import com.igomall.service.CartService;
import com.igomall.service.ConsultationService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.InComeService;
import com.igomall.service.MemberService;
import com.igomall.service.MessageService;
import com.igomall.service.OrderService;
import com.igomall.service.ProductNotifyService;
import com.igomall.service.ProductService;
import com.igomall.service.ReviewService;
import com.igomall.service.ShopService;
import com.igomall.util.ChangeDate;
import com.igomall.util.SettingUtils;

@Controller("shopMemberController")
@RequestMapping("/member")
public class MemberController extends BaseController {

	private static final int NEW_ORDER_COUNT = 6;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
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
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Shop shop = shopService.find(1L);
		model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(member, null, null));
		model.addAttribute("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		model.addAttribute("bonusCouponCount",bonusCouponService.count(member,false,false,shop,com.igomall.entity.BonusCoupon.Type.member,false));
		return "shop/member/index";
	}
	
	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	public @ResponseBody
	Message signIn() {
		Member member = memberService.getCurrent();

		if (member == null) {
			return ERROR_MESSAGE;
		}

		Setting setting = SettingUtils.get();
		SignInType signInType = setting.getSignInType();
		if (setting.getIsSignIn()) {
			DateTime now = new DateTime();
			DateTime date = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);

			Date beginDate = date.toDate();
			date = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 23, 59, 59);
			Date endDate = date.toDate();
			
			//当天的签到记录
			List<InCome> list = inComeService.findInCome(member,Type.signIn,beginDate, endDate);
			if(list==null||list.size()==0){//当天没有签到
				
			}else{//当天已经签到了
				return Message.error("今日已经签到，请明天再来！");
			}
			//昨天的签到记录
			now = ChangeDate.getPrevDay(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1);
			date = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);

			beginDate = date.toDate();
			date = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 23, 59, 59);
			endDate = date.toDate();
			list = inComeService.findInCome(member,Type.signIn,beginDate, endDate);
			if(list==null||list.size()==0){//昨天没有签到
				member.setSignInDay(1);
			}else{
				member.setSignInDay(member.getSignInDay()+1);
			}
			
			String memo="签到领取积分。";
			InCome inCome = new InCome();
			inCome.setBalance(setting.getSignInMoney().add(setting.getSignInEveryMoney().multiply(new BigDecimal(member.getSignInDay()-1))));
			if(setting.getSignInMaxMoney().compareTo(setting.getZero())>0&&inCome.getBalance().compareTo(setting.getSignInMaxMoney())>0){
				inCome.setBalance(setting.getSignInMaxMoney());
			}
			
			if(signInType==SignInType.balance){
				memo = memo+"领取之前[储值账户:"+member.getBalance().setScale(2)+",积分账户："+member.getPoint()+"];";
				member.setBalance(member.getBalance().add(inCome.getBalance()));
				memo = memo+"领取之后[储值账户:"+member.getBalance().setScale(2)+",积分账户："+member.getPoint()+"];";
			}else if(signInType==SignInType.point){
				memo = memo+"领取之前[储值账户:"+member.getBalance().setScale(2)+",积分账户："+member.getPoint()+"];";
				member.setPoint(member.getPoint()+inCome.getBalance().longValue());
				memo = memo+"领取之后[储值账户:"+member.getBalance().setScale(2)+",积分账户："+member.getPoint()+"];";
			}
			
			
			inCome.setMember(member);
			inCome.setMemo(memo);
			inCome.setType(Type.signIn);
			inComeService.save(inCome);
			
		}
		
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/myPeople", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Member member =memberService.getCurrent();
		model.addAttribute("page", memberService.findChildrenPage(member,pageable));
		
		return "shop/member/myPeople";
	}
	
	@RequestMapping(value = "/getJson", method = RequestMethod.GET)
	public String getJson(ModelMap model,String act,String op) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		if(act!=null&&"member".equals("member")){
			if(op!=null){
				if("ajax_load_member_info".equals(op)){//加载用户的基本信息
					return "shop/member/ajax/index/memberInfo";
				}else if("ajax_load_order_info".equals(op)){//加载订单和购物车信息
					model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
					model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
					model.addAttribute("messageCount", messageService.count(member, false));
					model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
					model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
					model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
					model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
					model.addAttribute("consultationCount", consultationService.count(member, null, null));
					model.addAttribute("newOrders", orderService.findList(member, 3, null, null));
					model.addAttribute("cart", cartService.getCurrent());
					return "shop/member/ajax/index/orderInfo";
				}else if("ajax_load_goods_info".equals(op)){//加载商品和店铺收藏
					//商品收藏
					model.addAttribute("favoriteProducts", productService.findList(member));
					//店铺收藏
					model.addAttribute("favoriteShops",null);
					return "shop/member/ajax/index/goodsInfo";
				}else if("ajax_load_sns_info".equals(op)){//圈子信息
					return "shop/member/ajax/index/snsInfo";
				}
			}
		}
		return "shop/member/ajax/index/memberInfo";
	}
}