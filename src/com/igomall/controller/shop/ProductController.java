
package com.igomall.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Pageable;
import com.igomall.ResourceNotFoundException;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Tag;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.BrandService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.SearchService;
import com.igomall.service.TagService;

@Controller("shopProductController")
@RequestMapping("/product")
public class ProductController extends BaseController {

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

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody
	List<Product> history(Long[] ids) {
		return productService.findList(ids);
	}

	@RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
	public String list(@PathVariable Long productCategoryId, Long brandId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			throw new ResourceNotFoundException();
		}
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagIds);
		Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
		if (productCategory != null) {
			Set<Attribute> attributes = productCategory.getAttributes();
			for (Attribute attribute : attributes) {
				String value = request.getParameter("attribute_" + attribute.getId());
				if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
					attributeValue.put(attribute, value);
				}
			}
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("brand", brand);
		model.addAttribute("promotion", promotion);
		model.addAttribute("tags", tags);
		model.addAttribute("attributeValue", attributeValue);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("page", productService.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, true, true, null, false, null, null, orderType, pageable));
		return "/shop/product/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long brandId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request, ModelMap model) {
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagIds);
		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("brand", brand);
		model.addAttribute("promotion", promotion);
		model.addAttribute("tags", tags);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("page", productService.findPage(null, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, orderType, pageable));
		return "/shop/product/list";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, ModelMap model) {
		if (StringUtils.isEmpty(keyword)) {
			return ERROR_VIEW;
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("productKeyword", keyword);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("page", searchService.search(keyword, startPrice, endPrice, orderType, pageable));
		return "shop/product/search";
	}

	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Long hits(@PathVariable Long id) {
		return productService.viewHits(id);
	}
	
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String search(ModelMap model, Long id) {
		model.addAttribute("product", productService.find(id));
		return "shop/product/detail";
	}
	
	@RequestMapping(value = "/content", method = RequestMethod.GET)
	public String content(ModelMap model, Long id,String key,String result,Integer times) throws Exception {
		model.addAttribute("product", productService.find(id));
		model.addAttribute("result", result==null?0:result);
		model.addAttribute("key",  key==null?0:key);
		model.addAttribute("times", times==null?0:times);
		return "shop/product/content";
	}
	
	@RequestMapping(value = "/promotion", method = RequestMethod.GET)
	public String promotion(ModelMap model, Long id,String key,String result,Integer times) throws Exception {
		model.addAttribute("product", productService.find(id));
		model.addAttribute("result", result==null?0:result);
		model.addAttribute("key",  key==null?0:key);
		model.addAttribute("times", times==null?0:times);
		return "shop/product/promotion";
	}
	
	/************************************************************************************************/
	@RequestMapping(value = "/content1", method = RequestMethod.GET)
	public String content1(ModelMap model, Long id) throws Exception {
		model.addAttribute("product", productService.find(id));
		return "shop/product/content1";
	}

}