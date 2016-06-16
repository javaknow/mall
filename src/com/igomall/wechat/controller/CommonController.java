
package com.igomall.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.entity.Member;
import com.igomall.service.MemberService;
import com.igomall.util.DesUtils;

@Controller("wechatCommonController")
@RequestMapping("/wechat/common")
public class CommonController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = "/getDecrypt", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getDecrypt(String username) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		Member member = memberService.findByUsername(username);
		if(member==null){
			map.put("key", "");
			map.put("result","");
		}else{
			map.putAll(DesUtils.encrypt2(member.getId().toString()));
		}
		return map;
	}
}