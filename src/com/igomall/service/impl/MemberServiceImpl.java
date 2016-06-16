package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.Principal;
import com.igomall.Setting;
import com.igomall.dao.AreaDao;
import com.igomall.dao.DepositDao;
import com.igomall.dao.MemberDao;
import com.igomall.dao.MemberRankDao;
import com.igomall.dao.WeChatMemberDao;
import com.igomall.entity.Admin;
import com.igomall.entity.Deposit;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;
import com.igomall.service.MemberService;
import com.igomall.util.SettingUtils;

@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements
		MemberService {

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;
	
	@Resource(name = "weChatMemberDaoImpl")
	private WeChatMemberDao weChatMemberDao;

	@Resource(name = "memberDaoImpl")
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.usernameExists(username);
	}
	
	@Transactional(readOnly = true)
	public boolean openIdExists(String openId) {
		return memberDao.usernameExists(openId);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);
		Setting setting = SettingUtils.get();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		} else {
			if (memberDao.emailExists(currentEmail)) {
				return false;
			} else {
				return true;
			}
		}
	}

	public void save(Member member, Admin operator) {
		Assert.notNull(member);
		memberDao.persist(member);
		if (member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
			deposit.setCredit(member.getBalance());
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			depositDao.persist(deposit);
		}
	}

	public void update(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) {
		Assert.notNull(member);
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		if (modifyPoint != null && modifyPoint != 0 && member.getPoint() + modifyPoint >= 0) {
			member.setPoint(member.getPoint() + modifyPoint);
		}
		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0 && member.getBalance().add(modifyBalance).compareTo(new BigDecimal(0)) >= 0) {
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			if (modifyBalance.compareTo(new BigDecimal(0)) > 0) {
				deposit.setType(operator != null ? Deposit.Type.adminRecharge: Deposit.Type.memberRecharge);
				deposit.setCredit(modifyBalance);
				deposit.setDebit(new BigDecimal(0));
			} else {
				deposit.setType(operator != null ? Deposit.Type.adminChargeback: Deposit.Type.memberPayment);
				deposit.setCredit(new BigDecimal(0));
				deposit.setDebit(modifyBalance);
			}
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername(): null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			depositDao.persist(deposit);
		}
		memberDao.merge(member);
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public List<Object[]> findPurchaseList(Date beginDate, Date endDate,
			Integer count) {
		return memberDao.findPurchaseList(beginDate, endDate, count);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return memberDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}

	@Override
	public Member findByNumber(String number) {
		return memberDao.findByNumber(number);
	}

	@Transactional(readOnly = true)
	public Boolean numberDisabled(String number) {
		/*Assert.hasText(number);
		Setting setting = SettingUtils.get();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(number, disabledUsername)) {
					return true;
				}
			}
		}*/
		return false;
	}

	@Override
	public Boolean numberExists(String number) {
		return memberDao.numberExists(number);
	}

	


	@Override
	public Page<Member> findChildrenPage(Member member, Pageable pageable) {
		return memberDao.findChildrenPage(member,pageable);
	}
	
	@Transactional(readOnly = true)
	public Member findByOpenId(String openId) {
		return memberDao.findByOpenId(openId);
	}

	@Override
	public List<Member> findChildren(Member member) {
		return memberDao.findChildren(member);
	}

	@Override
	public Member createMember(WeChatMember weChatMember) {
		
		Member member = new Member();
		Setting setting = SettingUtils.get();
		member.setNumber(setNumber(setting.getMemberNumberPrefix(),setting.getMemberNumberLength()));
		member.setPassword(DigestUtils.md5Hex(setting.getDefaultUserPassword()));
		member.setMemberRank(memberRankDao.findDefault());
		member.setUsername(member.getNumber());
		member.setEmail(member.getUsername()+"@igomall.com");
		member.setParent(null);
		member.setWeChatMember(weChatMember);
		member.setOpenId(weChatMember.getOpenid());
		memberDao.persist(member);
		
		weChatMember.setMember(member);
		weChatMemberDao.merge(weChatMember);
		return member;
		
		
	}

	@Override
	public String setNumber(String memberNumberPrefix, Integer memberNumberLength) {
		return memberDao.setNumber(memberNumberPrefix, memberNumberLength);
	}


}