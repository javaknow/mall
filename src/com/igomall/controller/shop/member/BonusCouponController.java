
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Member;
import com.igomall.entity.BonusCoupon.Type;
import com.igomall.service.BonusCouponService;
import com.igomall.service.MemberService;

@Controller("shopMemberBonusCouponController")
@RequestMapping("/member/bonusCoupon")
public class BonusCouponController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", bonusCouponService.findPage(member, pageable,Type.member));
		return "shop/member/bonusCoupon/list";
	}
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(Integer pageNumber, ModelMap model,Long id) {
		Member member = memberService.getCurrent();
		BonusCoupon bonusCoupon = bonusCouponService.find(id);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", bonusCouponService.findPage(member, pageable,Type.shop,bonusCoupon));
		return "shop/member/bonusCoupon/list1";
	}

}