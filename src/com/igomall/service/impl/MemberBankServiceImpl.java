
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.MemberBankDao;
import com.igomall.entity.Member;
import com.igomall.entity.MemberBank;
import com.igomall.service.MemberBankService;

@Service("memberBankServiceImpl")
public class MemberBankServiceImpl extends BaseServiceImpl<MemberBank, Long> implements MemberBankService {

	@Resource(name = "memberBankDaoImpl")
	private MemberBankDao memberBankDao;

	@Resource(name = "memberBankDaoImpl")
	public void setBaseDao(MemberBankDao memberBankDao) {
		super.setBaseDao(memberBankDao);
	}

	@Override
	public Page<MemberBank> findPage(Member member, Pageable pageable) {
		// TODO Auto-generated method stub
		return memberBankDao.findPage(member,pageable);
	}

	@Override
	public List<MemberBank> findList(Member member) {
		// TODO Auto-generated method stub
		return memberBankDao.findList(member);
	}


}