package com.igomall.controller.shop.member;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.igomall.entity.Sn;
import com.igomall.entity.Withdraw;
import com.igomall.service.MemberBankService;
import com.igomall.service.MemberService;
import com.igomall.service.SnService;
import com.igomall.service.WithdrawService;
import com.igomall.util.SettingUtils;

@Controller("shopMemberWithdrawController")
@RequestMapping("/member/withdraw")
public class WithdrawController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "withdrawServiceImpl")
	private WithdrawService withdrawService;
	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	@Resource(name = "snServiceImpl")
	private SnService snService;

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
	public String add(ModelMap model, Integer flag) {
		model.addAttribute("member",memberService.getCurrent());
		Member member = memberService.getCurrent();
		model.addAttribute("memberBanks", memberBankService.findList(member));
		return "shop/member/withdraw/add";
		

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", withdrawService.findPage(member,null, pageable,null,null));
		return "shop/member/withdraw/list";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Withdraw withdraw, Long memberBankId, BigDecimal balance, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
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
		
		withdraw.setMemberBank(memberBankService.find(memberBankId));
		withdraw.setMember(member);
		
		withdraw.setFee(withdraw.getBalance().multiply(setting.getFeeRate()));
		withdraw.setRealBalance(withdraw.getBalance().subtract(withdraw.getFee()));
		withdraw.setNumber(snService.generate(Sn.Type.payment));
		
		withdrawService.save(withdraw);

		member.setBalance(member.getBalance().subtract(withdraw.getBalance()));
		memberService.update(member);
		return "redirect:list.jhtml";
	}

}