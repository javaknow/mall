package com.igomall.controller.admin;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.entity.Menu;
import com.igomall.service.MenuService;
import com.igomall.util.Pinyin;

@Controller("adminMenuController")
@RequestMapping("/admin/menu")
public class MenuController extends BaseController {

	@Resource(name = "menuServiceImpl")
	private MenuService menuService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("menuTree", menuService.findTree(null));
		return "/admin/menu/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Menu menu, Long parentId, RedirectAttributes redirectAttributes) {
		menu.setParent(menuService.find(parentId));
		if (!isValid(menu)) {
			return ERROR_VIEW;
		}

		menu.setTreePath(null);
		menu.setGrade(null);
		menu.setChildren(null);
		menuService.save(menu);
		menu.setRoleName("admin:"+Pinyin.toPinyin(menu.getName())+"_"+menu.getId());
		menuService.update(menu);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:add.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Menu menu = menuService.find(id);
		model.addAttribute("menuTree",menuService.findTree(null));
		model.addAttribute("menu", menu);
		model.addAttribute("children", menuService.findChildren(menu, null));
		return "/admin/menu/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Menu menu, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
		menu.setParent(menuService.find(parentId));
		if (!isValid(menu)) {
			return ERROR_VIEW;
		}
		
		menu.setRoleName("admin:"+Pinyin.toPinyin(menu.getName())+"_"+menu.getId());
		menuService.update(menu);
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("menuTree",menuService.findTree(null));
		return "/admin/menu/list";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Menu menu = menuService.find(id);
		if (menu == null) {
			return ERROR_MESSAGE;
		}
		Set<Menu> children = menu.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.menu.deleteExistChildrenNotAllowed");
		}
		menuService.delete(id);
		return SUCCESS_MESSAGE;
	}
	
}