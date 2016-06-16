
package com.igomall.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.Pageable;
import com.igomall.controller.shop.BaseController;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Shop;
import com.igomall.entity.BonusCoupon.Type;
import com.igomall.service.BonusCouponService;
import com.igomall.service.ShopService;

@Controller("adminBonusCouponController")
@RequestMapping("/admin/bonusCoupon")
public class BonusCouponController extends BaseController {
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model,Long shopId) {
		Shop shop = shopService.find(shopId);
		model.addAttribute("page", bonusCouponService.findPage(pageable,shop, Type.member));
		return "/admin/bonusCoupon/list";
	}
	
	@RequestMapping(value = "/list1", method = RequestMethod.GET)
	public String list1(Pageable pageable, ModelMap model,Long id) {
		BonusCoupon bonusCoupon = bonusCouponService.find(id);
		model.addAttribute("page", bonusCouponService.findPage(pageable,bonusCoupon, Type.shop));
		return "/admin/bonusCoupon/list1";
	}

}