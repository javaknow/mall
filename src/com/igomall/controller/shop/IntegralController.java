
package com.igomall.controller.shop;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.entity.IntegralExchangeProduct;
import com.igomall.service.IntegralExchangeProductService;

@Controller("shopIntegralController")
@RequestMapping("/integral")
public class IntegralController extends BaseController {
	
	@Resource(name = "integralExchangeProductServiceImpl")
	private IntegralExchangeProductService integralExchangeProductService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		
		return "/shop/integral/index";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(Long id, ModelMap model) {
		try {
			IntegralExchangeProduct integralExchangeProduct = integralExchangeProductService.find(id);
			model.addAttribute("integralExchangeProduct",integralExchangeProduct);
			model.addAttribute("product",integralExchangeProduct.getProduct());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/shop/integral/detail";
	}
	
}