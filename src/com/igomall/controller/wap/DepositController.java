
package com.igomall.controller.wap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.Member;
import com.igomall.service.BankService;
import com.igomall.service.DepositService;
import com.igomall.service.MemberService;
import com.igomall.service.PluginService;

/**
 * 
 * @author blackboy
 *
 */
@Controller("wapMemberDepositController")
@RequestMapping("/wap/member/deposit")
public class DepositController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "bankServiceImpl")
	private BankService bankService;


	@RequestMapping(value = "/predepositlog_list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		return "wap/tmpl/member/deposit/predepositlog_list";
	}
	
	@RequestMapping(value = "/pdrecharge_list", method = RequestMethod.GET)
	public String pdrecharge_list(Integer pageNumber, ModelMap model) {
		return "wap/tmpl/member/deposit/pdrecharge_list";
	}
	
	@RequestMapping(value = "/pdcashlist", method = RequestMethod.GET)
	public String pdcashlist(Integer pageNumber, ModelMap model) {
		return "wap/tmpl/member/deposit/pdcashlist";
	}
	
	@RequestMapping(value = "/pdcashinfo", method = RequestMethod.GET)
	public String pdcashinfo(Integer pageNumber, ModelMap model) {
		return "wap/tmpl/member/deposit/pdcashinfo";
	}
	
	
	
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", depositService.findRechargePage(member, pageable));
		return "shop/member/deposit/list1";
	}

}