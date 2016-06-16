
package com.igomall.controller.wap;

import javax.annotation.Resource;

import com.igomall.service.ProductCategoryService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("wapProductCategoryController")
@RequestMapping("/wap/productCategory")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("rootProductCategories", productCategoryService.findRoots(true));
		return "/wap/tmpl/productCategory/index";
	}

}