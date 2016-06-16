
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
import com.igomall.entity.ShopRank;
import com.igomall.service.ShopRankService;

@Controller("adminShopRankController")
@RequestMapping("/admin/shopRank")
public class ShopRankController extends BaseController {

	@Resource(name = "shopRankServiceImpl")
	private ShopRankService shopRankService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/shop_rank/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ShopRank shopRank, RedirectAttributes redirectAttributes) {
		if (!isValid(shopRank)) {
			return ERROR_VIEW;
		}
	
		shopRank.setShops(null);
		shopRankService.save(shopRank);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("shopRank", shopRankService.find(id));
		return "/admin/shop_rank/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ShopRank shopRank, RedirectAttributes redirectAttributes) {
		if (!isValid(shopRank)) {
			return ERROR_VIEW;
		}
		ShopRank pShopRank = shopRankService.find(shopRank.getId());
		if (pShopRank == null) {
			return ERROR_VIEW;
		}
		if (pShopRank.getIsDefault()) {
			shopRank.setIsDefault(true);
		}
		
		shopRankService.update(shopRank, "shops");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", shopRankService.findPage(pageable));
		return "/admin/shop_rank/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				ShopRank shopRank = shopRankService.find(id);
				if (shopRank != null && shopRank.getShops() != null && !shopRank.getShops().isEmpty()) {
					return Message.error("admin.shopRank.deleteExistNotAllowed", shopRank.getName());
				}
			}
			
			shopRankService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}