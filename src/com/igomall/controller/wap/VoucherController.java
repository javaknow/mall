package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberVoucherController")
@RequestMapping("/wap/member/voucher")
public class VoucherController extends BaseController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String rechargecardlog_list(ModelMap model) {
		return "wap/tmpl/member/voucher/voucher_list";
	}

	@RequestMapping(value = "/voucher_pwex", method = RequestMethod.GET)
	public String rechargecard_add(ModelMap model) {
		return "wap/tmpl/member/voucher/voucher_pwex";
	}
	
	
}