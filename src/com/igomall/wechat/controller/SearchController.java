
package com.igomall.wechat.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.service.BrandService;
import com.igomall.service.CartService;
import com.igomall.service.MemberService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.SearchService;
import com.igomall.service.TagService;

@Controller("wechatSearchController")
@RequestMapping("/wechat/search")
public class SearchController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String detail(ModelMap model, Long id) throws Exception {
		return "wechat/search/index";
	}
}