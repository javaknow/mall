
package com.igomall.controller.admin;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.Shop;
import com.igomall.entity.ShopAttribute;
import com.igomall.entity.BaseEntity.Save;
import com.igomall.entity.ShopAttribute.Type;
import com.igomall.service.AdminService;
import com.igomall.service.AreaService;
import com.igomall.service.ShopAttributeService;
import com.igomall.service.ShopRankService;
import com.igomall.service.ShopService;

@Controller("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController extends BaseController {

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	@Resource(name = "shopRankServiceImpl")
	private ShopRankService shopRankService;
	@Resource(name = "shopAttributeServiceImpl")
	private ShopAttributeService shopAttributeService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("shopAttributes", shopAttributeService.findList());
		model.addAttribute("shop", shopService.find(id));
		return "/admin/shop/view";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("shopRanks", shopRankService.findAll());
		model.addAttribute("shopAttributes", shopAttributeService.findList());
		return "/admin/shop/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Shop shop, Long shopRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		shop.setShopRank(shopRankService.find(shopRankId));
		if (!isValid(shop, Save.class)) {
			return ERROR_VIEW;
		}
		
		shop.removeAttributeValue();
		for (ShopAttribute shopAttribute : shopAttributeService.findList()) {
			String parameter = request.getParameter("shopAttribute_" + shopAttribute.getId());
			if (shopAttribute.getType() == Type.text || shopAttribute.getType() == Type.select) {
				if (shopAttribute.getIsRequired() && StringUtils.isEmpty(parameter)) {
					return ERROR_VIEW;
				}
				shop.setAttributeValue(shopAttribute, parameter);
			} else if (shopAttribute.getType() == Type.checkbox) {
				String[] parameterValues = request.getParameterValues("shopAttribute_" + shopAttribute.getId());
				List<String> options = parameterValues != null ? Arrays.asList(parameterValues) : null;
				if (shopAttribute.getIsRequired() && (options == null || options.isEmpty())) {
					return ERROR_VIEW;
				}
				shop.setAttributeValue(shopAttribute, options);
			}
		}
		shopService.save(shop, adminService.getCurrent());
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("shopRanks", shopRankService.findAll());
		model.addAttribute("shopAttributes", shopAttributeService.findList());
		model.addAttribute("shop", shopService.find(id));
		return "/admin/shop/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Shop shop, Long shopRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Shop pShop = shopService.find(shop.getId());
		if (pShop == null) {
			return ERROR_VIEW;
		}
		shop.setShopRank(shopRankService.find(shopRankId));
		shop.setMember(pShop.getMember());
		if (!isValid(shop)) {
			return ERROR_VIEW;
		}
		
		
		
		shop.removeAttributeValue();
		for (ShopAttribute shopAttribute : shopAttributeService.findList()) {
			String parameter = request.getParameter("shopAttribute_" + shopAttribute.getId());
			if (shopAttribute.getType() == Type.text || shopAttribute.getType() == Type.select) {
				if (shopAttribute.getIsRequired() && StringUtils.isEmpty(parameter)) {
					return ERROR_VIEW;
				}
				shop.setAttributeValue(shopAttribute, parameter);
			} else if (shopAttribute.getType() == Type.checkbox) {
				String[] parameterValues = request.getParameterValues("shopAttribute_" + shopAttribute.getId());
				List<String> options = parameterValues != null ? Arrays.asList(parameterValues) : null;
				if (shopAttribute.getIsRequired() && (options == null || options.isEmpty())) {
					return ERROR_VIEW;
				}
				shop.setAttributeValue(shopAttribute, options);
			}
		}
		
		BeanUtils.copyProperties(shop, pShop, Shop.NOT_COPY_PROPERTIES);
		shopService.update(pShop, adminService.getCurrent());
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("shopRanks", shopRankService.findAll());
		model.addAttribute("shopAttributes", shopAttributeService.findAll());
		model.addAttribute("page", shopService.findPage(pageable));
		
		return "/admin/shop/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			
			shopService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}