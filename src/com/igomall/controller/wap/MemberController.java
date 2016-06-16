
package com.igomall.controller.wap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.MemberService;

@Controller("wapMemberController")
@RequestMapping("/wap/member")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/wap/tmpl/member/signin";
	}
	
	/**
	 * 积分列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pointList", method = RequestMethod.GET)
	public String pointList(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/wap/tmpl/member/pointList";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/wap/tmpl/member/index";
	}
	
	@RequestMapping(value = "/member_asset", method = RequestMethod.GET)
	public String member_asset(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/wap/tmpl/member/member_asset";
	}
	
	@RequestMapping(value = "/views_list", method = RequestMethod.GET)
	public String views_list(ModelMap model) {
		return "/wap/tmpl/member/views_list";
	}
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(ModelMap model) {
		return "/wap/tmpl/member/history/index";
	}
	
	@RequestMapping(value = "/member_account", method = RequestMethod.GET)
	public String member_account(ModelMap model) {
		return "/wap/tmpl/member/member_account";
	}
	
	@RequestMapping(value = "/member_password_step1", method = RequestMethod.GET)
	public String member_password_step1(ModelMap model) {
		return "/wap/tmpl/member/member_password_step1";
	}
	
	@RequestMapping(value = "/member_password_step2", method = RequestMethod.GET)
	public String member_password_step2(ModelMap model) {
		return "/wap/tmpl/member/member_password_step2";
	}
	
	
	@RequestMapping(value = "/member_feedback", method = RequestMethod.GET)
	public String member_feedback(ModelMap model) {
		return "/wap/tmpl/member/member_feedback";
	}
	
	@RequestMapping(value = "/member_paypwd_step1", method = RequestMethod.GET)
	public String member_paypwd_step1(ModelMap model) {
		return "/wap/tmpl/member/member_paypwd_step1";
	}
	
	@RequestMapping(value = "/member_mobile_bind", method = RequestMethod.GET)
	public String member_mobile_bind(ModelMap model) {
		return "/wap/tmpl/member/member_mobile_bind";
	}
	
	@RequestMapping(value = "/member_mobile_modify", method = RequestMethod.GET)
	public String member_mobile_modify(ModelMap model) {
		return "/wap/tmpl/member/member_mobile_modify";
	}
	
}