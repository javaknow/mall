
package com.igomall.controller.shop;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Setting;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.Payment;
import com.igomall.entity.PaymentMethod;
import com.igomall.entity.Recharge;
import com.igomall.entity.Sn;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Payment.Method;
import com.igomall.entity.Payment.Status;
import com.igomall.entity.Payment.Type;
import com.igomall.plugin.PaymentPlugin;
import com.igomall.plugin.PaymentPlugin.NotifyMethod;
import com.igomall.service.BankService;
import com.igomall.service.MemberService;
import com.igomall.service.OrderService;
import com.igomall.service.PaymentService;
import com.igomall.service.PluginService;
import com.igomall.service.RechargeService;
import com.igomall.service.SnService;
import com.igomall.util.SettingUtils;

@Controller("shopPaymentController")
@RequestMapping("/payment")
public class PaymentController extends BaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;
	@Resource(name = "rechargeServiceImpl")
	private RechargeService rechargeService;
	
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(Type type, String paymentPluginId, String sn, BigDecimal amount,Long bankId, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return ERROR_VIEW;
		}
		Payment payment = new Payment();
		payment.setBank(bankService.find(bankId));
		String description = null;
		if (type == Type.payment) {//订单
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember()) || order.isExpired() || order.isLocked(null)) {
				return ERROR_VIEW;
			}
			if (order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() != PaymentMethod.Method.online) {
				return ERROR_VIEW;
			}
			if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
				return ERROR_VIEW;
			}
			if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
				return ERROR_VIEW;
			}
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(order.getPaymentMethodName() + Payment.PAYMENT_METHOD_SEPARATOR + paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(order.getAmountPayable()));
			payment.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setOrder(order);
			paymentService.save(payment);
			description = order.getName();
		} else if (type == Type.recharge) {//充值
			Setting setting = SettingUtils.get();
			Recharge recharge = null;
			if(sn!=null){
				recharge = rechargeService.findBySn(sn);
				amount = recharge.getBalance();
				payment.setRecharge(recharge);
				amount = amount.setScale(setting.getPriceScale());
			}
			
			
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return ERROR_VIEW;
			}
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.recharge);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(amount));
			payment.setAmount(paymentPlugin.calculateAmount(amount));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setMember(member);
			paymentService.save(payment);
			if(recharge!=null){
				recharge.setPayment(payment);
				rechargeService.update(recharge);
			}
			description = message("shop.member.deposit.recharge");
		} else {
			return ERROR_VIEW;
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		model.addAttribute("parameterMap", paymentPlugin.getParameterMap(payment.getSn(), description, request));
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "shop/payment/submit";
	}

	@RequestMapping("/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model,String extra_return_param) {
		Payment payment = paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null) {
				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					paymentService.handle(payment);
				}else if("igomall_ok".equals(extra_return_param)){
					paymentService.handle(payment);
				}
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
			}
			model.addAttribute("payment", payment);
		}
		return "shop/payment/notify";
	}

}