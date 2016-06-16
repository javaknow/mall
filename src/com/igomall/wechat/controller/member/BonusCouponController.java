
package com.igomall.wechat.controller.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.BonusCouponService;
import com.igomall.service.MemberService;

@Controller("wechatMemberBonusCouponController")
@RequestMapping("/wechat/member/bonusCoupon")
public class BonusCouponController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("bonusCoupons", bonusCouponService.findList(member,null,null,null,null,null));
		model.addAttribute("balance",bonusCouponService.count(member, null, null, null, null, null));
		return "wechat/member/bonusCoupon/list";
	}
}