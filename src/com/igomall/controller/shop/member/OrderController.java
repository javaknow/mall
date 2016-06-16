
package com.igomall.controller.shop.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Area;
import com.igomall.entity.Cart;
import com.igomall.entity.Coupon;
import com.igomall.entity.CouponCode;
import com.igomall.entity.DeliveryCorp;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.OrderItem;
import com.igomall.entity.PaymentMethod;
import com.igomall.entity.Receiver;
import com.igomall.entity.Refunds;
import com.igomall.entity.Returns;
import com.igomall.entity.ReturnsItem;
import com.igomall.entity.Shipping;
import com.igomall.entity.ShippingMethod;
import com.igomall.entity.Sn;
import com.igomall.entity.BonusCoupon.Type;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Order.ShippingStatus;
import com.igomall.entity.Refunds.Method;
import com.igomall.plugin.PaymentPlugin;
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
import com.igomall.util.SettingUtils;

@Controller("shopMemberOrderController")
@RequestMapping("/member/order")
public class OrderController extends BaseController {

	private static final int PAGE_SIZE = 10;

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

	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveReceiver(Receiver receiver, Long areaId) {
		Map<String, Object> data = new HashMap<String, Object>();
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			data.put("message", Message.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return data;
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		data.put("message", SUCCESS_MESSAGE);
		data.put("receiver", receiver);
		return data;
	}

	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	boolean lock(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && !order.isLocked(null) && order.getPaymentMethod() != null && order.getPaymentMethod().getMethod() == PaymentMethod.Method.online && (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment)) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(null);
			orderService.update(order);
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/check_payment", method = RequestMethod.POST)
	public @ResponseBody
	boolean checkPayment(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && order.getPaymentStatus() == PaymentStatus.paid) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> couponInfo(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.warn("shop.order.cartNotEmpty"));
			return data;
		}
		if (!cart.isCouponAllowed()) {
			data.put("message", Message.warn("shop.order.couponNotAllowed"));
			return data;
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			Coupon coupon = couponCode.getCoupon();
			if (!coupon.getIsEnabled()) {
				data.put("message", Message.warn("shop.order.couponDisabled"));
				return data;
			}
			if (!coupon.hasBegun()) {
				data.put("message", Message.warn("shop.order.couponNotBegin"));
				return data;
			}
			if (coupon.hasExpired()) {
				data.put("message", Message.warn("shop.order.couponHasExpired"));
				return data;
			}
			if (!cart.isValid(coupon)) {
				data.put("message", Message.warn("shop.order.couponInvalid"));
				return data;
			}
			if (couponCode.getIsUsed()) {
				data.put("message", Message.warn("shop.order.couponCodeUsed"));
				return data;
			}
			data.put("message", SUCCESS_MESSAGE);
			data.put("couponName", coupon.getName());
			return data;
		} else {
			data.put("message", Message.warn("shop.order.couponCodeNotExist"));
			return data;
		}
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info(ModelMap model) {
		Cart cart = cartService.getCurrent();
		
		if (cart == null || cart.isEmpty()) {
			return "redirect:/cart/list.jhtml";
		}
		if (!isValid(cart)) {
			return ERROR_VIEW;
		}
		Order order = orderService.build(cart, null, null, null, null, false, null, false, null);
		model.addAttribute("order", order);
		model.addAttribute("cartToken", cart.getToken());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		
		
		return "/shop/member/order/info";
	}

	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculate(Long paymentMethodId, Long shippingMethodId, String code, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.order.cartNotEmpty"));
			return data;
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		CouponCode couponCode = couponCodeService.findByCode(code);
		Order order = orderService.build(cart, null, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", order.getQuantity());
		data.put("price", order.getPrice());
		data.put("freight", order.getFreight());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("tax", order.getTax());
		data.put("amountPayable", order.getAmountPayable());//总金额
		return data;
	}

	/**
	 * 订单的创建
	 * @param cartToken
	 * @param receiverId
	 * @param paymentMethodId
	 * @param shippingMethodId
	 * @param code
	 * @param isInvoice
	 * @param invoiceTitle
	 * @param useBalance
	 * @param memo
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	Message create(String cartToken, Long receiverId, Long paymentMethodId, Long shippingMethodId, String code, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance, String memo) {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.warn("shop.order.cartNotEmpty");
		}
		if (!StringUtils.equals(cart.getToken(), cartToken)) {
			return Message.warn("shop.order.cartHasChanged");
		}
		if (cart.getIsLowStock()) {
			return Message.warn("shop.order.cartLowStock");
		}
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return Message.error("shop.order.receiverNotExsit");
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (paymentMethod == null) {
			return Message.error("shop.order.paymentMethodNotExsit");
		}
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		if (shippingMethod == null) {
			return Message.error("shop.order.shippingMethodNotExsit");
		}
		if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
			return Message.error("shop.order.deliveryUnsupported");
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		Order order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo, null);
		
		if(order==null){
			return Message.error("订单创建失败");
		}
		return Message.success(order.getSn());
	}

	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired() || order.getPaymentMethod() == null) {
			return ERROR_VIEW;
		}
		if (order.getPaymentMethod().getMethod() == PaymentMethod.Method.online) {
			List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
			if (!paymentPlugins.isEmpty()) {
				PaymentPlugin defaultPaymentPlugin = paymentPlugins.get(0);
				if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
					model.addAttribute("fee", defaultPaymentPlugin.calculateFee(order.getAmountPayable()));
					model.addAttribute("amount", defaultPaymentPlugin.calculateAmount(order.getAmountPayable()));
				}
				model.addAttribute("defaultPaymentPlugin", defaultPaymentPlugin);
				model.addAttribute("paymentPlugins", paymentPlugins);
			}
		}
		model.addAttribute("banks",bankService.findAll(true));
		model.addAttribute("order", order);
		return "/shop/member/order/payment";
	}

	@RequestMapping(value = "/calculate_amount", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.findBySn(sn);
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired() || order.isLocked(null) || order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline || paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(order.getAmountPayable()));
		data.put("amount", paymentPlugin.calculateAmount(order.getAmountPayable()));
		return data;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", orderService.findPage(member, pageable));
		return "shop/member/order/list";
	}
	
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", orderService.findPage(member, pageable));
		return "shop/member/order/list1";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.getOrders().contains(order)) {
			return ERROR_VIEW;
		}
		model.addAttribute("order", order);
		return "shop/member/order/view";
	}

	/**
	 * 订单的确认
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public @ResponseBody
	Message confirm(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.confirmed) {
			orderService.confirm(order, null);
			return SUCCESS_MESSAGE;
		} else {
			return ERROR_MESSAGE;
		}
	}
	
	/**
	 * 订单的退货
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public @ResponseBody
	Message returns(String sn) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		Returns returns = new Returns();
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			ReturnsItem returnsItem = new ReturnsItem();
			returnsItem.setQuantity(orderItem.getQuantity());
			returns.getReturnsItems().add(returnsItem);
		}
		
		for (Iterator<ReturnsItem> iterator = returns.getReturnsItems().iterator(); iterator.hasNext();) {
			ReturnsItem returnsItem = iterator.next();
			if (returnsItem == null || StringUtils.isEmpty(returnsItem.getSn()) || returnsItem.getQuantity() == null || returnsItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getShippedQuantity() - orderItem.getReturnQuantity()) {
				return ERROR_MESSAGE;
			}
			returnsItem.setName(orderItem.getFullName());
			returnsItem.setReturns(returns);
		}
		returns.setOrder(order);
		ShippingMethod shippingMethod = order.getShippingMethod();
		returns.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCorp deliveryCorp = null;
		returns.setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
		Area area = order.getArea();
		returns.setArea(area != null ? area.getFullName() : null);
		if (!isValid(returns)) {
			return ERROR_MESSAGE;
		}
		if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
			return ERROR_MESSAGE;
		}
		if (order.getShippingStatus() != ShippingStatus.shipped && order.getShippingStatus() != ShippingStatus.partialShipment && order.getShippingStatus() != ShippingStatus.partialReturns) {
			return ERROR_MESSAGE;
		}
		returns.setSn(snService.generate(Sn.Type.returns));
		returns.setOperator("");
		orderService.returns(order, returns, null);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 订单的退款
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	public @ResponseBody
	Message refunds(String sn) {
		Order order = orderService.findBySn(sn);
		Refunds refunds = new Refunds();
		refunds.setOrder(order);
		PaymentMethod paymentMethod = order.getPaymentMethod();
		refunds.setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
		refunds.setMethod(Method.online);
		if(order.getShippingStatus()==ShippingStatus.unshipped){
			refunds.setAmount(order.getAmountPaid());
		}else{
			refunds.setAmount(BigDecimal.ZERO);
		}
		refunds.setSn(snService.generate(Sn.Type.refunds));
		refunds.setOperator("");
		/*if (!isValid(refunds)) {
			return ERROR_MESSAGE;
		}*/
		if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
			return ERROR_MESSAGE;
		}
		if (order.getPaymentStatus() != PaymentStatus.paid && order.getPaymentStatus() != PaymentStatus.partialPayment && order.getPaymentStatus() != PaymentStatus.partialRefunds) {
			return ERROR_MESSAGE;
		}
		
		
		
		refunds.setSn(snService.generate(Sn.Type.refunds));
		orderService.refunds(order, refunds, null);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && order.getPaymentStatus() == PaymentStatus.unpaid) {
			if (order.isLocked(null)) {
				return Message.warn("shop.member.order.locked");
			}
			orderService.cancel(order, null);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}

	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deliveryQuery(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.findBySn(sn);
		Setting setting = SettingUtils.get();
		if (shipping != null && shipping.getOrder() != null && memberService.getCurrent().equals(shipping.getOrder().getMember()) && StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
			data = shippingService.query(shipping);
		}
		return data;
	}
	
	/**
	 * 订单的完成
	 * @param sn
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public @ResponseBody
	Message complete(String sn, RedirectAttributes redirectAttributes,HttpServletRequest reqeust) {
		Order order = orderService.findBySn(sn);
		if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.confirmed) {
			orderService.complete(order, null);
			bonusCouponService.create(order,Type.member,reqeust);
			return SUCCESS_MESSAGE;
		} else {
			return ERROR_MESSAGE;
		}
	}

}