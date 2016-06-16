package com.igomall.controller.wap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.igomall.FileInfo.FileType;
import com.igomall.Message;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.Setting.CaptchaType;
import com.igomall.entity.AdPosition;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Deposit;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Point;
import com.igomall.entity.Point.Type;
import com.igomall.entity.Product;
import com.igomall.entity.Product.OrderType;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Receiver;
import com.igomall.entity.RechargeCard;
import com.igomall.entity.RedPacket;
import com.igomall.entity.Shipping;
import com.igomall.entity.Shop;
import com.igomall.entity.Tag;
import com.igomall.entity.Voucher;
import com.igomall.entity.Withdraw;
import com.igomall.service.AdPositionService;
import com.igomall.service.AdService;
import com.igomall.service.AreaService;
import com.igomall.service.BonusCouponService;
import com.igomall.service.BrandService;
import com.igomall.service.CaptchaService;
import com.igomall.service.ConsultationService;
import com.igomall.service.CouponCodeService;
import com.igomall.service.DepositService;
import com.igomall.service.FileService;
import com.igomall.service.InComeService;
import com.igomall.service.MemberService;
import com.igomall.service.MessageService;
import com.igomall.service.OrderService;
import com.igomall.service.PointService;
import com.igomall.service.ProductCategoryService;
import com.igomall.service.ProductNotifyService;
import com.igomall.service.ProductService;
import com.igomall.service.PromotionService;
import com.igomall.service.ReceiverService;
import com.igomall.service.RechargeCardService;
import com.igomall.service.RedPacketService;
import com.igomall.service.ReviewService;
import com.igomall.service.SearchService;
import com.igomall.service.ShippingService;
import com.igomall.service.ShopService;
import com.igomall.service.TagService;
import com.igomall.service.VoucherService;
import com.igomall.service.WithdrawService;
import com.igomall.util.DateUtils;
import com.igomall.util.JsonUtils;
import com.igomall.util.SMSCodeUtils;
import com.igomall.util.SettingUtils;
import com.igomall.util.SmsUtils;
import com.igomall.util.WebUtils;

@Controller("wapAjaxController")
@RequestMapping("/wap/mo_bile")
public class AjaxController extends BaseController {

	private static final int NEW_ORDER_COUNT = 6;
	@Resource(name = "adServiceImpl")
	private AdService adService;
	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "pointServiceImpl")
	private PointService pointService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;
	@Resource(name = "inComeServiceImpl")
	private InComeService inComeService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name="depositServiceImpl")
	private DepositService depositService;

	@Resource(name="receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name="withdrawServiceImpl")
	private WithdrawService withdrawService;
	
	@Resource(name="captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name="rechargeCardServiceImpl")
	private RechargeCardService rechargeCardService;
	@Resource(name="voucherServiceImpl")
	private VoucherService voucherService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name="redPacketServiceImpl")
	private RedPacketService redPacketService;

	@RequestMapping(value = "/makecodekey", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> getCaptchaId(HttpServletRequest request) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("captchaId", UUID.randomUUID().toString());
		return data;
	}
	
	/**
	 * 验证码的获取
	 * @param captchaId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/makecode", method = RequestMethod.GET)
	public void image(String captchaId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(captchaId)) {
			captchaId = request.getSession().getId();
		}
		String pragma = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String value = new StringBuffer().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
		response.addHeader(pragma, value);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		ServletOutputStream servletOutputStream = null;
		try {
			servletOutputStream = response.getOutputStream();
			BufferedImage bufferedImage = captchaService.buildImage(captchaId);
			ImageIO.write(bufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(servletOutputStream);
		}
	}
	
	
	/**
	 * 首页的ajax加载
	 * @param model
	 * @return
	 */
	@RequestMapping(value="index",method = RequestMethod.GET)
	public String index(ModelMap model,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		//首页轮播广告
		AdPosition adPosition = adPositionService.find(5L);
		model.addAttribute("ads",adPosition.getAds());
		
		//home1
		model.addAttribute("home1", adService.find(5L));
		
		
		
		//home3
		Map<String,Object> home3 = new HashMap<String,Object>();
		adPosition = adPositionService.find(2L);
		home3.put(adPosition.getName(), adPosition.getAds());
		model.addAttribute("home3", home3);
		
		
		
		
		
		Pageable pageable = new Pageable(1, 14);
		Map<String,List<Product>> map = new HashMap<String,List<Product>>();
		//goods(最新商品)
		map.put("最新商品", productService.findPage(null, null, null, null, null, null, null, true, true, null, false, null, null, OrderType.dateDesc, pageable).getContent());
		//热卖商品
		pageable = new Pageable(2, 14);
		map.put("热卖商品", productService.findPage(null, null, null, null, null, null, null, true, true, null, false, null, null, OrderType.salesDesc, pageable).getContent());
		//促销商品
		pageable = new Pageable(3, 14);
		map.put("促销商品", productService.findPage(null, null, null, null, null, null, null, true, true, null, false, null, null, OrderType.scoreDesc, pageable).getContent());
		model.addAttribute("map",map);
		return "/wap/ajax/index";
	}
	
	/**
	 * 首页里面的搜索框的自动补全
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/search_hot_info/index",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> search_hot_info_index(ModelMap model,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Setting setting = SettingUtils.get();
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("datas",setting.getHotSearches()[0]);
		return data;
	}
	
	
	@RequestMapping(value="/search_key_list/index",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> search_key_list_index(ModelMap model,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Setting setting = SettingUtils.get();
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("list",setting.getHotSearches());
		data.put("his_list",setting.getHotSearches());
		return data;
	}
	
	/**
	 * key:3 order:2  价格从高到低
	 * key:3 order:1 价格从低到高
	 * key:2 order:2 人气排序，从高到低
	 * key:1 order:2 销量排序 ，从高到底
	 * @param response
	 * @param keyword
	 * @param page
	 * @param curpage
	 * @param startPrice
	 * @param endPrice
	 * @param orderType
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/search", method = RequestMethod.GET)
	public String search(Long productCategoryId,Long brandId,Long promotionId, Long[] tagIds,Integer key,Integer order,HttpServletResponse response,String keyword,Integer page,Integer curpage, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request, ModelMap model) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(key!=null&&order!=null){
			if(key==3&&order==2){
				orderType = OrderType.priceDesc;
			}else if(key==3&&order==1){
				orderType = OrderType.priceAsc;
			}else if(key==2&&order==2){
				orderType = OrderType.topDesc;
			}else if(key==1&&order==2){
				orderType = OrderType.salesDesc;
			}
		}
		
		/*if (StringUtils.isEmpty(keyword)) {
			data.put("code", 200);
			data.put("hasmore", false);
			data.put("page_total", 0);
			data.put("goods_list", new ArrayList<Product>());
			return data;
		}*/
		model.addAttribute("hasmore", false);
		model.addAttribute("page_total", 0);
		model.addAttribute("goods_list", new ArrayList<Product>());
		
		Pageable pageable = new Pageable(curpage, page);
		
		Page<Product> page1 = new Page<Product>();
		
		if(productCategoryId!=null||brandId!=null){
			Brand brand = brandService.find(brandId);
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
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
			page1 = productService.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, true, true, null, false, null, null, orderType, pageable);
		}else if(keyword!=null){
			page1 = searchService.search(keyword, startPrice, endPrice, orderType, pageable);
		}
		
		Integer page_total = page1.getTotalPages();
		if(curpage<page_total){
			model.addAttribute("hasmore", true);
		}else{
			model.addAttribute("hasmore", false);
		}
		model.addAttribute("page", page1);
		return "/wap/ajax/product/list";
	}
	
	@RequestMapping(value = "/search/search_adv", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> search_search_adv(HttpServletResponse response,ModelMap model) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("code", 200);
		data.put("area_list", areaService.findRoots());
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(1, "7天退货");
		map.put(2, "品质承诺");
		map.put(3, "破损补寄");
		map.put(4, "急速物流");
		data.put("contract_list", map);
		return data;
	}
	
	@RequestMapping(value = "/shop/store_credit", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> store_credit(HttpServletResponse response,Long shopId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> map0 = new HashMap<String,Object>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		Map<String,Object> map2 = new HashMap<String,Object>();
		if (shopId != null) {
			Shop shop = shopService.find(shopId);
			if(shop!=null){
				map0.put("text", "描述");
				map0.put("credit", shop.getProductAndDescription());
				map0.put("percent", "--");
				map0.put("percent_class", "equal");
				map0.put("percent_text", "持平");
				data.put("store_desccredit", map0);
				map1.put("text", "服务");
				map1.put("credit", shop.getProductAndDescription());
				map1.put("percent", "--");
				map1.put("percent_class", "equal");
				map1.put("percent_text", "持平");
				data.put("store_servicecredit", map1);
				map2.put("text", "物流");
				map2.put("credit", shop.getSellerServiceAttitude());
				map2.put("percent", "--");
				map2.put("percent_class", "equal");
				map2.put("percent_text", "持平");
				data.put("store_deliverycredit", map2);
			}else{
				map0.put("text", "描述");
				map0.put("credit", 5.0);
				map0.put("percent", "--");
				map0.put("percent_class", "equal");
				map0.put("percent_text", "持平");
				data.put("store_desccredit", map0);
				map1.put("text", "服务");
				map1.put("credit", 5.0);
				map1.put("percent", "--");
				map1.put("percent_class", "equal");
				map1.put("percent_text", "持平");
				data.put("store_servicecredit", map1);
				map2.put("text", "物流");
				map2.put("credit", 5.0);
				map2.put("percent", "--");
				map2.put("percent_class", "equal");
				map2.put("percent_text", "持平");
				data.put("store_deliverycredit", map2);
			}
			
		}else{
			map0.put("text", "描述");
			map0.put("credit", 5.0);
			map0.put("percent", "--");
			map0.put("percent_class", "equal");
			map0.put("percent_text", "持平");
			data.put("store_desccredit", map0);
			map1.put("text", "服务");
			map1.put("credit", 5.0);
			map1.put("percent", "--");
			map1.put("percent_class", "equal");
			map1.put("percent_text", "持平");
			data.put("store_servicecredit", map1);
			map2.put("text", "物流");
			map2.put("credit", 5.0);
			map2.put("percent", "--");
			map2.put("percent_class", "equal");
			map2.put("percent_text", "持平");
			data.put("store_deliverycredit", map2);
		}
		
		data.put("code",200);
		
		
		return data;
	}
	
	/**
	 * 获取产品的分类
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/productCategory/getProductCategories", method = RequestMethod.GET)
	public String getProductCategories(Long gc_id,ModelMap model) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		ProductCategory parent = productCategoryService.find(gc_id);
		if(parent==null){
			model.addAttribute("productCategories",productCategoryService.findRoots(true));
			return "/wap/ajax/productCategory/list";
		}else{
			model.addAttribute("productCategories",productCategoryService.findDirectChildren(parent, null,true));
			return "/wap/ajax/productCategory/list1";
		}
	}
	
	/**
	 * 获取品牌
	 * @return
	 * @author 夏黎
	 */
	@RequestMapping(value = "/productCategory/getBrand", method = RequestMethod.GET)
	public String getBrand(ModelMap model) {
		model.addAttribute("brands", brandService.findAll());
		return "/wap/ajax/brand/list";
	}
	
	@RequestMapping(value = "/login/get_state", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> get_state() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("pc_qq", "1");
		datas.put("pc_sn", "1");
		datas.put("connect_qq", "1");
		datas.put("connect_sn", "1");
		datas.put("connect_wap_wx", "1");
		datas.put("connect_sms_reg", "1");
		datas.put("connect_sms_lgn", "1");
		datas.put("connect_sms_psd", "1");
		
		data.put("datas", datas);
		
		return data;
	}
	
	/**
	 * 检查当天是否已经签到过了
	 * @return
	 */
	@RequestMapping(value = "/point/checksignin", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> checksignin() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		List<Point> list = pointService.findList(member, Type.sign, DateUtils.getStart(new Date()), DateUtils.getEnd(new Date()));
		if(list.isEmpty()){
			data.put("code", 200);
			data.put("points_signin", 5);
		}else{
			data.put("code", 400);
		}
		
		return data;
	}
	
	/**
	 * 检查当天是否已经签到过了
	 * @return
	 */
	@RequestMapping(value = "/point/my_asset", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> my_asset() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("points", member.getPoint());
		
		return data;
	}
	
	@RequestMapping(value = "/point/signin_list", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> signin_list(Integer curpage,Integer page) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(curpage,page);
		data.put("signin_list", pointService.findPage(member, null, null, null, pageable).getContent());
		
		return data;
	}
	
	@RequestMapping(value = "/point/signin_add", method = RequestMethod.GET)
	public @ResponseBody
	Map<String,Object> signin_add() {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Point point = new Point();
		point.setBalance(5L);
		point.setMember(member);
		point.setMemo("会员签到");
		point.setType(Type.sign);
		pointService.save(point);
		
		member.setPoint(member.getPoint()+5L);
		memberService.update(member);
		
		data.put("points", member.getPoint());
		
		return data;
	}
	
	/**
	 * 检测是否允许手机注册
	 * @return
	 */
	@RequestMapping(value = "/connect_sms_reg", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> connect_sms_reg(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		data.put("datas", 1);
		
		
		return data;
	}
	
	@RequestMapping(value = "/member/member_index", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> member_index(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("memberRank", member.getMemberRank().getName());
		data.put("avatar", member.getPhoto()==null?"http://b2b2c.shopnctest.com/dema/data/upload/shop/common/default_user_portrait.gif":member.getPhoto());
		data.put("username", member.getUsername());
		data.put("favorites_goods", member.getFavoriteProducts().size());
		data.put("favorites_store", member.getFavoriteShops().size());
		data.put("state", 1);//设置已经登陆了状态
		
		
		
		Shop shop = shopService.find(1L);
		data.put("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		data.put("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		data.put("messageCount", messageService.count(member, false));
		data.put("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		data.put("favoriteCount", productService.count(member, null, null, null, null, null, null));
		data.put("productNotifyCount", productNotifyService.count(member, null, null, null));
		data.put("reviewCount", reviewService.count(member, null, null, null));
		data.put("consultationCount", consultationService.count(member, null, null));
		data.put("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		data.put("bonusCouponCount",bonusCouponService.count(member,false,false,shop,com.igomall.entity.BonusCoupon.Type.member,false));
		
		return data;
	}
	
	/**
	 * 浏览记录
	 * @return
	 */
	@RequestMapping(value = "/member/browse_list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> browse_list(Long[] ids){
		Map<String,Object> data = new HashMap<String,Object>();
		if(ids==null||ids.length==0){
			ids = new Long[]{
				11177L,11182L,5433L,10268L,2504L
			};
		}
		data.put("goodsbrowse_list",productService.findList(ids));;
		data.put("code", 200);
		
		return data;
	}
	
	@RequestMapping(value = "/point/pointslog", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> pointslog(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("log_list", pointService.findList(member, null, null, null));
		return data;
	}
	
	@RequestMapping(value = "/deposit/predepositlog", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> predepositlog(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("list", depositService.findList(member,null));
		return data;
	}
	
	/**
	 * 充值记录列表
	 * @return
	 */
	@RequestMapping(value = "/deposit/pdrechargelist", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> pdrechargelist(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("list", depositService.findReChageList(member));
		return data;
	}
	
	/**
	 * 获取预存款的余额
	 * @return
	 */
	@RequestMapping(value = "/deposit/my_asset", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> deposit_my_asset(String fields){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		if("predepoit".equals(fields)){
			data.put(fields, member.getBalance());
		}else if("available_rc_balance".equals(fields)){
			data.put(fields, member.getBalance3());
		}else{
			data.put("predepoit", member.getBalance());
			data.put("available_rc_balance", member.getBalance());
			data.put("voucher", member.getBalance());
			data.put("redpacket", member.getBalance());
			data.put("point", member.getPoint());
		}
		
		return data;
	}
	
	@RequestMapping(value = "/receiver/address_list", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> address_list(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("address_list", member.getReceivers());
		return data;
	}
	
	@RequestMapping(value = "/receiver/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> receiver_delete(Long address_id){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		Receiver receiver = receiverService.find(address_id);
		if(receiver!=null&&member.getReceivers().contains(receiver)){
			data.put("code", 200);
			data.put("message", SUCCESS_MESSAGE);
			receiverService.delete(receiver);
		}else{
			data.put("code", 201);
			data.put("message", ERROR_MESSAGE);
		}
		
		return data;
	}
	
	
	
	
	/**
	 * 获取地址列表
	 * @return
	 */
	@RequestMapping(value = "/area/area_list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> area_list(Long area_id){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		if(area_id==0){
			data.put("area_list", areaService.findRoots());
		}else{
			data.put("area_list", areaService.findChildren(areaService.find(area_id)));
		}
		return data;
	}
	
	/**
	 * 收获地址的添加
	 * @param receiver
	 * @param areaId
	 * @return
	 */
	@RequestMapping(value = "/receiver/address_add", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> address_add(Receiver receiver, Long areaId) {
		receiver.setArea(areaService.find(areaId));
		Map<String,Object> data = new HashMap<String,Object>();
		if (!isValid(receiver)) {
			data.put("code", 200);
			data.put("message", ERROR_MESSAGE);
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			data.put("code", 200);
			data.put("message", Message.error("收获地址不能超过"+Receiver.MAX_RECEIVER_COUNT));
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		data.put("code", 200);
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	
	@RequestMapping(value = "/receiver/address_info", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> address_info(Long address_id, ModelMap model, RedirectAttributes redirectAttributes) {
		Receiver receiver = receiverService.find(address_id);
		Map<String,Object> data = new HashMap<String,Object>();
		if (receiver == null) {
			data.put("code", 200);
			data.put("message", Message.error("地址不存在"));
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			data.put("code", 200);
			data.put("message", Message.error("地址不存在"));
		}
		data.put("code", 200);
		data.put("address_info",receiver);
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	
	@RequestMapping(value = "/receiver/address_edit", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> address_edit(Receiver receiver, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		Map<String,Object> data = new HashMap<String,Object>();
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			data.put("code", 200);
			data.put("message", Message.error("地址不存在"));
		}
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			data.put("code", 200);
			data.put("message", Message.error("地址不存在"));
		}
		Member member = memberService.getCurrent();
		if (!member.equals(pReceiver.getMember())) {
			data.put("code", 200);
			data.put("message", Message.error("地址不存在"));
		}
		receiverService.update(receiver, "member");
		data.put("code", 200);
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	/**
	 * 提现记录列表
	 * @return
	 */
	@RequestMapping(value = "/withdraw/pdcashlist", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> pdcashlist(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("list", withdrawService.findList(member));
		return data;
	}
	
	@RequestMapping(value = "/withdraw/pdcashinfo", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> pdcashinfo(Long id){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		Withdraw withdraw = withdrawService.find(id);
		if(withdraw.getMember()==member){
			data.put("info", withdraw);
		}else{
			data.put("info", new Withdraw());
		}
		
		return data;
	}
	
	
	/**
	 * 充值卡的充值记录
	 * @return
	 */
	@RequestMapping(value = "/recharge/rcblog", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> rcblog(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("log_list", depositService.findList(member,com.igomall.entity.Deposit.Type.rechargeCard));
		return data;
	}
	
	
	@RequestMapping(value = "/recharge/rechargecard_add", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> rechargecard_add(String code,String captcha,String captchaId) {
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		if (!captchaService.isValid(CaptchaType.rechargeCard, captchaId, captcha)) {
			data.put("message",Message.error("shop.captcha.invalid"));
			return data;
		}
		
		RechargeCard rechargeCard = rechargeCardService.findByCode(code);
		if(rechargeCard==null||rechargeCard.getMember()!=member){
			data.put("message",Message.error("请输入正确的充值卡编号"));
			return data;
		}
		if(rechargeCard.getIsExperid()){
			data.put("message",Message.error("充值卡已过期或未开始使用"));
			return data;
		}
		if(rechargeCard.getIsUsed()){
			data.put("message",Message.error("充值卡已使用"));
			return data;
		}
		rechargeCard.setIsUsed(true);
		rechargeCard.setUsedDate(new Date());
		rechargeCardService.update(rechargeCard);
		
		member.setBalance3(member.getBalance3().add(rechargeCard.getBalance()));
		memberService.update(member);
		
		//写入充值记录
		Deposit deposit = new Deposit();
		deposit.setBalance(member.getBalance3());
		deposit.setCredit(rechargeCard.getBalance());
		deposit.setDebit(BigDecimal.ZERO);
		deposit.setFreezeBalance(BigDecimal.ZERO);
		deposit.setMember(member);
		deposit.setMemo("充值卡充值");
		deposit.setType(com.igomall.entity.Deposit.Type.rechargeCard);
		depositService.save(deposit);
		
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	
	/**
	 * 代金券领用记录
	 * @return
	 */
	@RequestMapping(value = "/voucher/voucher_list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> voucher_list(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("voucher_list", depositService.findList(member,com.igomall.entity.Deposit.Type.voucher));
		return data;
	}
	
	
	@RequestMapping(value = "/voucher/voucher_pwex", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> voucher_pwex(String code,String captcha,String captchaId) {
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		if (!captchaService.isValid(CaptchaType.voucher, captchaId, captcha)) {
			data.put("message",Message.error("shop.captcha.invalid"));
			return data;
		}
		
		Voucher voucher = voucherService.findByCode(code);
		if(voucher==null||voucher.getMember()!=member){
			data.put("message",Message.error("请输入正确的代金券编号"));
			return data;
		}
		if(voucher.getIsExperid()){
			data.put("message",Message.error("代金券已过期或未开始使用"));
			return data;
		}
		if(voucher.getIsUsed()){
			data.put("message",Message.error("代金券已使用"));
			return data;
		}
		voucher.setIsUsed(true);
		voucher.setUsedDate(new Date());
		voucherService.update(voucher);
		
		//写入充值记录
		Deposit deposit = new Deposit();
		deposit.setBalance(member.getBalance4());
		deposit.setCredit(voucher.getBalance());
		deposit.setDebit(BigDecimal.ZERO);
		deposit.setFreezeBalance(BigDecimal.ZERO);
		deposit.setMember(member);
		deposit.setMemo("代金券充值");
		deposit.setType(com.igomall.entity.Deposit.Type.voucher);
		depositService.save(deposit);
		
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	
	/**
	 * 红包领取记录
	 * @return
	 */
	@RequestMapping(value = "/redpacket/list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> redpacket_list(){
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		data.put("redpacket_list", depositService.findList(member,com.igomall.entity.Deposit.Type.redpacket));
		return data;
	}
	
	
	@RequestMapping(value = "/redpacket/rp_pwex", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> redpacket_rp_pwex(String code,String captcha,String captchaId) {
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = memberService.getCurrent();
		data.put("code", 200);
		if (!captchaService.isValid(CaptchaType.redPacket, captchaId, captcha)) {
			data.put("message",Message.error("shop.captcha.invalid"));
			return data;
		}
		
		RedPacket redPacket = redPacketService.findByCode(code);
		if(redPacket==null||redPacket.getMember()!=member){
			data.put("message",Message.error("请输入正确的红包编号"));
			return data;
		}
		if(redPacket.getIsExperid()){
			data.put("message",Message.error("红包已过期或未开始领用"));
			return data;
		}
		if(redPacket.getIsUsed()){
			data.put("message",Message.error("红包已被领取"));
			return data;
		}
		redPacket.setIsUsed(true);
		redPacket.setUsedDate(new Date());
		redPacketService.update(redPacket);
		
		member.setBalance5(member.getBalance5().add(redPacket.getBalance()));
		memberService.update(member);
		
		//写入充值记录
		Deposit deposit = new Deposit();
		deposit.setBalance(member.getBalance5());
		deposit.setCredit(redPacket.getBalance());
		deposit.setDebit(BigDecimal.ZERO);
		deposit.setFreezeBalance(BigDecimal.ZERO);
		deposit.setMember(member);
		deposit.setMemo("领取红包");
		deposit.setType(com.igomall.entity.Deposit.Type.redpacket);
		depositService.save(deposit);
		
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	/**
	 * 产品详细信息
	 * @param goods_id
	 * @return
	 */
	@RequestMapping(value = "/product/goods_detail", method = RequestMethod.GET)
	public String goods_detail(Long goods_id,ModelMap model){
		Member member = memberService.getCurrent();
		model.addAttribute("code", 200);
		Product product = productService.find(goods_id);
		model.addAttribute("product", product);
		
		//推荐产品
		Pageable pageable = new Pageable(1,8);
		model.addAttribute("goods_commend_list", productService.findPage(null, null, null, null, null, null, null, true, true, null, false, null, null, OrderType.salesDesc, pageable).getContent());
		
		//是否收藏过该产品
		if(member!=null){
			if(member.getFavoriteProducts().contains(product)){
				model.addAttribute("is_favorate", true);
			}else{
				model.addAttribute("is_favorate", false);
			}
		}else{
			model.addAttribute("is_favorate", false);
		}
		
		return "/wap/ajax/product/detail";
	}
	
	/**
	 * 清楚浏览记录
	 * @return
	 */
	@RequestMapping(value = "/product/browse_clearall", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> browse_clearall(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		WebUtils.removeCookie(request, response, "historyProduct");
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	/**
	 * 商品收藏
	 * @return
	 */
	@RequestMapping(value = "/favorites/list", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> favorites_list(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		data.put("favorites_list", member.getFavoriteProducts());
		return data;
	}
	
	/**
	 * 店铺收藏
	 * @return
	 */
	@RequestMapping(value = "/favorites/store", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> favorites_store(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		data.put("favorites_list", member.getFavoriteShops());
		return data;
	}
	
	
	/**
	 * 获取手机号码
	 * @return
	 */
	@RequestMapping(value = "/get_mobile_info", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get_mobile_info(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		if(member!=null&&member.getMobile()!=null){
			data.put("mobile", member.getMobile());
			data.put("state", true);
		}else{
			data.put("mobile", "");
			data.put("state", false);
		}
		
		return data;
	}
	
	/**
	 * 获取支付密码
	 * @return
	 */
	@RequestMapping(value = "/get_paypwd_info", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get_paypwd_info(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		data.put("state", member.getPassword()==null?true:false);
		return data;
	}
	
	/**
	 * 修改登录密码的验证码的发送
	 * @return
	 */
	@RequestMapping(value = "/modify_password_step2", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> modify_password_step2(String captchaId,String captcha,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		if (!captchaService.isValid(CaptchaType.resetPassword, captchaId, captcha)) {
			data.put("code", 201);
			data.put("message",Message.error("shop.captcha.invalid"));
			return data;
		}
		String code = SMSCodeUtils.getCode(6);
		WebUtils.addCookie(request, response, "smsCode", code,60);
		Member member = memberService.getCurrent();
		SmsUtils.sendSms(member.getPhone(),code);
		
		data.put("smscode", code);
		data.put("sms_time", 60);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	@RequestMapping(value = "/modify_password_step3", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> modify_password_step3(String auth_code,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		String smsCode = WebUtils.getCookie(request, "smsCode");
		if(StringUtils.isEmpty(smsCode)||StringUtils.isEmpty(auth_code)||!auth_code.equals(smsCode)){
			data.put("code", 201);
			data.put("message", Message.error("验证码输入不正确"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	@RequestMapping(value = "/modify_password_step4", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> modify_password_step4(String auth_code,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		if(member==null||!member.getIsEnabled()||member.getIsLocked()){
			data.put("code", 201);
			data.put("message", Message.error("登陆信息已过期或无操作权限！"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	@RequestMapping(value = "/modify_password_step5", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> modify_password_step5(String password,String password1){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		if(member==null||!member.getIsEnabled()||member.getIsLocked()){
			data.put("code", 201);
			data.put("message", Message.error("登陆信息已过期或无操作权限！"));
			return data;
		}
		if(StringUtils.isEmpty(password1)||StringUtils.isEmpty(password)){
			data.put("code", 202);
			data.put("message", Message.error("密码输入有误！"));
			return data;
		}
		
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> execute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	@RequestMapping(value = "/modify_mobile_step2", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> modify_mobile_step2(String captchaId,String captcha,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		if (!captchaService.isValid(CaptchaType.modifyPhone, captchaId, captcha)) {
			data.put("code", 201);
			data.put("message",Message.error("shop.captcha.invalid"));
			return data;
		}
		String code = SMSCodeUtils.getCode(6);
		WebUtils.addCookie(request, response, "smsCodeModifyPhone", code,60);
		Member member = memberService.getCurrent();
		SmsUtils.sendSms(member.getPhone(),code);
		
		data.put("smscode", code);
		data.put("sms_time", 60);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	
	@RequestMapping(value = "/modify_mobile_step3", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> modify_mobile_step3(String auth_code,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		
		String smsCode = WebUtils.getCookie(request, "smsCodeModifyPhone");
		if(StringUtils.isEmpty(smsCode)||StringUtils.isEmpty(auth_code)||!auth_code.equals(smsCode)){
			data.put("code", 201);
			data.put("message", Message.error("验证码输入不正确"));
			return data;
		}
		
		member.setPhone(null);
		memberService.update(member);
		
		data.put("message", SUCCESS_MESSAGE);
		
		return data;
	}
	
	/**
	 * 订单列表
	 * @param page 每页显示的条数
	 * @param curpage 当前页
	 * @param state_type 订单状态
	 * @param order_key 查询条件
	 * @return
	 */
	@RequestMapping(value = "/order/list", method = RequestMethod.POST)
	public String order_list(Integer page,Integer curpage,String state_type,String order_key,ModelMap model){
		Member member = memberService.getCurrent();
		
		Pageable pageable = new Pageable(curpage,page);
		List<Order> list = orderService.findPage(member, pageable).getContent();
		model.addAttribute("page", orderService.findPage(member, pageable));
		if(list.size()==page){
			model.addAttribute("hasmore", true);
		}else{
			model.addAttribute("hasmore", false);
		}
		
		return "/wap/ajax/order/list";
	}
	
	@RequestMapping(value = "/order/search_deliver", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> search_deliver(String sn){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Map<String, Object> datas = new HashMap<String, Object>();
		Order order = orderService.findBySn(sn);
		if(order.getMember()==member){
			Set<Shipping> shippings = order.getShippings();
			Setting setting = SettingUtils.get();
			
			for (Shipping shipping : shippings) {
				if (shipping != null && shipping.getOrder() != null && memberService.getCurrent().equals(shipping.getOrder().getMember()) && StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
					datas.put("shipping", shippingService.query(shipping));
				}
			}
		}
		
		data.put("datas", datas);
		return data;
	}
	
	@RequestMapping(value = "/order/evaluate", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> evaluate(String sn){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if(order!=null&&order.getMember()==member){
			data.put("message", SUCCESS_MESSAGE);
			data.put("order_goods", order.getOrderItems());
			data.put("store_info", order.getShop());
		}else{
			data.put("message", SUCCESS_MESSAGE);
		}
		
		return data;
	}
	/**
	 * 订单的取消
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/order/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && order.getPaymentStatus() == PaymentStatus.unpaid) {
			if (order.isLocked(null)) {
				return Message.warn("shop.member.order.locked");
			}
			orderService.cancel(order, null);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileType fileType, MultipartFile file, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
		} else {
			String url = fileService.upload(fileType, file, false);
			if (url == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				data.put("message", SUCCESS_MESSAGE);
				data.put("url", url);
			}
		}
		try {
			response.setContentType("text/html; charset=UTF-8");
			JsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 订单的确认
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/order/confirm", method = RequestMethod.POST)
	public @ResponseBody
	Message confirm(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.confirmed) {
			orderService.confirm(order, null);
			return SUCCESS_MESSAGE;
		} else {
			return ERROR_MESSAGE;
		}
	}
	
	@RequestMapping(value = "/order/evaluate_again", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> evaluate_again(String sn){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if(order!=null&&order.getMember()==member){
			data.put("message", SUCCESS_MESSAGE);
			data.put("evaluate_goods", order.getOrderItems());
			data.put("store_info", order.getShop());
		}else{
			data.put("message", SUCCESS_MESSAGE);
		}
		
		return data;
	}
	
	@RequestMapping(value = "/product/info", method = RequestMethod.GET)
	public String product_info(Long id,ModelMap model){
		Product product = productService.find(id);
		model.addAttribute("product", product);
		
		return "/wap/ajax/product/info";
	}
	
	@RequestMapping(value = "/store/info", method = RequestMethod.GET)
	public String store_info(Long store_id,ModelMap model){
		Shop shop = shopService.find(store_id);
		model.addAttribute("shop", shop);
		Member member = memberService.getCurrent();
		//是否收藏过该产品
		if(member!=null){
			if(member.getFavoriteShops().contains(shop)){
				model.addAttribute("is_favorate", true);
			}else{
				model.addAttribute("is_favorate", false);
			}
		}else{
			model.addAttribute("is_favorate", false);
		}
		
		model.addAttribute("products", productService.findList());
		return "/wap/ajax/store/info";
	}
	
	/**
	 * 获取未读消息的条数
	 * @param username 用户名
	 * @return
	 */
	@RequestMapping(value="/index/getMsgCount",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getMsgCount(String username){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code",200);
		Member member = memberService.findByUsername(username);
		data.put("messageCount", messageService.count(member, false));
		return data;
	}
	
	
	@RequestMapping(value = "/member/message/list", method = RequestMethod.POST)
	public String message_list(String username,ModelMap model){
		
		Member member = memberService.getCurrent();
		if(member.getUsername().equals(username)){
			Pageable pageable = new Pageable(1, 30000);
			model.addAttribute("page", messageService.findPage(member, pageable));
		}else{
			model.addAttribute("page", new Page<Message>());
		}
		return "/wap/ajax/message/list";
	}
}
