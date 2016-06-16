
package com.igomall.controller.wap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igomall.controller.shop.BaseController;
import com.igomall.service.MemberService;

@Controller("wapHistoryController")
@RequestMapping("/wap/member/history")
public class HistoryController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
}