
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;

@Controller("shopMemberFavoriteController")
@RequestMapping("/member/favorite")
public class FavoriteController extends BaseController {
	private static final int PAGE_SIZE = 10;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Message add(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return ERROR_MESSAGE;
		}
		
		Member member = memberService.getCurrent();
		
		if (member.getFavoriteProducts().contains(product)) {
			return Message.warn("shop.member.favorite.exist");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
			return Message.warn("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
		}
		member.getFavoriteProducts().add(product);
		memberService.update(member);
		return Message.success("shop.member.favorite.success");
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", productService.findPage(member, pageable));
		return "shop/member/favorite/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!member.getFavoriteProducts().contains(product)) {
			return ERROR_MESSAGE;
		}
		member.getFavoriteProducts().remove(product);
		memberService.update(member);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete1(Long id, ModelMap model) {
		Product product = productService.find(id);
		if (product == null) {
			model.addAttribute("msg", "产品不存在，删除失败");
		}
		Member member = memberService.getCurrent();
		if (!member.getFavoriteProducts().contains(product)) {
			model.addAttribute("msg", "您未已收藏过该产品，删除失败");
		}
		member.getFavoriteProducts().remove(product);
		memberService.update(member);
		model.addAttribute("msg", "删除成功");
		return "/shop/member/favorite/ajaxInfo";
	}

}