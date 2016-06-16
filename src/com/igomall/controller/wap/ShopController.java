
package com.igomall.controller.wap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igomall.service.ShopService;

@Controller("wapShopController")
@RequestMapping("/wap/shop")
public class ShopController extends BaseController {

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;


	

}