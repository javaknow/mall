
package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Admin;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;

public interface MemberService extends BaseService<Member, Long> {

	boolean usernameExists(String username);

	boolean openIdExists(String openId);

	boolean usernameDisabled(String username);

	boolean emailExists(String email);

	boolean emailUnique(String previousEmail, String currentEmail);

	void save(Member member, Admin operator);

	void update(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator);

	Member findByUsername(String username);

	List<Member> findListByEmail(String email);

	List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count);

	boolean isAuthenticated();

	Member getCurrent();

	String getCurrentUsername();

	/**
	 * 根据会员编号查找会员
	 * @param parentNumber
	 * @return
	 */
	Member findByNumber(String number);

	Boolean numberDisabled(String number);

	Boolean numberExists(String number);

	Page<Member> findChildrenPage(Member member, Pageable pageable);

	Member findByOpenId(String openId);

	List<Member> findChildren(Member member);

	Member createMember(WeChatMember weChatMember);

	String setNumber(String memberNumberPrefix, Integer memberNumberLength);

}