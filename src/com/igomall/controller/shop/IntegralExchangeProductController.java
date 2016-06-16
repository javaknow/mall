
package com.igomall.controller.shop;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.IntegralExchangeProduct;
import com.igomall.service.IntegralExchangeProductService;

@Controller("shopIntegralExchangeProductController")
@RequestMapping("/integralExchangeProduct")
public class IntegralExchangeProductController extends BaseController {

	@Resource(name = "integralExchangeProductServiceImpl")
	private IntegralExchangeProductService integralExchangeProductService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Page<IntegralExchangeProduct> page = integralExchangeProductService.findPage(pageable);
		model.addAttribute("page",page);
		return "/shop/integral/list";
	}
	
}