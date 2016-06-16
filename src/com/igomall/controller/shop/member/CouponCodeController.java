/*
 * 
 * 
 * 
 */
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Coupon;
import com.igomall.entity.Member;
import com.igomall.service.CouponCodeService;
import com.igomall.service.CouponService;
import com.igomall.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopMemberCouponCodeController")
@RequestMapping("/member/coupon_code")
public class CouponCodeController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponService.findPage(true, true, false, pageable));
		return "shop/member/coupon_code/exchange";
	}

	@RequestMapping(value = "/exchange", method = RequestMethod.POST)
	public @ResponseBody
	Message exchange(Long id) {
		Coupon coupon = couponService.find(id);
		if (coupon == null || !coupon.getIsEnabled() || !coupon.getIsExchange() || coupon.hasExpired()) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (member.getPoint() < coupon.getPoint()) {
			return Message.warn("shop.member.couponCode.point");
		}
		couponCodeService.exchange(coupon, member);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponCodeService.findPage(member, pageable));
		return "shop/member/coupon_code/list";
	}

}