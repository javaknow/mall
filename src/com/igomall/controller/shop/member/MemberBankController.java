
package com.igomall.controller.shop.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.entity.MemberBank;
import com.igomall.service.AreaService;
import com.igomall.service.MemberBankService;
import com.igomall.service.MemberService;

@Controller("shopMemberMemberBankController")
@RequestMapping("/member/memberBank")
public class MemberBankController extends BaseController {

	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/shop/member/memberBank/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(MemberBank memberBank,Long areaId,RedirectAttributes redirectAttributes) {
		if (!isValid(memberBank)) {
			return ERROR_VIEW;
		}
		memberBank.setMember(memberService.getCurrent());
		memberBank.setArea(areaService.find(areaId));
		memberBankService.save(memberBank);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("memberBank", memberBankService.find(id));
		return "/shop/member/memberBank/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(MemberBank memberBank,Long areaId,RedirectAttributes redirectAttributes) {
		if (!isValid(memberBank)) {
			return ERROR_VIEW;
		}
		memberBank.setMember(memberService.getCurrent());
		memberBank.setArea(areaService.find(areaId));
		memberBankService.update(memberBank);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("page", memberBankService.findPage(member,pageable));
		return "/shop/member/memberBank/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Long id,RedirectAttributes redirectAttributes) {
		memberBankService.delete(id);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

}