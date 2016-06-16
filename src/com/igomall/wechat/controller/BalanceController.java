
package com.igomall.wechat.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.MemberService;

@Controller("wapBalanceController")
@RequestMapping("/wechat/balance")
public class BalanceController extends BaseController {
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@RequestMapping(value = "/amount", method = RequestMethod.GET)
	public String amount(ModelMap model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		return "wap/balance/amount";
	}
	
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public String balance(ModelMap model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		return "wap/balance/balance";
	}
	
	@RequestMapping(value = "/balance1", method = RequestMethod.GET)
	public String balance1(ModelMap model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		return "wap/balance/balance1";
	}
	
	@RequestMapping(value = "/point", method = RequestMethod.GET)
	public String point(ModelMap model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		return "wap/balance/point";
	}
}