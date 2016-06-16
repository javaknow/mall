
package com.igomall.dao;

import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;

public interface MemberDao extends BaseDao<Member, Long> {

	boolean usernameExists(String username);

	boolean openIdExists(String openId);
	
	boolean emailExists(String email);

	Member findByUsername(String username);

	List<Member> findListByEmail(String email);

	List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count);

	Member findByNumber(String number);

	Boolean numberExists(String number);

	Page<Member> findChildrenPage(Member member, Pageable pageable);

	Member findByOpenId(String openId);

	List<Member> findChildren(Member member);

	/**
	 * 生成会员编号
	 * @param memberNumberPrefix 编号的前缀
	 * @param memberNumberLength 编号的长度
	 * @return
	 */
	public String setNumber(String memberNumberPrefix, Integer memberNumberLength);
}