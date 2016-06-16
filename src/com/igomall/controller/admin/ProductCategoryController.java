package com.igomall.controller.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.igomall.Message;
import com.igomall.entity.Brand;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.service.BrandService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	/**
	 * 添加产品分类
	 * 
	 * @param model
	 * @param productCategoryId
	 *            上一级分类id
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model, Long productCategoryId) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("productCategoryId", productCategoryId);
		return "/admin/product_category/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}

		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setProducts(null);
		productCategory.setParameterGroups(null);
		productCategory.setAttributes(null);
		productCategory.setPromotions(null);
		productCategoryService.save(productCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(id);
		model.addAttribute("productCategoryTree",
				productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("children",
				productCategoryService.findChildren(productCategory, null));
		return "/admin/product_category/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				return ERROR_VIEW;
			}
			List<ProductCategory> children = productCategoryService .findChildren(parent, null);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		productCategoryService.update(productCategory, "treePath", "grade","children", "products", "parameterGroups", "attributes","promotions");

		//if(productCategory.getIsEnabled()){
			List<ProductCategory> children = productCategoryService.findChildren( productCategory, null);
			for (ProductCategory child : children) {
				child.setIsEnabled(productCategory.getIsEnabled());
				productCategoryService.update(child);
			}
		//}
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model,Boolean isEnabled,Long parentId) {
		if(isEnabled==null){
			isEnabled=true;
		}
		if(parentId==null){
			model.addAttribute("productCategoryTree",productCategoryService.findTree(null,null));
		}else{
			ProductCategory parent = productCategoryService.find(parentId);
			model.addAttribute("productCategoryTree",productCategoryService.findTree(parent, null));
			model.addAttribute("productCategoryId", parentId);
		}
		
		model.addAttribute("isEnabled",isEnabled);
		return "/admin/product_category/list";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		return "/admin/product_category/index";
	}
	
	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String left(ModelMap model) {
		model.addAttribute("productCategoryTree",productCategoryService.findAll());

		return "/admin/product_category/left";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		Set<Product> products = productCategory.getProducts();
		if (products != null && !products.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistProductNotAllowed");
		}
		productCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/operate", method = RequestMethod.POST)
	public @ResponseBody
	Message operate(Long id,Boolean isEnabled) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<Product> products = productCategory.getProducts();
		if (products != null && !products.isEmpty()) {
			products = productCategory.getProducts();
			if (products != null && !products.isEmpty()) {
				for (Product product : products) {
					product.setIsMarketable(isEnabled);
					productService.update(product);
				}
			}else{
				
			}
		}
		
		List<ProductCategory> children = productCategoryService.findChildren(productCategory,null,null);
		if (children != null && !children.isEmpty()) {
			for (ProductCategory child : children) {
				child.setIsEnabled(isEnabled);
				productCategoryService.update(child);
				products = child.getProducts();
				if (products != null && !products.isEmpty()) {
					for (Product product : products) {
						product.setIsMarketable(isEnabled);
					}
				}else{
					
				}
			}
		}
		productCategory.setIsEnabled(isEnabled);
		productCategoryService.update(productCategory);
		return SUCCESS_MESSAGE;
	}
	
	//批量禁用
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public @ResponseBody
	Message close(Long[] ids) {
		List<ProductCategory> productCategorys = productCategoryService.findList(ids);
		for (ProductCategory productCategory : productCategorys) {
			if (productCategory != null) {
				//Set<Product> products = productCategory.getProducts();
				/*if (products != null && !products.isEmpty()) {
					products = productCategory.getProducts();
					if (products != null && !products.isEmpty()) {
						for (Product product : products) {
							product.setIsMarketable(false);
							productService.update(product);
						}
					}else{
						List<ProductCategory> children = productCategoryService.findChildren(productCategory,null,null);
						if (children != null && !children.isEmpty()) {
							for (ProductCategory child : children) {
								child.setIsEnabled(false);
								productCategoryService.update(child);
								products = child.getProducts();
								if (products != null && !products.isEmpty()) {
									for (Product product : products) {
										product.setIsMarketable(false);
									}
								}else{
									
								}
							}
						}
					}
				}*/
				
				productCategory.setIsEnabled(false);
				productCategoryService.update(productCategory);
			}
		}
		return SUCCESS_MESSAGE;
	}
	
	//批量启用
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	public @ResponseBody
	Message open(Long[] ids) {
		List<ProductCategory> productCategorys = productCategoryService.findList(ids);
		for (ProductCategory productCategory : productCategorys) {
			if (productCategory != null) {
				//Set<Product> products = productCategory.getProducts();
				/*if (products != null && !products.isEmpty()) {
					products = productCategory.getProducts();
					if (products != null && !products.isEmpty()) {
						for (Product product : products) {
							product.setIsMarketable(true);
							productService.update(product);
						}
					}else{
						List<ProductCategory> children = productCategoryService.findChildren(productCategory,null,null);
						if (children != null && !children.isEmpty()) {
							for (ProductCategory child : children) {
								child.setIsEnabled(true);
								productCategoryService.update(child);
								products = child.getProducts();
								if (products != null && !products.isEmpty()) {
									for (Product product : products) {
										product.setIsMarketable(true);
									}
								}else{
									
								}
							}
						}
					}
				}*/

				productCategory.setIsEnabled(true);
				productCategoryService.update(productCategory);
			}
		}
		return SUCCESS_MESSAGE;
	}
}