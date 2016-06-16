package com.igomall.controller.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.ResourceNotFoundException;
import com.igomall.entity.Goods;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.Promotion;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.util.DesUtils;

@Controller("shopPromotionController")
@RequestMapping("/promotion")
public class PromotionController extends BaseController {

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		Promotion promotion = promotionService.find(id);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("promotion", promotion);
		return "/shop/promotion/content";
	}

	@RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
	public String index(@PathVariable Long id, ModelMap model) throws Exception {

		Promotion promotion = promotionService.find(id);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		Member member = memberService.getCurrent();
		if(member!=null){
			model.addAllAttributes(DesUtils.encrypt2(member.getId().toString()));
		    model.addAttribute("isLogin", true);
		     
		}
		Set<Product> products = promotion.getProducts();
		List<Product> product1 = new ArrayList<Product>();
		List<Goods> goods = new ArrayList<Goods>();
		for (Product product : products) {
			if(goods.contains(product.getGoods())){
				
			}else{
				goods.add(product.getGoods());
				product1.add(product);
				System.out.println(product.getGoods().getId()+"=================");
			}
		}
		
		model.addAttribute("products", product1);
		model.addAttribute("promotion", promotion);
		System.out.println("====================================================="+promotion.getProducts().size());
		return "/shop/promotion/index";
	}
	
	@RequestMapping(value = "/index/promotionMore", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> promotionMore(Long promotionId, Integer page, ModelMap model) {

		Promotion promotion = promotionService.find(promotionId);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		List<Product> products = new ArrayList<Product>();
		products = productService.findList(promotion, (page-1)*20, 20);
		return products;
	}

}