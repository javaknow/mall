
package com.igomall.wechat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Order;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;

@Controller("wechatProductCategoryController")
@RequestMapping("/wechat/productCategory")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("title", "商品分类");
		model.addAttribute("productCategories", productCategoryService.findRoots(true));
		return "/wechat/productCategory/list";
	}
	
	@RequestMapping(value="getSecondProductCategroy", method = RequestMethod.GET)
	public String getSecondProductCategroy(ModelMap model,Long parentId) {
		model.addAttribute("title", "商品分类");
		model.addAttribute("productCategories", productCategoryService.findChildren(productCategoryService.find(parentId), true));
		return "/wechat/productCategory/list1";
	}
	
	@RequestMapping(value = "productList", method = RequestMethod.GET)
	public String productList(ModelMap model,Long productCategoryId,Integer first,Integer count,Integer type,Integer val,String orderBy){
		if(type==null){
			type=0;
		}
		if(val==null){
			val=1;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=12;
		}
		if(orderBy==null){
			orderBy = "top_goods_salenum";
		}
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);

		List<Product> products = null;
		List<Order> orders = new ArrayList<Order>();
		if("top_goods_salenum".equals(orderBy)){//销量
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}
		}else if("top_goods_collect".equals(orderBy)){//人气
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}
		}else if("top_store_price".equals(orderBy)){//价格
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.priceAsc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.priceDesc, first, count, null, orders, true);
				
			}
		}else {//默认销量
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}
		}
		model.addAttribute("products", products);
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("type", type);
		model.addAttribute("val",val);
		model.addAttribute("orderBy",orderBy);
		return "/wechat/productCategory/productList";
	}
	
	@RequestMapping(value = "productList1", method = RequestMethod.GET)
	public @ResponseBody List<Product> productList1(ModelMap model,Long productCategoryId,Integer first,Integer count,String orderBy, Integer type,Integer val){
		if(type==null){
			type=0;
		}
		if(val==null){
			val=1;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=12;
		}
		if(orderBy==null){
			orderBy = "top_goods_salenum";
		}
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Product> products = null;
		List<Order> orders = new ArrayList<Order>();
		if("top_goods_salenum".equals(orderBy)){//销量
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}
		}else if("top_goods_collect".equals(orderBy)){//人气
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}
		}else if("top_store_price".equals(orderBy)){//价格
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.priceAsc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.priceDesc, first, count, null, orders, true);
				
			}
		}else {//默认销量
			if(val==0){//升序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, first, count, null, orders, true);
				
			}else{//降序
				products = productService.findList(productCategory, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.salesAsc, first, count, null, orders, true);
				
			}
		}
		return products;
	}

}