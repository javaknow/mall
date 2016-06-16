
package com.igomall.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.Message;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.FileInfo.FileType;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Goods;
import com.igomall.entity.MemberRank;
import com.igomall.entity.Parameter;
import com.igomall.entity.ParameterGroup;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.ProductImage;
import com.igomall.entity.Promotion;
import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;
import com.igomall.entity.Tag;
import com.igomall.entity.Product.OrderType;
import com.igomall.entity.Tag.Type;
import com.igomall.service.BrandService;
import com.igomall.service.FileService;
import com.igomall.service.GoodsService;
import com.igomall.service.MemberRankService;
import com.igomall.service.ParseCSVService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductImageService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.ShopService;
import com.igomall.service.SpecificationService;
import com.igomall.service.SpecificationValueService;
import com.igomall.service.TagService;
import com.igomall.util.Parse1688Product;
import com.igomall.util.SettingUtils;

@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name = "parseCSVServiceImpl")
	private ParseCSVService parseCSVService;
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		if (productService.snUnique(previousSn, sn)) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody
	Set<ParameterGroup> parameterGroups(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getParameterGroups();
	}

	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody
	Set<Attribute> attributes(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getAttributes();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model,Long productCategoryId) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("productCategoryId",productCategoryId);
		return "/admin/product/add";
	}
	
	/**
	 * 导入淘宝数据包
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importTaobao", method = RequestMethod.GET)
	public String importTaobao(ModelMap model,Long productCategoryId) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("productCategoryId",productCategoryId);
		return "/admin/product/importTaobao";
	}
	
	/**
	 * 导入阿里巴巴数据包
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importAliBaBa", method = RequestMethod.GET)
	public String importAliBaBa(ModelMap model,Long productCategoryId) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("productCategoryId",productCategoryId);
		return "/admin/product/importAliBaBa";
	}
	
	/**
	 * 导入阿里巴巴产品
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/importAliBaBaProduct", method = RequestMethod.GET)
	public String importAliBaBaProduct(ModelMap model,Long productCategoryId) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(null));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("productCategoryId",productCategoryId);
		return "/admin/product/importAliBaBaProduct";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Product product, Long productCategoryId, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
			if (productImage.getFile() != null && !productImage.getFile().isEmpty()) {
				if (!fileService.isValid(FileType.image, productImage.getFile())) {
					addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
					return "redirect:add.jhtml";
				}
			}
		}
		
		product.setShop(shopService.find(1L));
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.getProductCategory().setIsEnabled(true);
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(product)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
			return ERROR_VIEW;
		}
		if (product.getMarketPrice() == null) {
			BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getPoint() == null) {
			long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}
		
		
		product.setFullName(null);
		product.setAllocatedStock(0);
		product.setScore(0F);
		product.setTotalScore(0L);
		product.setScoreCount(0L);
		product.setHits(0L);
		product.setWeekHits(0L);
		product.setMonthHits(0L);
		product.setSales(0L);
		product.setWeekSales(0L);
		product.setMonthSales(0L);
		product.setWeekHitsDate(new Date());
		product.setMonthHitsDate(new Date());
		product.setWeekSalesDate(new Date());
		product.setMonthSalesDate(new Date());
		product.setReviews(null);
		product.setConsultations(null);
		product.setFavoriteMembers(null);
		product.setPromotions(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setGiftItems(null);
		product.setProductNotifies(null);
		product.setIsParent(true);

		for (MemberRank memberRank : memberRankService.findAll()) {
			String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			} else {
				product.getMemberPrice().remove(memberRank);
			}
		}

		for (ProductImage productImage : product.getProductImages()) {
			productImageService.build(productImage);
		}
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

		for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
			for (Parameter parameter : parameterGroup.getParameters()) {
				String parameterValue = request.getParameter("parameter_" + parameter.getId());
				if (StringUtils.isNotEmpty(parameterValue)) {
					product.getParameterValue().put(parameter, parameterValue);
				} else {
					product.getParameterValue().remove(parameter);
				}
			}
		}

		for (Attribute attribute : product.getProductCategory().getAttributes()) {
			String attributeValue = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(attributeValue)) {
				product.setAttributeValue(attribute, attributeValue);
			} else {
				product.setAttributeValue(attribute, null);
			}
		}

		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = specificationService.find(specificationIds[i]);
				String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0) {
					for (int j = 0; j < specificationValueIds.length; j++) {
						if (i == 0) {
							if (j == 0) {
								product.setGoods(goods);
								product.setSpecifications(new HashSet<Specification>());
								product.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(product);
							} else {
								Product specificationProduct = new Product();
								BeanUtils.copyProperties(product, specificationProduct);
								specificationProduct.setShop(shopService.find(1L));
								specificationProduct.setId(null);
								specificationProduct.setCreateDate(null);
								specificationProduct.setModifyDate(null);
								specificationProduct.setSn(null);
								specificationProduct.setFullName(null);
								specificationProduct.setAllocatedStock(0);
								specificationProduct.setIsList(false);
								specificationProduct.setScore(0F);
								specificationProduct.setTotalScore(0L);
								specificationProduct.setScoreCount(0L);
								specificationProduct.setHits(0L);
								specificationProduct.setWeekHits(0L);
								specificationProduct.setMonthHits(0L);
								specificationProduct.setSales(0L);
								specificationProduct.setWeekSales(0L);
								specificationProduct.setMonthSales(0L);
								specificationProduct.setWeekHitsDate(new Date());
								specificationProduct.setMonthHitsDate(new Date());
								specificationProduct.setWeekSalesDate(new Date());
								specificationProduct.setMonthSalesDate(new Date());
								specificationProduct.setGoods(goods);
								specificationProduct.setReviews(null);
								specificationProduct.setConsultations(null);
								specificationProduct.setFavoriteMembers(null);
								specificationProduct.setSpecifications(new HashSet<Specification>());
								specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								specificationProduct.setPromotions(null);
								specificationProduct.setCartItems(null);
								specificationProduct.setOrderItems(null);
								specificationProduct.setGiftItems(null);
								specificationProduct.setProductNotifies(null);
								specificationProduct.setIsParent(false);
								products.add(specificationProduct);
							}
						}
						Product specificationProduct = products.get(j);
						SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		} else {
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.save(goods);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?isMarketable=true";
	}
	
	@RequestMapping(value = "/saveImportTaobao", method = RequestMethod.POST)
	public String saveImportTaobao(Long productCategoryId,String proxyShop, String shopUrl,BigDecimal price1,BigDecimal marketPrice1, Long brandId, Long[] tagIds,Boolean isGift,Boolean isTop,Boolean isList,Boolean isMarketable,String file,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		//解析淘宝数据
		List<Product> list = parseCSVService.parseTaobao(file);
		
		for (Product product : list) {
			
			product.setProductCategory(productCategoryService.find(productCategoryId));
			product.getProductCategory().setIsEnabled(true);
			product.setBrand(brandService.find(brandId));
			product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
			
			if(isGift==null){
				isGift=false;
			}
			if(isList==null){
				isList=false;
			}
			if(isMarketable==null){
				isMarketable=false;
			}
			if(isTop==null){
				isTop=false;
			}
			product.setShop(shopService.find(1L));
			product.setPrice(product.getCost().multiply(price1));
			product.setMarketPrice(product.getCost().multiply(marketPrice1));
			product.setProxyShop(proxyShop);
			product.setShopUrl(shopUrl);
			product.setIsGift(isGift);
			product.setIsList(isList);
			product.setIsMarketable(isMarketable);
			product.setIsTop(isTop);
			product.setIsParent(true);
			if (!isValid(product)) {
				return ERROR_VIEW;
			}
			if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
				return ERROR_VIEW;
			}
			if (product.getMarketPrice() == null) {
				BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
				product.setMarketPrice(defaultMarketPrice);
			}
			if (product.getPoint() == null) {
				long point = calculateDefaultPoint(product.getPrice());
				product.setPoint(point);
			}
			
			product.setFullName(null);
			product.setAllocatedStock(0);
			product.setScore(0F);
			product.setTotalScore(0L);
			product.setScoreCount(0L);
			product.setHits(0L);
			product.setWeekHits(0L);
			product.setMonthHits(0L);
			product.setSales(0L);
			product.setWeekSales(0L);
			product.setMonthSales(0L);
			product.setWeekHitsDate(new Date());
			product.setMonthHitsDate(new Date());
			product.setWeekSalesDate(new Date());
			product.setMonthSalesDate(new Date());
			product.setReviews(null);
			product.setConsultations(null);
			product.setFavoriteMembers(null);
			product.setPromotions(null);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setGiftItems(null);
			product.setProductNotifies(null);

			for (MemberRank memberRank : memberRankService.findAll()) {
				String price = request.getParameter("memberPrice_" + memberRank.getId());
				if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
					product.getMemberPrice().put(memberRank, new BigDecimal(price));
				} else {
					product.getMemberPrice().remove(memberRank);
				}
			}

			for (ProductImage productImage : product.getProductImages()) {
				
				productImageService.build(productImage);
			}
			Collections.sort(product.getProductImages());
			if (product.getImage() == null && product.getThumbnail() != null) {
				product.setImage(product.getThumbnail());
			}

			for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
				for (Parameter parameter : parameterGroup.getParameters()) {
					String parameterValue = request.getParameter("parameter_" + parameter.getId());
					if (StringUtils.isNotEmpty(parameterValue)) {
						product.getParameterValue().put(parameter, parameterValue);
					} else {
						product.getParameterValue().remove(parameter);
					}
				}
			}

			for (Attribute attribute : product.getProductCategory().getAttributes()) {
				String attributeValue = request.getParameter("attribute_" + attribute.getId());
				if (StringUtils.isNotEmpty(attributeValue)) {
					product.setAttributeValue(attribute, attributeValue);
				} else {
					product.setAttributeValue(attribute, null);
				}
			}

			Goods goods = new Goods();
			List<Product> products = new ArrayList<Product>();
			
			Map<Specification,List<SpecificationValue>> map = parseSpecificationId(product);
			
			
			Set<Specification> set = map.keySet();
			Iterator<Specification> iterator = set.iterator();
			List<Specification> specifications = new ArrayList<Specification>();
			while(iterator.hasNext()){
				specifications.add(iterator.next());
			}
			
			if (specifications != null && specifications.size() > 0) {
				for (int i = 0; i < specifications.size(); i++) {
					Specification specification = specifications.get(i);
					List<SpecificationValue> specificationValues = map.get(specification);
					if (specificationValues != null && specificationValues.size() > 0) {
						for (int j = 0; j < specificationValues.size(); j++) {
							
							if (i == 0) {
								if (j == 0) {
									product.setGoods(goods);
									product.setSpecifications(new HashSet<Specification>());
									product.setSpecificationValues(new HashSet<SpecificationValue>());
									products.add(product);
								} else {
									Product specificationProduct = new Product();
									BeanUtils.copyProperties(product, specificationProduct);
									specificationProduct.setShop(shopService.find(1L));
									specificationProduct.setId(null);
									specificationProduct.setCreateDate(null);
									specificationProduct.setModifyDate(null);
									specificationProduct.setSn(null);
									specificationProduct.setFullName(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
									specificationProduct.setScore(0F);
									specificationProduct.setTotalScore(0L);
									specificationProduct.setScoreCount(0L);
									specificationProduct.setHits(0L);
									specificationProduct.setWeekHits(0L);
									specificationProduct.setMonthHits(0L);
									specificationProduct.setSales(0L);
									specificationProduct.setWeekSales(0L);
									specificationProduct.setMonthSales(0L);
									specificationProduct.setWeekHitsDate(new Date());
									specificationProduct.setMonthHitsDate(new Date());
									specificationProduct.setWeekSalesDate(new Date());
									specificationProduct.setMonthSalesDate(new Date());
									specificationProduct.setGoods(goods);
									specificationProduct.setReviews(null);
									specificationProduct.setConsultations(null);
									specificationProduct.setFavoriteMembers(null);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									specificationProduct.setProductNotifies(null);
									specificationProduct.setIsParent(false);
									products.add(specificationProduct);
									
								}
							}
							Product specificationProduct = products.get(j);
							SpecificationValue specificationValue = specificationValues.get(j);
							specificationProduct.getSpecifications().add(specification);
							specificationProduct.getSpecificationValues().add(specificationValue);
						}
					}
				}
			} else {
				product.setGoods(goods);
				product.setSpecifications(null);
				product.setSpecificationValues(null);
				products.add(product);
			}
			goods.getProducts().clear();
			goods.getProducts().addAll(products);
			goodsService.save(goods);
		}
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?isMarketable=true";
	}

	/**
	 * 1627207:3232483:藏青色小鹿;
	 * 1627207:3232484:藏青色鬼头;
	 * 1627207:3232481:灰色鬼头;
	 * 1627207:90554:藏青色米字;
	 * 1627207:28332:灰色小鹿;
	 * 1627207:30156:灰色米字;
	 * 1627207:60092:黑色小鹿
	 * 
	 * 
	 * 94.8:23577:0:1627207:3232483;
	 * 20509:28383;
	 * 94.8:23646:0:1627207:3232484;
	 * 20509:28383;
	 * 94.8:23647:0:1627207:3232481;
	 * 20509:28383;
	 * 94.8:23641:0:1627207:90554;
	 * 20509:28383;
	 * 94.8:23647:0:1627207:28332;
	 * 20509:28383;
	 * 94.8:23645:0:1627207:30156;
	 * 20509:28383;
	 * 94.8:23637:0:1627207:60092;
	 * 20509:28383;
	 * @param product
	 * @return
	 */
	public Map<Specification,List<SpecificationValue>> parseSpecificationId(Product product) {
		Map<Specification,List<SpecificationValue>> map = new HashMap<Specification,List<SpecificationValue>>();
		
		
		String skuPropses = product.getSkuProps();
		
		//Map<规则，规则值的集合>
		Map<String,List<String>> map1 = Parse1688Product.parseSpecification(skuPropses);
		
			
		Set<String> specificationStrSet = map1.keySet();//规格
		Long taobaoId=0L;
		
		for (String specificationStr : specificationStrSet) {
			taobaoId = Long.parseLong(specificationStr);
			Specification specification = specificationService.findByTaoBaoId(taobaoId);
			//规格不存在的时候 就需要创建一个新的规格
			if(specification==null&&taobaoId>0){
				specification = specificationService.createSpecificationTaoBaoId(taobaoId);
				map.put(specification, new ArrayList<SpecificationValue>());
			}
			
			//规格值
			List<String> specificationValueStrList = map1.get(specificationStr);
			SpecificationValue specificationValue = null;
			
			for (String string : specificationValueStrList) {
				taobaoId=Long.parseLong(string);
				
				specificationValue = specificationValueService.findByTaoBaoId(specification,taobaoId);
				//规格值不存在的时候 就需要创建一个新的规格值
				if(specificationValue==null&&taobaoId>0&&specification!=null){
					specificationValue = new SpecificationValue();
					//创建规格值
					specificationValue.setName(taobaoId+"");
					specificationValue.setTaobaoId(taobaoId);
					specificationValue.setImage(null);
					specificationValue.setSpecification(specification);
					specificationValueService.save(specificationValue);
				}
				
				if (specification!=null) {
					if (map.containsKey(specification)) {

					} else {
						map.put(specification,new ArrayList<SpecificationValue>());
					}
					
					map.get(specification).add(specificationValue);
				}
			}
		}
		
		map = dealMap(map);
		
		
		return map;
	}

	private Map<Specification, List<SpecificationValue>> dealMap(Map<Specification, List<SpecificationValue>> map) {
		Map<Specification, List<SpecificationValue>> map1 = new HashMap<Specification, List<SpecificationValue>>();
		Iterator<Specification> iterator = map.keySet().iterator();
		Integer size = 1;
		while(iterator.hasNext()){
			Specification specification = iterator.next();
			
			size = size * (map.get(specification).size()==0?1:map.get(specification).size());
		}
		
		iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			Specification specification = iterator.next();
			map1.put(specification, new ArrayList<SpecificationValue>());
			
			List<SpecificationValue> specificationValues = map.get(specification);
			Integer times = size/(specificationValues.size()==0?1:specificationValues.size());
			while(times>0){
				map1.get(specification).addAll(specificationValues);
				times--;
			}
		}
		return map1;
	}

	@RequestMapping(value = "/saveImportAliBaBa", method = RequestMethod.POST)
	public String saveImportAliBaBa(Long productCategoryId,String proxyShop, String shopUrl, BigDecimal price1,BigDecimal marketPrice1, Long brandId, Long[] tagIds,Boolean isGift,Boolean isTop,Boolean isList,Boolean isMarketable,String file,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		//解析淘宝数据
		List<Product> list = parseCSVService.parseAliBaBa(file);
		
		for (Product product : list) {
			product.setProductCategory(productCategoryService.find(productCategoryId));
			product.getProductCategory().setIsEnabled(true);
			product.setBrand(brandService.find(brandId));
			product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
			
			if(isGift==null){
				isGift=false;
			}
			if(isList==null){
				isList=false;
			}
			if(isMarketable==null){
				isMarketable=false;
			}
			if(isTop==null){
				isTop=false;
			}
			
			product.setShop(shopService.find(1L));
			product.setPrice(product.getCost().multiply(price1));
			product.setMarketPrice(product.getCost().multiply(marketPrice1));
			product.setProxyShop(proxyShop);
			product.setShopUrl(shopUrl);
			product.setIsGift(isGift);
			product.setIsList(isList);
			product.setIsMarketable(isMarketable);
			product.setIsTop(isTop);
			product.setIsParent(true);
			if (!isValid(product)) {
				return ERROR_VIEW;
			}
			if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
				return ERROR_VIEW;
			}
			if (product.getMarketPrice() == null) {
				BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
				product.setMarketPrice(defaultMarketPrice);
			}
			if (product.getPoint() == null) {
				long point = calculateDefaultPoint(product.getPrice());
				product.setPoint(point);
			}
			
			product.setFullName(null);
			product.setAllocatedStock(0);
			product.setScore(0F);
			product.setTotalScore(0L);
			product.setScoreCount(0L);
			product.setHits(0L);
			product.setWeekHits(0L);
			product.setMonthHits(0L);
			product.setSales(0L);
			product.setWeekSales(0L);
			product.setMonthSales(0L);
			product.setWeekHitsDate(new Date());
			product.setMonthHitsDate(new Date());
			product.setWeekSalesDate(new Date());
			product.setMonthSalesDate(new Date());
			product.setReviews(null);
			product.setConsultations(null);
			product.setFavoriteMembers(null);
			product.setPromotions(null);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setGiftItems(null);
			product.setProductNotifies(null);

			for (MemberRank memberRank : memberRankService.findAll()) {
				String price = request.getParameter("memberPrice_" + memberRank.getId());
				if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
					product.getMemberPrice().put(memberRank, new BigDecimal(price));
				} else {
					product.getMemberPrice().remove(memberRank);
				}
			}

			for (ProductImage productImage : product.getProductImages()) {
				productImageService.build(productImage);
			}
			Collections.sort(product.getProductImages());
			if (product.getImage() == null && product.getThumbnail() != null) {
				product.setImage(product.getThumbnail());
			}

			for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
				for (Parameter parameter : parameterGroup.getParameters()) {
					String parameterValue = request.getParameter("parameter_" + parameter.getId());
					if (StringUtils.isNotEmpty(parameterValue)) {
						product.getParameterValue().put(parameter, parameterValue);
					} else {
						product.getParameterValue().remove(parameter);
					}
				}
			}

			for (Attribute attribute : product.getProductCategory().getAttributes()) {
				String attributeValue = request.getParameter("attribute_" + attribute.getId());
				if (StringUtils.isNotEmpty(attributeValue)) {
					product.setAttributeValue(attribute, attributeValue);
				} else {
					product.setAttributeValue(attribute, null);
				}
			}

			
			
			Goods goods = new Goods();
			List<Product> products = new ArrayList<Product>();
			
			Map<Specification,List<SpecificationValue>> map = parseSpecificationId(product);
			Set<Specification> set = map.keySet();
			Iterator<Specification> iterator = set.iterator();
			List<Specification> specifications = new ArrayList<Specification>();
			while(iterator.hasNext()){
				specifications.add(iterator.next());
			}
			
			if (specifications != null && specifications.size() > 0) {
				for (int i = 0; i < specifications.size(); i++) {
					Specification specification = specifications.get(i);
					List<SpecificationValue> specificationValues = map.get(specification);
					if (specificationValues != null && specificationValues.size() > 0) {
						for (int j = 0; j < specificationValues.size(); j++) {
							if (i == 0) {
								if (j == 0) {
									product.setGoods(goods);
									product.setSpecifications(new HashSet<Specification>());
									product.setSpecificationValues(new HashSet<SpecificationValue>());
									products.add(product);
								} else {
									Product specificationProduct = new Product();
									BeanUtils.copyProperties(product, specificationProduct);
									specificationProduct.setShop(shopService.find(1L));
									specificationProduct.setId(null);
									specificationProduct.setCreateDate(null);
									specificationProduct.setModifyDate(null);
									specificationProduct.setSn(null);
									specificationProduct.setFullName(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
									specificationProduct.setScore(0F);
									specificationProduct.setTotalScore(0L);
									specificationProduct.setScoreCount(0L);
									specificationProduct.setHits(0L);
									specificationProduct.setWeekHits(0L);
									specificationProduct.setMonthHits(0L);
									specificationProduct.setSales(0L);
									specificationProduct.setWeekSales(0L);
									specificationProduct.setMonthSales(0L);
									specificationProduct.setWeekHitsDate(new Date());
									specificationProduct.setMonthHitsDate(new Date());
									specificationProduct.setWeekSalesDate(new Date());
									specificationProduct.setMonthSalesDate(new Date());
									specificationProduct.setGoods(goods);
									specificationProduct.setReviews(null);
									specificationProduct.setConsultations(null);
									specificationProduct.setFavoriteMembers(null);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									specificationProduct.setProductNotifies(null);
									specificationProduct.setIsParent(false);
									products.add(specificationProduct);
								}
							}
							Product specificationProduct = products.get(j);
							SpecificationValue specificationValue = specificationValues.get(j);
							specificationProduct.getSpecifications().add(specification);
							specificationProduct.getSpecificationValues().add(specificationValue);

							
						}
					}
				}
			} else {
				product.setGoods(goods);
				product.setSpecifications(null);
				product.setSpecificationValues(null);
				products.add(product);
			}
			goods.getProducts().clear();
			goods.getProducts().addAll(products);
			goodsService.save(goods);
			
		}
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?isMarketable=true";
	}
	
	
	@RequestMapping(value = "/saveImportAliBaBaProduct", method = RequestMethod.POST)
	public String saveImportAliBaBaProduct(Product product, String msg, Long productCategoryId,String proxyShop, String shopUrl,BigDecimal price1,BigDecimal marketPrice1, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		//String msg = HttpUtils.getRequest(product.getOriginalUrl(),"gbk");	
		//解析产品名字
		String name = Parse1688Product.getTitle(msg);
		product.setName(name);
		product.setFullName(name);
		product.setSeoTitle(name);
		
		//解析价格
		name = Parse1688Product.getPrice(msg);
		product.setCost(new BigDecimal(name));
		product.setPrice(product.getCost().multiply(price1));
		product.setMarketPrice(product.getCost().multiply(marketPrice1));
		product.setPoint(0L);
		
		//解析关键字
		name = Parse1688Product.getKeyWords(msg);
		product.setKeyword(name);
		product.setSeoKeywords(name);
		
		
		//解析描述
		//name = Parse1688Product.getDescription(msg);
		//product.setSeoDescription(name);
		
		//产品详情
		name = Parse1688Product.getContent(msg);
		product.setIntroduction(name);
	
		//解析产品图片
		List<String> productImageStr = Parse1688Product.getPic(msg);
		
		for (String string : productImageStr) {
			ProductImage productImage = new ProductImage();
			productImage.setLarge(string);
			productImage.setMedium(string);
			productImage.setSource(string);
			productImage.setThumbnail(string);
			productImage.setTitle(product.getName());
			product.getProductImages().add(productImage);
			product.setImage(productImageStr.get(0));
		}
		
		product.setShop(shopService.find(1L));
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.getProductCategory().setIsEnabled(true);
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(product)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
			return ERROR_VIEW;
		}
		if (product.getMarketPrice() == null) {
			BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getPoint() == null) {
			long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}
		
		product.setPrice(product.getCost().multiply(price1));
		product.setMarketPrice(product.getCost().multiply(marketPrice1));
		product.setProxyShop(proxyShop);
		product.setShopUrl(shopUrl);
		product.setWeight(0.0);
		product.setAllocatedStock(0);
		product.setScore(0F);
		product.setTotalScore(0L);
		product.setScoreCount(0L);
		product.setHits(0L);
		product.setWeekHits(0L);
		product.setMonthHits(0L);
		product.setSales(0L);
		product.setWeekSales(0L);
		product.setMonthSales(0L);
		product.setWeekHitsDate(new Date());
		product.setMonthHitsDate(new Date());
		product.setWeekSalesDate(new Date());
		product.setMonthSalesDate(new Date());
		product.setReviews(null);
		product.setConsultations(null);
		product.setFavoriteMembers(null);
		product.setPromotions(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setGiftItems(null);
		product.setProductNotifies(null);
		product.setIsParent(true);
		for (MemberRank memberRank : memberRankService.findAll()) {
			String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			} else {
				product.getMemberPrice().remove(memberRank);
			}
		}

		
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

		for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
			for (Parameter parameter : parameterGroup.getParameters()) {
				String parameterValue = request.getParameter("parameter_" + parameter.getId());
				if (StringUtils.isNotEmpty(parameterValue)) {
					product.getParameterValue().put(parameter, parameterValue);
				} else {
					product.getParameterValue().remove(parameter);
				}
			}
		}

		for (Attribute attribute : product.getProductCategory().getAttributes()) {
			String attributeValue = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(attributeValue)) {
				product.setAttributeValue(attribute, attributeValue);
			} else {
				product.setAttributeValue(attribute, null);
			}
		}

		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = specificationService.find(specificationIds[i]);
				String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0) {
					for (int j = 0; j < specificationValueIds.length; j++) {
						if (i == 0) {
							if (j == 0) {
								product.setGoods(goods);
								product.setSpecifications(new HashSet<Specification>());
								product.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(product);
							} else {
								Product specificationProduct = new Product();
								BeanUtils.copyProperties(product, specificationProduct);
								specificationProduct.setShop(shopService.find(1L));
								specificationProduct.setId(null);
								specificationProduct.setCreateDate(null);
								specificationProduct.setModifyDate(null);
								specificationProduct.setSn(null);
								specificationProduct.setFullName(null);
								specificationProduct.setAllocatedStock(0);
								specificationProduct.setIsList(false);
								specificationProduct.setScore(0F);
								specificationProduct.setTotalScore(0L);
								specificationProduct.setScoreCount(0L);
								specificationProduct.setHits(0L);
								specificationProduct.setWeekHits(0L);
								specificationProduct.setMonthHits(0L);
								specificationProduct.setSales(0L);
								specificationProduct.setWeekSales(0L);
								specificationProduct.setMonthSales(0L);
								specificationProduct.setWeekHitsDate(new Date());
								specificationProduct.setMonthHitsDate(new Date());
								specificationProduct.setWeekSalesDate(new Date());
								specificationProduct.setMonthSalesDate(new Date());
								specificationProduct.setGoods(goods);
								specificationProduct.setReviews(null);
								specificationProduct.setConsultations(null);
								specificationProduct.setFavoriteMembers(null);
								specificationProduct.setSpecifications(new HashSet<Specification>());
								specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								specificationProduct.setPromotions(null);
								specificationProduct.setCartItems(null);
								specificationProduct.setOrderItems(null);
								specificationProduct.setGiftItems(null);
								specificationProduct.setProductNotifies(null);
								products.add(specificationProduct);
							}
						}
						Product specificationProduct = products.get(j);
						SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		} else {
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.save(goods);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?isMarketable=true";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree(true));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", productService.find(id));
		return "/admin/product/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Product product, Long productCategoryId, Long brandId, Long[] tagIds, Long[] specificationIds, Long[] specificationProductIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
			if (productImage.getFile() != null && !productImage.getFile().isEmpty()) {
				if (!fileService.isValid(FileType.image, productImage.getFile())) {
					addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
					return "redirect:edit.jhtml?id=" + product.getId();
				}
			}
		}
		product.setIsParent(true);
		product.setShop(shopService.find(1L));
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(product)) {
			return ERROR_VIEW;
		}
		Product pProduct = productService.find(product.getId());
		if (pProduct == null) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn())) {
			return ERROR_VIEW;
		}
		if (product.getMarketPrice() == null) {
			BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getPoint() == null) {
			long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}

		for (MemberRank memberRank : memberRankService.findAll()) {
			String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			} else {
				product.getMemberPrice().remove(memberRank);
			}
		}

		for (ProductImage productImage : product.getProductImages()) {
			productImageService.build(productImage);
		}
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

		for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
			for (Parameter parameter : parameterGroup.getParameters()) {
				String parameterValue = request.getParameter("parameter_" + parameter.getId());
				if (StringUtils.isNotEmpty(parameterValue)) {
					product.getParameterValue().put(parameter, parameterValue);
				} else {
					product.getParameterValue().remove(parameter);
				}
			}
		}

		for (Attribute attribute : product.getProductCategory().getAttributes()) {
			String attributeValue = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(attributeValue)) {
				product.setAttributeValue(attribute, attributeValue);
			} else {
				product.setAttributeValue(attribute, null);
			}
		}

		Goods goods = pProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = specificationService.find(specificationIds[i]);
				String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0) {
					for (int j = 0; j < specificationValueIds.length; j++) {
						if (i == 0) {
							if (j == 0) {
								BeanUtils.copyProperties(product, pProduct, new String[] { "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers",
										"specifications", "specificationValues", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies" });
								pProduct.setSpecifications(new HashSet<Specification>());
								pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(pProduct);
							} else {
								if (specificationProductIds != null && j < specificationProductIds.length) {
									Product specificationProduct = productService.find(specificationProductIds[j]);
									if (specificationProduct == null || (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods))) {
										return ERROR_VIEW;
									}
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									products.add(specificationProduct);
								} else {
									Product specificationProduct = new Product();
									BeanUtils.copyProperties(product, specificationProduct);
									specificationProduct.setId(null);
									specificationProduct.setCreateDate(null);
									specificationProduct.setModifyDate(null);
									specificationProduct.setSn(null);
									specificationProduct.setFullName(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
									specificationProduct.setScore(0F);
									specificationProduct.setTotalScore(0L);
									specificationProduct.setScoreCount(0L);
									specificationProduct.setHits(0L);
									specificationProduct.setWeekHits(0L);
									specificationProduct.setMonthHits(0L);
									specificationProduct.setSales(0L);
									specificationProduct.setWeekSales(0L);
									specificationProduct.setMonthSales(0L);
									specificationProduct.setWeekHitsDate(new Date());
									specificationProduct.setMonthHitsDate(new Date());
									specificationProduct.setWeekSalesDate(new Date());
									specificationProduct.setMonthSalesDate(new Date());
									specificationProduct.setGoods(goods);
									specificationProduct.setReviews(null);
									specificationProduct.setConsultations(null);
									specificationProduct.setFavoriteMembers(null);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									specificationProduct.setProductNotifies(null);
									specificationProduct.setIsParent(false);
									products.add(specificationProduct);
								}
							}
						}
						Product specificationProduct = products.get(j);
						SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		} else {
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			BeanUtils.copyProperties(product, pProduct, new String[] { "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems",
					"orderItems", "giftItems", "productNotifies" });
			products.add(pProduct);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.update(goods);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?isMarketable=true";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		if(isMarketable==null){
			isMarketable=true;
		}
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagId);
		model.addAttribute("productCategoryTree", productCategoryService.findTree(true));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("productCategoryId",productCategoryId);
		model.addAttribute("page", productService.findPage(productCategory, brand, promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable));
		
		/*List<Goods> goods = goodsService.findAll();
		for (Goods goods2 : goods) {
			Set<Product> products = goods2.getProducts();
			Product product = products.iterator().next();
			if(product.getIsParent()==null){
				product.setIsParent(true);
				productService.update(product);
				System.out.println("===="+product.getId());
			}
		}*/
		
		return "/admin/product/list";
	}
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		isMarketable=false;
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagId);
		model.addAttribute("productCategoryTree", productCategoryService.findTree(true));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("productCategoryId",productCategoryId);
		model.addAttribute("page", productService.findPage(productCategory, brand, promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable));
		return "/admin/product/list1";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if(ids!=null){
			for (Long id : ids) {
				Product product = productService.find(id);
				if(product!=null){
					fileService.delete(product.getImage());
					List<ProductImage> images = product.getProductImages();
					for (ProductImage productImage : images) {
						fileService.delete(productImage.getLarge());
						fileService.delete(productImage.getMedium());
						fileService.delete(productImage.getSource());
						fileService.delete(productImage.getThumbnail());
					}
					productService.delete(product);
				}
			}
		}
		
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/instore", method = RequestMethod.POST)
	public @ResponseBody
	Message instore(Long[] ids) {
		if(ids!=null){
			for (Long id : ids) {
				Product product = productService.find(id);
				if(product!=null){
					product.setIsMarketable(false);
					productService.update(product);
				}
			}
		}
		
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/instore1", method = RequestMethod.POST)
	public @ResponseBody
	Message instore1(Long id, Boolean isMarketable) {
		if(id!=null){
			Product product = productService.find(id);
			if(product!=null){
				product.setIsMarketable(isMarketable);
				productService.update(product);
			}
		}
		
		return SUCCESS_MESSAGE;
	}
	
	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
	}

	private long calculateDefaultPoint(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model,Boolean isMarketable) {
		model.addAttribute("isMarketable",isMarketable);
		return "/admin/product/index";
	}
	
	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String left(ModelMap model,Boolean isMarketable) {
		model.addAttribute("productCategoryTree",productCategoryService.findAll());
		if(isMarketable==null){
			isMarketable=false;
		}
		model.addAttribute("isMarketable",isMarketable);
		return "/admin/product/left";
	}
	
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
	public String index1(ModelMap model) {
		return "/admin/product/index1";
	}
	
	@RequestMapping(value = "/left1", method = RequestMethod.GET)
	public String left1(ModelMap model) {
		model.addAttribute("productCategoryTree",productCategoryService.findAll());
		return "/admin/product/left1";
	}
	
	@RequestMapping(value = "/cuxiao", method = RequestMethod.POST)
	public String cuxiao(Long promotionId,String productIds) {
		Promotion promotion = promotionService.find(promotionId);
		if(promotion!=null){
			String[] productId = productIds.split(",");
			for (String string : productId) {
				Product product = productService.find(Long.parseLong(string));
				if(product!=null){
					promotion.getProducts().add(product);
				}
			}
			
			promotionService.update(promotion);
		}
		return "redirect:list.jhtml";
	}
	
	@RequestMapping(value = "/cancleCuxiao", method = RequestMethod.POST)
	public @ResponseBody
	Message cancleCuxiao(Long id) {
		if(id!=null){
			Product product = productService.find(id);
			if(product!=null){
				Set<Promotion> promotions = product.getPromotions();
				for (Promotion promotion : promotions) {
					if(promotion.getProducts().contains(product)){
						promotion.getProducts().remove(product);
						promotionService.update(promotion);
					}
				}
				
			}
		}
		
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/list2", method = RequestMethod.GET)
	public String list2(Long id,ModelMap model) {
		Goods goods = productService.find(id).getGoods();
		model.addAttribute("products", goods.getProducts());
		model.addAttribute("id", id);
		return "/admin/product/list2";
	}
}