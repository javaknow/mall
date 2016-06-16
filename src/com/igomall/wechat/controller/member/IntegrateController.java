
package com.igomall.wechat.controller.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.IntegrateService;
import com.igomall.service.MemberService;

@Controller("wechatMemberIntegrateController")
@RequestMapping("/wechat/member/integrate")
public class IntegrateController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "integrateServiceImpl")
	private IntegrateService integrateService;
	

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("integrates", integrateService.findAll(member));
		model.addAttribute("member",member);
		return "wechat/member/integrate/list";
	}
	
}