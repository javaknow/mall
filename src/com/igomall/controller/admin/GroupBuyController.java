
package com.igomall.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.GroupBuy;
import com.igomall.entity.Product;
import com.igomall.service.AdminService;
import com.igomall.service.GroupBuyService;
import com.igomall.service.MemberRankService;
import com.igomall.service.ProductService;

@Controller("adminGroupBuyController")
@RequestMapping("/admin/groupBuy")
public class GroupBuyController extends BaseController {

	@Resource(name = "groupBuyServiceImpl")
	private GroupBuyService groupBuyService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;


	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		return "/admin/groupBuy/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(GroupBuy groupBuy,Long[] productIds, RedirectAttributes redirectAttributes) {
		if (!isValid(groupBuy)) {
			return ERROR_VIEW;
		}
		if (groupBuy.getBeginDate() != null && groupBuy.getEndDate() != null && groupBuy.getBeginDate().after(groupBuy.getEndDate())) {
			return ERROR_VIEW;
		}
		
		groupBuyService.save(groupBuy);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("groupBuy", groupBuyService.find(id));
		return "/admin/groupBuy/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(GroupBuy groupBuy, RedirectAttributes redirectAttributes) {
		if (!isValid(groupBuy)) {
			return ERROR_VIEW;
		}
		if (groupBuy.getBeginDate() != null && groupBuy.getEndDate() != null && groupBuy.getBeginDate().after(groupBuy.getEndDate())) {
			return ERROR_VIEW;
		}
		groupBuyService.update(groupBuy);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", groupBuyService.findPage(pageable));
		return "/admin/groupBuy/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		groupBuyService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/build", method = RequestMethod.GET)
	public String build(Long id, ModelMap model) {
		GroupBuy groupBuy = groupBuyService.find(id);
		model.addAttribute("groupBuy", groupBuy);
		return "/admin/groupBuy/build";
	}
	
	@RequestMapping(value = "search",method=RequestMethod.POST)
	public String search(ModelMap model,String stockMemo){
		List<Product> products = productService.findList(10, null, null);
		model.addAttribute("products",products);
		return "/admin/groupBuy/search";
	}

}