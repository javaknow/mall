
package com.igomall.wechat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.OrderItem;
import com.igomall.entity.Shop;
import com.igomall.service.AreaService;
import com.igomall.service.BankService;
import com.igomall.service.BonusCouponService;
import com.igomall.service.CartService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.DeliveryCorpService;
import com.igomall.service.MemberService;
import com.igomall.service.OrderService;
import com.igomall.service.PaymentMethodService;
import com.igomall.service.PluginService;
import com.igomall.service.ReceiverService;
import com.igomall.service.ShippingMethodService;
import com.igomall.service.ShippingService;
import com.igomall.service.SnService;

@Controller("wechatOrderController")
@RequestMapping("/wechat/order")
public class OrderController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model,Long memberId) {
		Member member = memberService.find(memberId);
		List<Order> orders = orderService.findList(member,null,null,null);
		//Map<Order,Map<Shop,List<OrderItem>>> map = splitByShop(orders);
		//model.addAttribute("map", map);
		model.addAttribute("orders", orders);
		return "wechat/member/order/list";
	}
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(ModelMap model,Long memberId) {
		Member member = memberService.find(memberId);
		List<Order> orders = orderService.findList(member,null,null,null);
		//Map<Order,Map<Shop,List<OrderItem>>> map = splitByShop(orders);
		//model.addAttribute("map", map);
		model.addAttribute("orders", orders);
		return "wechat/member/order/list1";
	}
	
	@RequestMapping(value = "/list2", method = RequestMethod.GET)
	public String list2(ModelMap model,Long memberId) {
		Member member = memberService.find(memberId);
		List<Order> orders = orderService.findList(member,null,null,null);
		//Map<Order,Map<Shop,List<OrderItem>>> map = splitByShop(orders);
		//model.addAttribute("map", map);
		model.addAttribute("orders", orders);
		return "wechat/member/order/list2";
	}
	
	@RequestMapping(value = "/list3", method = RequestMethod.GET)
	public String list3(ModelMap model,Long memberId) {
		Member member = memberService.find(memberId);
		List<Order> orders = orderService.findList(member,null,null,null);
		//Map<Order,Map<Shop,List<OrderItem>>> map = splitByShop(orders);
		//model.addAttribute("map", map);
		model.addAttribute("orders", orders);
		return "wechat/member/order/list3";
	}
	
	@RequestMapping(value = "/list4", method = RequestMethod.GET)
	public String list4(ModelMap model,Long memberId) {
		Member member = memberService.find(memberId);
		List<Order> orders = orderService.findList(member,null,null,null);
		//Map<Order,Map<Shop,List<OrderItem>>> map = splitByShop(orders);
		//model.addAttribute("map", map);
		model.addAttribute("orders", orders);
		return "wechat/member/order/list4";
	}
	
	/**
	 * 根据店铺来分割订单
	 * @param orders
	 * @return
	 * @author 夏黎
	 */
	public Map<Order, Map<Shop, List<OrderItem>>> splitByShop(List<Order> orders) {
		Map<Order,Map<Shop,List<OrderItem>>> map = new HashMap<Order,Map<Shop,List<OrderItem>>>();
		Map<Shop,List<OrderItem>> shopMap = new HashMap<Shop,List<OrderItem>>();
		
		for (Order order : orders) {
			if(map.containsKey(order)){
				shopMap = map.get(order);
				for (OrderItem orderItem : order.getOrderItems()) {
					Shop shop = orderItem.getProduct().getShop();
					if(shopMap.containsKey(shop)){
						
					}else{
						shopMap.put(shop, new ArrayList<OrderItem>());
					}
					shopMap.get(shop).add(orderItem);
				}
			}else{
				map.put(order, new HashMap<Shop,List<OrderItem>>());
				shopMap = map.get(order);
				for (OrderItem orderItem : order.getOrderItems()) {
					Shop shop = orderItem.getProduct().getShop();
					if(shopMap.containsKey(shop)){
						
					}else{
						shopMap.put(shop, new ArrayList<OrderItem>());
					}
					shopMap.get(shop).add(orderItem);
				}
			}
		}
		return map;
	}

	/*@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(ModelMap model,Integer type) {
		Member member = memberService.getCurrent();
		if(type==1){//已付款
			model.addAttribute("orders", orderService.findPaymentList(member));
		}else if(type==2){//未付款
			model.addAttribute("orders", orderService.findWaitPaymentList(member));
		}else if(type==3){//已发货
			model.addAttribute("orders", orderService.findShippedList(member));
		}else if(type==4){//未发货
			model.addAttribute("orders", orderService.findUnShipList(member));
		}else if(type==5){//已确认
			model.addAttribute("orders", orderService.findConfirmedList(member));
		}else if(type==6){//未确认
			model.addAttribute("orders", orderService.findUnConfirmeList(member));
		}else if(type==7){//已评价
			model.addAttribute("orders", orderService.findPingJiaList(member));
		}else if(type==8){//未评价
			model.addAttribute("orders", orderService.findUnPingJiaList(member));
		}else if(type==9){//已退款
			model.addAttribute("orders", orderService.findTuiKuanList(member));
		}else if(type==1){//已退货
			model.addAttribute("orders", orderService.findTuiHuoList(member));
		}else if(type==0){
			model.addAttribute("orders",orderService.findList(member, null, null, null));
		}
		model.addAttribute("type", type);
		return "wap/order/list";
	}*/
}