
package com.igomall.controller.wap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.entity.Receiver;
import com.igomall.service.AreaService;
import com.igomall.service.MemberService;
import com.igomall.service.ReceiverService;

@Controller("wapMemberReceiverController")
@RequestMapping("/wap/member/receiver")
public class ReceiverController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", receiverService.findPage(member, pageable));
		return "wap/tmpl/member/receiver/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, Message.warn("shop.member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return "redirect:list.jhtml";
		}
		return "wap/tmpl/member/receiver/add";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(RedirectAttributes redirectAttributes) {
		return "wap/tmpl/member/receiver/edit";
	}
}