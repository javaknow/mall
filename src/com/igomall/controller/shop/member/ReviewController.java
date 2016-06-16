
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.MemberService;
import com.igomall.service.ReviewService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("shopMemberReviewController")
@RequestMapping("/member/review")
public class ReviewController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", reviewService.findPage(member, null, null, null, pageable));
		return "shop/member/review/list";
	}

}