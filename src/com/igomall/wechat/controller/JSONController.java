package com.igomall.wechat.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.Order;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.entity.Ad;
import com.igomall.entity.Area;
import com.igomall.entity.Brand;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Receiver;
import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;
import com.igomall.entity.BonusCoupon.Type;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.AdPositionService;
import com.igomall.service.AdService;
import com.igomall.service.AreaService;
import com.igomall.service.BonusCouponService;
import com.igomall.service.BrandService;
import com.igomall.service.MemberService;
import com.igomall.service.OrderService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductImageService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.ReceiverService;
import com.igomall.service.SearchService;
import com.igomall.util.DesUtils;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.entity.JsonEntity;

@Controller("wechatJsonController")
@RequestMapping("/wechat/mo_bile")
public class JSONController extends BaseController {
	public enum PageType {
		index
	}
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	
	@Resource(name = "adServiceImpl")
	private AdService adService;
	
	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;
	
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;
	
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String page(){
		return "wap/index";
	}
	
	
	/**
	 * 首页
	 * @param model
	 * @param memberId
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> index() {
		Map<String,Object> data = new HashMap<String,Object>();
		//首页的滚动图片
		List<Ad> ads1 = adService.findList(adPositionService.find(1L), null, null, null, null);
		data.put("ads1", ads1);
		//首页的其他部分的广告图片
		List<Ad> ads2 = adService.findList(adPositionService.find(2L), null, null, null, null);
		data.put("ads2", ads2);
		
		List<Ad> ads3 = adService.findList(adPositionService.find(3L), null, null, null, null);
		data.put("ads3", ads3);
		
		List<Ad> ads4 = adService.findList(adPositionService.find(4L), null, null, null, null);
		data.put("ads4", ads4);
		List<Ad> ads5 = adService.findList(adPositionService.find(5L), null, null, null, null);
		data.put("ads5", ads5);
		
		//最新产品
		//data.put("newProducts", findProducts(1,8));
		//热销产品
		//data.put("hotProducts", findProducts(2,8));
		//促销产品
		//data.put("promotionProducts", findProducts(3,8));
		
		
		return data;
	}
	
	/**
	 * 查询 最新产品，热销产品 促销产品
	 * @param type 类型。1：最新产品  2：热销产品  3：促销产品
	 * @param count  数量
	 * @return
	 */
	public List<Product> findProducts(Integer type,Integer count) {
		List<Product> products = new ArrayList<Product>();
		if(type==1){//最新产品
			products = productService.findList(null, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.dateDesc, count, null, null);
		}else if(type==2){//热销产品
			products = productService.findList(null, null, null, null, null, null, null, true, null, null, null, null, null, OrderType.dateDesc, count, null, null);
		}else if(type==3){//促销产品
			Promotion promotion = promotionService.find(1L);
			
			products = productService.findList(null, null, promotion, null, null, null, null, true, null, null, null, null, null, OrderType.salesDesc, count, null, null);
		}
		
		return products;
	}

	/**
	 * 首页
	 * @param model
	 * @param memberId
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> search(String act,String op) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		if("index".equals(act)){//首页
			if("search_hot_info".equals(op)){//搜索
				Setting setting = SettingUtils.get();
				data.put("datas", setting.getHotSearches());
				return data;
			}
			//首页滚动广告
			data.put("adv_list", adService.findList(adPositionService.find(1L), null, null, null, null));
			
			return data;
			
		}else{
			return data;
		}
	}
	
	
	
	/**
	 * 获取产品的分类
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/productCategory/getProductCategories", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getProductCategories(Long gc_id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		ProductCategory parent = productCategoryService.find(gc_id);
		List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		if(parent==null){
			productCategories = productCategoryService.findRoots(true);
		}else{
			productCategories = productCategoryService.findDirectChildren(parent, null,true);
		}
		data.put("datas", productCategories);
		return data;
	}
	
	/**
	 * 获取产品的一级分类
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/productCategory/getBrand", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getBrand() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		List<Brand> brands = brandService.findAll();
		data.put("datas", brands);
		
		return data;
	}
	
	/**
	 * 获取产品的详情
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/product/getProductDetail", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getProductDetail(HttpServletRequest request,HttpServletResponse response,Long goods_id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Product product = productService.find(goods_id);
		data.put("datas", product);
		
		/*
		 * 处理规格
		 */
		
		Map<Specification,List<SpecificationValue>> specifications = new HashMap<Specification,List<SpecificationValue>>();
		Set<SpecificationValue> specificationValues = product.getGoods().getSpecificationValues();;
		
		for (SpecificationValue specificationValue : specificationValues) {
			Specification specification = specificationValue.getSpecification();
			if(specifications.containsKey(specification)){
				specifications.get(specification).add(specificationValue);
			}else{
				List<SpecificationValue> specificationValues1 = new ArrayList<SpecificationValue>();
				specificationValues1.add(specificationValue);
				specifications.put(specification, specificationValues1);
			}
			
		}
		data.put("specifications", specifications);
		
		return data;
	}
	
	
	@RequestMapping(value = "/receiver/list", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> receiver_list() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Set<Receiver> receivers = member.getReceivers();
		data.put("datas", receivers);
		
		return data;
	}
	
	@RequestMapping(value = "/receiver/edit", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> receiver_edit(Long id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Receiver receiver = receiverService.find(id);
		data.put("datas", receiver);
		
		return data;
	}
	
	@RequestMapping(value = "/area/getArea", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> area_getArea(Long area_id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		List<Area> areas = new ArrayList<Area>();
		if(area_id==null||area_id==0){
			areas = areaService.findRoots();
		}else{
			Area parent = areaService.find(area_id);
			areas = areaService.findChildren(parent);
		}
		data.put("datas", areas);
		
		return data;
	}
	
	@RequestMapping(value = "/area/update", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> area_update(Receiver receiver,Integer is_default,Long areaId) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		receiver.setArea(areaService.find(areaId));
		if(is_default!=null&&is_default==1){
			receiver.setIsDefault(true);
		}else{
			receiver.setIsDefault(false);
		}
		receiver.setMember(memberService.getCurrent());
		if(receiver.getId()==null){
			receiverService.save(receiver);
		}else{
			receiverService.update(receiver);
		}
		
		data.put("datas", receiver);
		
		return data;
	}
	
	@RequestMapping(value = "/receiver/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> area_delete(Long address_id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		receiverService.delete(receiverService.find(address_id));

		data.put("datas", "ok");
		
		return data;
	}
	
	/**
	 * 默认：综合排序
	 * 人气排序：key=2&order=2
	 * 价格从高到低：key=3&order=2
	 * 价格从低到高：&key=3&order=1
	 * 销量排序：key=1&order=2
	 * @param model
	 * @param productCategoryId
	 * @param brandId
	 * @param page
	 * @param curpage
	 * @param key
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> product_list(ModelMap model, Long productCategoryId,Long brandId,Integer page,Integer curpage,Integer key,Integer order,String keyword,BigDecimal price_from,BigDecimal price_to) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		OrderType orderType = OrderType.priceDesc;
		if(key==null&&order==null){
			orderType = OrderType.priceDesc;
		}else if(key==2&&order==2){//人气排序
			orderType = OrderType.scoreDesc;
		}else if(key==3&&order==2){//价格从高到低
			orderType = OrderType.priceDesc;
		}else if(key==3&&order==1){//价格从低到高
			orderType = OrderType.priceAsc;
		}else if(key==1&&order==2){//销量排序
			orderType = OrderType.salesDesc;
		}else{//综合排序
			orderType = OrderType.priceDesc;
		}
		
		if(productCategoryId==null&&brandId==null){//如果这两个都为null  那就一定是搜索了
			if(StringUtils.isEmpty(keyword)){
				data.put("datas", productService.findList(null, null, null, null, null, price_from,  price_to, true, true, null, null, null, null, orderType, (curpage-1)*page, page, null, new ArrayList<Order>(), true));
			}else{
				Pageable pageable = new Pageable(curpage,page);
				data.put("datas", searchService.search1(keyword, price_from,  price_to, orderType,pageable).getContent());
			}
			
		}else{
			data.put("datas", productService.findList(productCategoryService.find(productCategoryId), brandService.find(brandId), null, null, null, null, null, true, true, null, null, null, null, orderType, (curpage-1)*page, page, null, new ArrayList<Order>(), true));
		}
		
		return data;
	}
	
	
	
	
	@RequestMapping(value = "searchCondition", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> searchCondition() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		List<Area> area_list = areaService.findRoots();
		
		List<JsonEntity> contract_list = new ArrayList<JsonEntity>();
		JsonEntity jsonEntity1 = new JsonEntity();
		jsonEntity1.setId(1L);
		jsonEntity1.setIsParent(false);
		jsonEntity1.setName("7天退货");
		
		JsonEntity jsonEntity2 = new JsonEntity();
		jsonEntity2.setId(1L);
		jsonEntity2.setIsParent(false);
		jsonEntity2.setName("品质承诺");
		
		JsonEntity jsonEntity3 = new JsonEntity();
		jsonEntity3.setId(1L);
		jsonEntity3.setIsParent(false);
		jsonEntity3.setName("破损补寄");
		
		JsonEntity jsonEntity4 = new JsonEntity();
		jsonEntity4.setId(1L);
		jsonEntity4.setIsParent(false);
		jsonEntity4.setName("急速物流");
		
		contract_list.add(jsonEntity1);
		contract_list.add(jsonEntity2);
		contract_list.add(jsonEntity3);
		contract_list.add(jsonEntity4);
		
		data.put("area_list", area_list);
		data.put("contract_list", contract_list);
		return data;
	}
	
	
	/**
	 * 
	 * @param model
	 * @param promotionId
	 * @param page
	 * @param curpage
	 * @param key
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/promotion/list", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> promotion_list(ModelMap model, Long promotionId,Integer page,Integer curpage,Integer key,Integer order) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		OrderType orderType = OrderType.priceDesc;
		if(key==null&&order==null){
			orderType = OrderType.priceDesc;
		}else if(key==2&&order==2){//人气排序
			orderType = OrderType.scoreDesc;
		}else if(key==3&&order==2){//价格从高到低
			orderType = OrderType.priceDesc;
		}else if(key==3&&order==1){//价格从低到高
			orderType = OrderType.priceAsc;
		}else if(key==1&&order==2){//销量排序
			orderType = OrderType.salesDesc;
		}else{//综合排序
			orderType = OrderType.priceDesc;
		}
		
		if(promotionId!=null){//如果这两个都为null  那就一定是搜索了
			Pageable pageable = new Pageable(curpage,page);
			data.put("datas", productService.findPage(null, null, promotionService.find(promotionId), null, null, null, null, true, true, null, false, null, null, orderType, pageable).getContent());
		}
		
		Member member = memberService.getCurrent();
		data.putAll(DesUtils.encrypt2(member.getId().toString()));
		return data;
	}
	
	
	@RequestMapping(value = "/history/list", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> history_list(Long[] ids) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		data.put("datas", productService.findList(ids));
		
		return data;
	}
	
	@RequestMapping(value = "/myPeople/list", method = RequestMethod.POST)
	public @ResponseBody
	List<JsonEntity> myPeople(Long id) {
		Member parent = memberService.getCurrent();
		if(id==null){
			parent = memberService.getCurrent();
		}else{
			parent = memberService.find(id);
		}
		
		if(parent==null){
			parent = memberService.find(337L);
		}
		List<JsonEntity> jsonEntities = new ArrayList<JsonEntity>();
		List<Member> children = memberService.findChildren(parent);
		if(children!=null&&children.size()>0){
			for (Member member : children) {
				JsonEntity jsonEntity = new JsonEntity(member.getId(),member.getUsername(),member.getChildren().size()>0?true:false);
				jsonEntities.add(jsonEntity);
			}
		}
		return jsonEntities;
	}
	
	/**
	 * 产品里面的查看详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/product/product_introduce", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> promotion_product_introduce(Long id) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		data.put("datas", productService.find(id).getIntroduction());
		
		return data;
	}
	
	/**订单****************************************************************************************************/
	/**
	 * 
	 * @author black
	 * @param page
	 * @param curpage
	 * @return
	 */
	/**
	 * 订单列表
	 * @author 夏黎
	 * @param page 每页显示多少条
	 * @param curpage 当前页
	 * @param key 当前登录的用户
	 * @param state_type 订单的状态   null:全部订单   state_new:待付款 state_send:待收货 state_noeval:待评价
	 * @param order_key 查询关键字
	 * @return
	 */
	@RequestMapping(value = "/order/list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> order_list(Integer page,Integer curpage,String key,String state_type,String order_key){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		Member member = memberService.findByUsername(key);
		Pageable pageable = new Pageable(curpage,page);
		if(state_type==null){
			data.put("datas", orderService.findPage(member,null,null,null,null,pageable).getContent());
		}else if("state_new".equals(state_type)){//待付款订单
			data.put("datas", orderService.findPageWaitingPayment(member,pageable).getContent());
		}else if("state_send".equals(state_type)){//待收货
			data.put("datas", orderService.findPageWaitingReceipt(member,pageable).getContent());
		}else if("state_noeval".equals(state_type)){//待评价
			data.put("datas", orderService.findWaitingNoevalPage(member,pageable).getContent());
		}
		
		return data;
	}
	
	/**
	 * 订单的取消
	 * @author black
	 * @return
	 */
	@RequestMapping(value = "/order/cancel", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> order_cancel(String key,String sn){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		com.igomall.entity.Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && order.getPaymentStatus() == PaymentStatus.unpaid) {
			if (order.isLocked(null)) {
				data.put("message", Message.warn("shop.member.order.locked"));
			}else{
				orderService.cancel(order, null);
				data.put("message", SUCCESS_MESSAGE);
			}
			
		}else{
			data.put("message", ERROR_MESSAGE);
		}
		
		return data;
	}
	
	/**
	 * 订单的完成
	 * 
	 * @param sn
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/order/complete", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> order_complete(String key,String sn) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		com.igomall.entity.Order order = orderService.findBySn(sn);
		if (order != null && !order.isExpired()&& order.getOrderStatus() == OrderStatus.confirmed&&memberService.getCurrent().equals(order.getMember())) {
			orderService.complete(order, null);
			bonusCouponService.create(order, Type.member, null);
			data.put("message", SUCCESS_MESSAGE);
		} else {
			data.put("message", ERROR_MESSAGE);
		}
		
		return data;
	}
	
	@RequestMapping(value="/search/getHotWords",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getHotWords(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		data.put("list",SettingUtils.get().getHotSearches());
		
		return data;
	}
}
