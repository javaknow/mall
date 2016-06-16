
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.MemberService;
import com.igomall.service.MoneyFlowingWaterService;

@Controller("shopMemberMoneyFlowingWaterController")
@RequestMapping("/member/moneyFlowingWater")
public class MoneyFlowingWaterController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "moneyFlowingWaterServiceImpl")
	private MoneyFlowingWaterService moneyFlowingWaterService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", moneyFlowingWaterService.findPage(member, pageable));
		return "shop/member/moneyFlowingWater/list";
	}

}