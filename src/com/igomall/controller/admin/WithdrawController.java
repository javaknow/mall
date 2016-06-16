package com.igomall.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Withdraw;
import com.igomall.entity.Withdraw.Status;
import com.igomall.service.AdminService;
import com.igomall.service.MemberService;
import com.igomall.service.WithdrawService;

@Controller("adminWithdrawController")
@RequestMapping("/admin/withdraw")
public class WithdrawController extends BaseController {

	@Resource(name = "withdrawServiceImpl")
	private WithdrawService withdrawService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model,Status status) {
		model.addAttribute("page", withdrawService.findPage(null,status,pageable,null,null));
		
		return "/admin/withdraw/list";
	}

	@RequestMapping(value = "/ok", method = RequestMethod.POST)
	public @ResponseBody
	Message ok(Long id) {
		Message message = new Message();
		Withdraw withdraw = withdrawService.find(id);
		if (withdraw == null) {
			message = ERROR_MESSAGE;
		} else if (withdraw.getStatus() == Status.wait) {
			withdraw.setStatus(Status.success);
			withdraw.setOperator(adminService.getCurrent());
			withdrawService.update(withdraw);
			message = SUCCESS_MESSAGE;
		} else {
			message = ERROR_MESSAGE;
		}

		return message;

	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public @ResponseBody
	Message reject(Long id) {
		Message message = new Message();
		Withdraw withdraw = withdrawService.find(id);
		if (withdraw == null) {
			message = ERROR_MESSAGE;
		} else if (withdraw.getStatus() == Status.wait) {
			withdraw.setStatus(Status.failure);
			withdraw.setOperator(adminService.getCurrent());
			withdrawService.update(withdraw);
			Member member = withdraw.getMember();
			member.setBalance(member.getBalance().add(withdraw.getBalance()));
			memberService.update(member);
			message = SUCCESS_MESSAGE;
		} else {
			message = ERROR_MESSAGE;
		}

		return message;

	}

}