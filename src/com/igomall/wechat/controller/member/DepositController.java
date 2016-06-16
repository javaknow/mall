
package com.igomall.wechat.controller.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.BankService;
import com.igomall.service.DepositService;
import com.igomall.service.MemberService;
import com.igomall.service.PluginService;
import com.igomall.service.WithdrawService;

@Controller("wechatMemberDepositController")
@RequestMapping("/wechat/member/deposit")
public class DepositController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;
	@Resource(name = "withdrawServiceImpl")
	private WithdrawService withdrawService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", depositService.findPage(member, pageable));
		model.addAttribute("member",member);
		return "wechat/member/balance/list";
	}
	
	@RequestMapping(value = "/rechargeRecord", method = RequestMethod.GET)
	public String rechargeRecord(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("records", depositService.findReChageList(member));
		model.addAttribute("member",member);
		return "wechat/member/balance/rechargeRecord";
	}

	@RequestMapping(value = "/withdrawRecord", method = RequestMethod.GET)
	public String withdrawRecord(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("records", withdrawService.findList(member, null, null, null));
		model.addAttribute("member",member);
		return "wechat/member/balance/withdrawRecord";
	}
}