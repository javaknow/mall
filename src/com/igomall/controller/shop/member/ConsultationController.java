/*
 * 
 * 
 * 
 */
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.ConsultationService;
import com.igomall.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("shopMemberConsultationController")
@RequestMapping("/member/consultation")
public class ConsultationController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", consultationService.findPage(member, null, null, pageable));
		return "shop/member/consultation/list";
	}

}