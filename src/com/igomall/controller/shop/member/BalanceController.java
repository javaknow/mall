
package com.igomall.controller.shop.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.plugin.PaymentPlugin;
import com.igomall.service.BankService;
import com.igomall.service.DepositService;
import com.igomall.service.MemberService;
import com.igomall.service.PluginService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopMemberBalanceController")
@RequestMapping("/member/balance")
public class BalanceController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;

	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(String paymentPluginId, BigDecimal amount) {
		Map<String, Object> data = new HashMap<String, Object>();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled() || amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(amount));
		return data;
	}

	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		return data;
	}

	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public String recharge(ModelMap model) {
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		if (!paymentPlugins.isEmpty()) {
			
			//设置第一个是默认的支付方式
			model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(0));
			model.addAttribute("paymentPlugins", paymentPlugins);
			model.addAttribute("banks", bankService.findAll(true));
		}
		return "shop/member/deposit/recharge";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", depositService.findPage(member, pageable));
		return "shop/member/balance/index";
	}

}