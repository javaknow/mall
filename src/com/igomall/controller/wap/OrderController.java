
package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberOrderController")
@RequestMapping("/wap/member/order")
public class OrderController extends BaseController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {

		return "/wap/tmpl/member/order/list";
	}
	
	@RequestMapping(value = "/delivery", method = RequestMethod.GET)
	public String delivery() {

		return "/wap/tmpl/member/order/delivery";
	}
	@RequestMapping(value = "/evaluation", method = RequestMethod.GET)
	public String evaluation(String sn) {
		return "/wap/tmpl/member/order/evaluation";
	}
	
	@RequestMapping(value = "/evaluation_again", method = RequestMethod.GET)
	public String evaluation_again() {

		return "/wap/tmpl/member/order/evaluation_again";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail() {

		return "/wap/tmpl/member/order/detail";
	}
}