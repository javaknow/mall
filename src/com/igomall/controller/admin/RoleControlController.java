
package com.igomall.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.Menu;
import com.igomall.entity.RoleControl;
import com.igomall.service.MenuService;
import com.igomall.service.RoleControlService;

@Controller("adminRoleControlController")
@RequestMapping("/admin/roleControl")
public class RoleControlController extends BaseController {

	@Resource(name = "roleControlServiceImpl")
	private RoleControlService roleControlService;
	@Resource(name = "menuServiceImpl")
	private MenuService menuService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("menuTree",menuService.findTree(true));
		return "/admin/roleControl/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RoleControl roleControl,Long menuId, RedirectAttributes redirectAttributes) {
		Menu menu = menuService.find(menuId);
		if(menu==null){
			return ERROR_VIEW;
		}
		if (!isValid(roleControl)) {
			return ERROR_VIEW;
		}
		
		roleControl.setMenu(menu);
		roleControl.setRoleControlName("admin:"+menu.getName()+"_"+roleControl.getName()+"_"+roleControl.getMethod());
		
		roleControlService.update(roleControl);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("roleControl", roleControlService.find(id));
		model.addAttribute("menuTree",menuService.findTree(true));
		return "/admin/roleControl/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(RoleControl roleControl, Long menuId, RedirectAttributes redirectAttributes) {
		Menu menu = menuService.find(menuId);
		if(menu==null){
			return ERROR_VIEW;
		}
		if (!isValid(roleControl)) {
			return ERROR_VIEW;
		}
		roleControl.setMenu(menu);
		roleControl.setRoleControlName("admin:"+menu.getName()+"_"+roleControl.getName()+"_"+roleControl.getMethod());
		roleControlService.update(roleControl);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", roleControlService.findPage(pageable));
		return "/admin/roleControl/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		roleControlService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}