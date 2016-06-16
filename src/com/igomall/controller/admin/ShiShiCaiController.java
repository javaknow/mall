/*
 * 
 * 
 * 
 */
package com.igomall.controller.admin;

import javax.annotation.Resource;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.ShiShiCai;
import com.igomall.entity.ShiShiCai.Type;
import com.igomall.service.ShiShiCaiService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminShiShiCaiController")
@RequestMapping("/admin/shiShiCai")
public class ShiShiCaiController extends BaseController {

	@Resource(name = "shiShiCaiServiceImpl")
	private ShiShiCaiService shiShiCaiService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Type.values());
		return "/admin/shiShiCai/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ShiShiCai shiShiCai, RedirectAttributes redirectAttributes) {
		if (!isValid(shiShiCai)) {
			return ERROR_VIEW;
		}
		shiShiCaiService.save(shiShiCai);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("shiShiCai", shiShiCaiService.find(id));
		return "/admin/shiShiCai/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ShiShiCai shiShiCai, RedirectAttributes redirectAttributes) {
		if (!isValid(shiShiCai)) {
			return ERROR_VIEW;
		}
		shiShiCaiService.update(shiShiCai, "ads");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", shiShiCaiService.findPage(pageable));
		return "/admin/shiShiCai/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		shiShiCaiService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}