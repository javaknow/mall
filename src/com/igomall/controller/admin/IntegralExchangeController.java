
package com.igomall.controller.admin;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.IntegralExchange;
import com.igomall.entity.IntegralExchangeProduct;
import com.igomall.entity.Product;
import com.igomall.service.IntegralExchangeProductService;
import com.igomall.service.IntegralExchangeService;
import com.igomall.service.ProductService;

@Controller("adminIntegralExchangeController")
@RequestMapping("/admin/integralExchange")
public class IntegralExchangeController extends BaseController {

	@Resource(name = "integralExchangeServiceImpl")
	private IntegralExchangeService integralExchangeService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "integralExchangeProductServiceImpl")
	private IntegralExchangeProductService integralExchangeProductService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/integralExchange/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(IntegralExchange integralExchange, RedirectAttributes redirectAttributes) {
		for (Iterator<IntegralExchangeProduct> iterator = integralExchange.getIntegralExchangeProducts().iterator(); iterator.hasNext();) {
			IntegralExchangeProduct integralExchangeProduct = iterator.next();
			if (integralExchangeProduct == null||integralExchangeProduct.getProductId()==null) {
				iterator.remove();
			} else {
				integralExchangeProduct.setProduct(productService.find(integralExchangeProduct.getProductId()));
				integralExchangeProduct.setIntegralExchange(integralExchange);
			}
		}
		if (!isValid(integralExchange)) {
			return ERROR_VIEW;
		}
		integralExchangeService.save(integralExchange);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("integralExchange", integralExchangeService.find(id));
		return "/admin/integralExchange/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(IntegralExchange integralExchange,  RedirectAttributes redirectAttributes) {
		for (Iterator<IntegralExchangeProduct> iterator = integralExchange.getIntegralExchangeProducts().iterator(); iterator.hasNext();) {
			IntegralExchangeProduct integralExchangeProduct = iterator.next();
			if (integralExchangeProduct == null) {
				iterator.remove();
			} else {
				integralExchangeProduct.setIntegralExchange(integralExchange);
			}
		}
		if (!isValid(integralExchange)) {
			return ERROR_VIEW;
		}
		integralExchangeService.update(integralExchange);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Page<IntegralExchangeProduct> page = integralExchangeProductService.findPage(pageable);
		model.addAttribute("page",page);
		return "/admin/integralExchange/list";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		integralExchangeService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "search",method=RequestMethod.POST)
	public String search(ModelMap model,String stockMemo){
		List<Product> products = productService.search(stockMemo, null, 10);
		model.addAttribute("products",products);
		return "/admin/IntegralExchange/search";
	}

}