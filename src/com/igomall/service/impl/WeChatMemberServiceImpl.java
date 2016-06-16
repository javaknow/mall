package com.igomall.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.igomall.Principal;
import com.igomall.Setting;
import com.igomall.dao.MemberDao;
import com.igomall.dao.MemberRankDao;
import com.igomall.dao.WeChatMemberDao;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.util.GetAccessTokenUtil;

@Service("weChatMemberServiceImpl")
public class WeChatMemberServiceImpl extends BaseServiceImpl<WeChatMember, Long> implements WeChatMemberService {

	@Resource(name = "weChatMemberDaoImpl")
	private WeChatMemberDao weChatMemberDao;
	
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Resource(name = "weChatMemberDaoImpl")
	public void setBaseDao(WeChatMemberDao weChatMemberDao) {
		super.setBaseDao(weChatMemberDao);
	}

	
	@Transactional(readOnly = true)
	public boolean openIdExists(String openId) {
		return weChatMemberDao.openIdExists(openId);
	}

	@Transactional(readOnly = true)
	public WeChatMember findByOpenId(String openId) {
		return weChatMemberDao.findByOpenId(openId);
	}


	@Override
	public WeChatMember create(String fromUserName,Member parent) {
		//请求微信获取用户信息
		com.igomall.wechat.entity.WeChatMember weChatMember = GetAccessTokenUtil.getMemberInfo(fromUserName);
		WeChatMember weChatMember1 = new WeChatMember();
		weChatMember1.copyProperties(weChatMember);
		weChatMemberDao.persist(weChatMember1);
		
		Member member = memberDao.findByOpenId(weChatMember1.getOpenid());
		
		if(member==null){//用户也不存在
			member = new Member();
			Setting setting = SettingUtils.get();
			member.setNumber(memberDao.setNumber(setting.getMemberNumberPrefix(),setting.getMemberNumberLength()));
			member.setPassword(DigestUtils.md5Hex(setting.getDefaultUserPassword()));
			member.setMemberRank(memberRankDao.findDefault());
			member.setUsername(member.getNumber());
			member.setEmail(member.getUsername()+"@igomall.com");
			member.setParent(parent);
			member.setWeChatMember(weChatMember1);
			member.setOpenId(fromUserName);
			memberDao.persist(member);
		}else{
			member.setWeChatMember(weChatMember1);
			member.setOpenId(fromUserName);
			memberDao.merge(member);
		}
		weChatMember1.setMember(member);
		weChatMemberDao.merge(weChatMember1);
		
		return weChatMember1;
	}

	@Override
	public WeChatMember update(WeChatMember weChatMember) {
		// 请求微信获取用户信息
		com.igomall.wechat.entity.WeChatMember weChatMember1 = GetAccessTokenUtil.getMemberInfo(weChatMember.getOpenid());
	
		weChatMember.copyProperties(weChatMember1);
		try {
			weChatMemberDao.merge(weChatMember);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return weChatMember;
	}


	@Override
	public WeChatMember getWeChatMember(Member member) {
		return weChatMemberDao.getWeChatMember(member);
	}


	@Transactional(readOnly = true)
	public WeChatMember getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(WeChatMember.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return weChatMemberDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentOpenid() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(WeChatMember.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}


}