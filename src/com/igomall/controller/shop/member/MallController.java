
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.service.MemberService;

@Controller("shopMemberMallController")
@RequestMapping("/member/mall")
public class MallController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = "/apply", method = RequestMethod.GET)
	public String list(ModelMap model) {
		return "shop/member/mall/apply";
	}
}