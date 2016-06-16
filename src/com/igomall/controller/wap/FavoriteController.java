
package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberFavoriteController")
@RequestMapping("/wap/member/favorites")
public class FavoriteController extends BaseController {
	
	@RequestMapping(value = "/store", method = RequestMethod.GET)
	public String favorites_store(ModelMap model) {
		return "/wap/tmpl/member/favorites/store";
	}
	
	@RequestMapping(value="/favorites",method = RequestMethod.GET)
	public String favorites(ModelMap model) {
		return "/wap/tmpl/member/favorites/favorites";
	}
}