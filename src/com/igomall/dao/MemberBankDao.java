
package com.igomall.dao;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.MemberBank;

public interface MemberBankDao extends BaseDao<MemberBank, Long> {

	Page<MemberBank> findPage(Member member, Pageable pageable);

	List<MemberBank> findList(Member member);


}