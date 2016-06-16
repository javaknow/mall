
package com.igomall.wechat.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Message;
import com.igomall.Principal;
import com.igomall.Setting;
import com.igomall.Setting.AccountLockType;
import com.igomall.entity.Cart;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;
import com.igomall.service.CaptchaService;
import com.igomall.service.CartService;
import com.igomall.service.MemberService;
import com.igomall.service.RSAService;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.SettingUtils;
import com.igomall.util.WebUtils;

@Controller("wechatLoginController")
@RequestMapping("/wechat/login")
public class LoginController extends BaseController {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;


	@Resource(name = "weChatMemberServiceImpl")
	private WeChatMemberService weChatMemberService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		Setting setting = SettingUtils.get();
		if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
			redirectUrl = null;
		}
		return "/wechat/login/index";
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> submit(String username, String password, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", 200);
		
		Member member;
		Setting setting = SettingUtils.get();
		
		member = memberService.findByUsername(username); 
		
		if (member == null) {
			data.put("msg",Message.error("shop.login.unknownAccount"));
			return data;
		}
		if (!member.getIsEnabled()) {
			data.put("msg", Message.error("shop.login.disabledAccount"));
			return data;
		}
		if (member.getIsLocked()) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					data.put("msg", Message.error("shop.login.lockedAccount"));
					return data;
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(false);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					data.put("msg", Message.error("shop.login.lockedAccount"));
					return data;
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(false);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}

		if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(true);
				member.setLockedDate(new Date());
			}
			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				data.put("msg", Message.error("shop.login.accountLockCount", setting.getAccountLockCount()));
				return data;
			} else {
				data.put("msg", Message.error("shop.login.incorrectCredentials"));
				return data;
			}
		}
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		memberService.update(member);

		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
		}

		Map<String, Object> attributes = new HashMap<String, Object>();
		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		WeChatMember weChatMember = weChatMemberService.getWeChatMember(member);
		if(weChatMember!=null){
			session.setAttribute(WeChatMember.PRINCIPAL_ATTRIBUTE_NAME, new Principal(weChatMember.getId(), weChatMember.getOpenid()));
			WebUtils.addCookie(request, response, WeChatMember.OPEN_COOKIE_NAME, weChatMember.getOpenid());
		}

		data.put("msg", SUCCESS_MESSAGE);
		return data;
	}

}