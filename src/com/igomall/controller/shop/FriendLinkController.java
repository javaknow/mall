
package com.igomall.controller.shop;

import javax.annotation.Resource;

import com.igomall.entity.FriendLink.Type;
import com.igomall.service.FriendLinkService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("shopFriendLinkController")
@RequestMapping("/friend_link")
public class FriendLinkController extends BaseController {

	@Resource(name = "friendLinkServiceImpl")
	private FriendLinkService friendLinkService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("textFriendLinks", friendLinkService.findList(Type.text));
		model.addAttribute("imageFriendLinks", friendLinkService.findList(Type.image));
		return "/shop/friend_link/index";
	}

}