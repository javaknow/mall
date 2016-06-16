/*
 * 
 * 
 * 
 */
package com.igomall.controller.admin;

import java.util.Iterator;

import javax.annotation.Resource;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.entity.Attribute;
import com.igomall.entity.Product;
import com.igomall.entity.BaseEntity.Save;
import com.igomall.service.AttributeService;
import com.igomall.service.ProductCategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAttributeController")
@RequestMapping("/admin/attribute")
public class AttributeController extends BaseController {

	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(true));
		model.addAttribute("attributeValuePropertyCount", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT);
		return "/admin/attribute/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Attribute attribute, Long productCategoryId, RedirectAttributes redirectAttributes) {
		for (Iterator<String> iterator = attribute.getOptions().iterator(); iterator.hasNext();) {
			String option = iterator.next();
			if (StringUtils.isEmpty(option)) {
				iterator.remove();
			}
		}
		attribute.setProductCategory(productCategoryService.find(productCategoryId));
		if (!isValid(attribute, Save.class)) {
			return ERROR_VIEW;
		}
		if (attribute.getProductCategory().getAttributes().size() >= Product.ATTRIBUTE_VALUE_PROPERTY_COUNT) {
			addFlashMessage(redirectAttributes, Message.error("admin.attribute.addCountNotAllowed", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT));
		} else {
			attribute.setPropertyIndex(null);
			attributeService.save(attribute);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		}
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(true));
		model.addAttribute("attributeValuePropertyCount", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT);
		model.addAttribute("attribute", attributeService.find(id));
		return "/admin/attribute/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Attribute attribute, RedirectAttributes redirectAttributes) {
		for (Iterator<String> iterator = attribute.getOptions().iterator(); iterator.hasNext();) {
			String option = iterator.next();
			if (StringUtils.isEmpty(option)) {
				iterator.remove();
			}
		}
		if (!isValid(attribute)) {
			return ERROR_VIEW;
		}
		attributeService.update(attribute, "propertyIndex", "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", attributeService.findPage(pageable));
		return "/admin/attribute/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		attributeService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}