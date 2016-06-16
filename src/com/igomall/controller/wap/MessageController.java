
package com.igomall.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.controller.shop.BaseController;

@Controller("wapMemberMessageController")
@RequestMapping("/wap/member/message")
public class MessageController extends BaseController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, Model model) {
		return "/wap/tmpl/member/message/list";
	}
}