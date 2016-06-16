package com.igomall.controller.wap;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberRechargeController")
@RequestMapping("/wap/member/recharge")
public class RechargeController extends BaseController {
	
	@RequestMapping(value = "/rechargecardlog_list", method = RequestMethod.GET)
	public String rechargecardlog_list(ModelMap model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "wap/tmpl/member/recharge/rechargecardlog_list";
	}

	@RequestMapping(value = "/rechargecard_add", method = RequestMethod.GET)
	public String rechargecard_add(ModelMap model) {
		return "wap/tmpl/member/recharge/rechargecard_add";
	}
	
	
}