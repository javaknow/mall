package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberRedPacketController")
@RequestMapping("/wap/member/redPacket")
public class RedPacketController extends BaseController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String rechargecardlog_list(ModelMap model) {
		return "wap/tmpl/member/redPacket/redpacket_list";
	}

	@RequestMapping(value = "/redpacket_pwex", method = RequestMethod.GET)
	public String rechargecard_add(ModelMap model) {
		return "wap/tmpl/member/redPacket/redpacket_pwex";
	}
	
	
}