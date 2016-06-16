
package com.igomall.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("shopInviteController")
@RequestMapping("/invite")
public class InviteController extends BaseController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		return "/shop/invite";
	}
	
}