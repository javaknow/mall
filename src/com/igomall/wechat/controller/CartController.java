
package com.igomall.wechat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.entity.Cart;
import com.igomall.entity.CartItem;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.CartItem.Type;
import com.igomall.service.CartItemService;
import com.igomall.service.CartService;
import com.igomall.service.IntegralExchangeProductService;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;
import com.igomall.util.DesUtils;

@Controller("wechatCartController")
@RequestMapping("/wechat/cart")
public class CartController extends BaseController {
	
	public static final String DEFAULT_SCAN_URL = "/scan.jhtml";

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;
	@Resource(name = "integralExchangeProductServiceImpl")
	private IntegralExchangeProductService integralExchangeProductService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Map<String,Object> add(Long id, Integer quantity,Long integralExchangeProductId, HttpServletRequest request, HttpServletResponse response,Long key,String result) throws IOException, Exception {
System.out.println("==================================");
		Map<String,Object> data = new HashMap<String,Object>();
		Member parent=null;
		Member member = memberService.getCurrent();
		if(result!=null&&key!=null){
			DesUtils des = new DesUtils(DesUtils.SECRETKEY+key);//自定义密钥 
			Long result1 = Long.parseLong(des.decrypt(result));
		    result1 = result1-key;
			 parent = memberService.find(result1);
		}
		
		if(parent==null){
			 parent = memberService.find(337L);
		 }
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Product product = productService.find(id);
		if (product == null) {
			data.put("message", Message.warn("shop.cart.productNotExsit"));
			return data;
		}
		
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("shop.cart.productNotMarketable"));
			return data;
		}
		if (product.getIsGift()) {
			data.put("message", Message.warn("shop.cart.notForSale"));
			return data;
		}

		Cart cart = cartService.getCurrent();
		
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}

		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			data.put("message", Message.warn("shop.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT));
			return data;
		}

		if (cart.contains(product)) {
			//CartItem cartItem = cart.getCartItem(product);
			CartItem cartItem = cart.getCartItem(product,parent);
			if(cartItem==null){
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
					return data;
				}
				if (product.getStock() != null && quantity > product.getAvailableStock()) {
					data.put("message", Message.warn("shop.cart.productLowStock"));
					return data;
				}
				cartItem = new CartItem();
				cartItem.setParent(parent);
				cartItem.setQuantity(quantity);
				cartItem.setProduct(product);
				cartItem.setCart(cart);
				cartItem.setType(Type.normal);
				cartItemService.save(cartItem);
				cart.getCartItems().add(cartItem);
			}else{
				if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
					data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
					return data;
				}
				if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					data.put("message", Message.warn("shop.cart.productLowStock"));
					return data;
				}
				cartItem.add(quantity);
				cartItem.setType(Type.normal);
				cartItem.setParent(parent);
				cartItemService.update(cartItem);
			}
			
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
				return data;
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				data.put("message", Message.warn("shop.cart.productLowStock"));
				return data;
			}
			CartItem cartItem = new CartItem();
			cartItem.setParent(parent);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setType(Type.normal);
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}
		
		data.put("isLogin", true);
		if(member==null){
			data.put("isLogin", false);
			data.put("url", DEFAULT_SCAN_URL+"?parentId="+parent.getId());
			data.put("message", Message.warn("请先扫描二维码再操作！"));
			return data;
		}else{
			data.put("message", Message.success("shop.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true, false)));
			return data;
		}
	}
	
	
	
	@RequestMapping(value = "/cal", method = RequestMethod.POST)
	public @ResponseBody
	Integer cal(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		Integer count = 0;
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
			for (CartItem cartItem : cart.getCartItems()) {
				count = count+cartItem.getQuantity();
			};
		}
		
		return count;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("cart", cartService.getCurrent());
		model.addAttribute("titie", "我的购物车");
		return "/wechat/cart/list";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExsit"));
			return data;
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
			return data;
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			return data;
		}
		
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("giftItems", cart.getGiftItems());
		return data;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
System.out.println("================="+id);
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExsit"));
			return data;
		}
		cartItems.remove(cartItem);
		cartItemService.delete(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("isLowStock", cart.getIsLowStock());
		data.put("count", cart.getCartItems().size());
		data.put("cartItems", cart.getCartItems());
		return data;
	}

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public String info(ModelMap model) {
		model.addAttribute("cart", cartService.getCurrent());
		return "/shop/index/cart";
	}
	
	@RequestMapping(value = "/info1", method = RequestMethod.POST)
	public String info1(ModelMap model) {
		model.addAttribute("cart", cartService.getCurrent());
		return "/shop/index/cart1";
	}

}