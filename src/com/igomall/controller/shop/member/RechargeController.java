package com.igomall.controller.shop.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.entity.MemberBank;
import com.igomall.entity.Recharge;
import com.igomall.entity.Recharge.Status;
import com.igomall.entity.Sn.Type;
import com.igomall.plugin.PaymentPlugin;
import com.igomall.service.BankService;
import com.igomall.service.MemberBankService;
import com.igomall.service.MemberService;
import com.igomall.service.PluginService;
import com.igomall.service.RechargeService;
import com.igomall.service.SnService;
import com.igomall.util.SettingUtils;

@Controller("shopMemberRechargeController")
@RequestMapping("/member/recharge")
public class RechargeController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "rechargeServiceImpl")
	private RechargeService rechargeService;
	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;
	
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(Long memberBankId, BigDecimal balance) {
		Setting setting = SettingUtils.get();
		Map<String, Object> data = new HashMap<String, Object>();
		MemberBank memberBank = memberBankService.find(memberBankId);
		if (memberBank == null) {
			data.put("message", ERROR_MESSAGE);
		} else {
			data.put("message", SUCCESS_MESSAGE);
			data.put("fee", setting.getFeeRate().multiply(balance));
			data.put("bankTrueName", memberBank.getBankTrueName());
			data.put("bankType", memberBank.getBankType());
			data.put("bankAccount", memberBank.getBankAccount());
		}

		return data;
	}
	
	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		data.put("state","error");
		return data;
	}
	
	
	@RequestMapping(value = "/check_money", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkMoney(BigDecimal balance) {
		Member member = memberService.getCurrent();
		if (balance==null||balance.compareTo(member.getBalance())>0) {
			return false;
		}
		return true;
		
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		
		return "shop/member/recharge/add";
	}
	
	@RequestMapping(value = "/recharge", method = RequestMethod.POST)
	public String recharge(ModelMap model,BigDecimal amount) {
		Recharge recharge = new Recharge();
		recharge.setSn(snService.generate(Type.recharge));
		recharge.setBalance(amount);
		recharge.setFee(BigDecimal.ZERO);
		recharge.setMember(memberService.getCurrent());
		recharge.setMemo("在线充值");
		recharge.setOperator(null);
		recharge.setRealBalance(amount);
		recharge.setStatus(Status.wait);
		rechargeService.save(recharge);
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		if (!paymentPlugins.isEmpty()) {
			
			//设置第一个是默认的支付方式
			model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(0));
			model.addAttribute("paymentPlugins", paymentPlugins);
			model.addAttribute("banks", bankService.findAll(true));
		}
		model.addAttribute("recharge", recharge);
		return "shop/member/recharge/recharge";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", rechargeService.findPage(member,null, pageable,null,null));
		return "shop/member/recharge/list";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Recharge recharge, Long memberBankId, BigDecimal balance, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		
		if (member == null) {
			return ERROR_VIEW;
		}

		Setting setting = SettingUtils.get();
		if (balance == null || balance.compareTo(new BigDecimal(0)) <= 0 || balance.precision() > 15 || balance.scale() > setting.getPriceScale()) {
			return ERROR_VIEW;
		}
		
		if(balance.compareTo(member.getBalance())>0){
			return ERROR_VIEW;
		}
		
		recharge.setMember(member);
		
		recharge.setFee(recharge.getBalance().multiply(setting.getFeeRate()));
		recharge.setRealBalance(recharge.getBalance().subtract(recharge.getFee()));
		
		rechargeService.save(recharge);

		member.setBalance(member.getBalance().subtract(recharge.getBalance()));
		memberService.update(member);
		return "redirect:list.jhtml";
	}
	
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(ModelMap model, String sn) {
		model.addAttribute("recharge",rechargeService.findBySn(sn));
		
		return "shop/member/recharge/view";
		

	}

}