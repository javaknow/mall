package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.WithdrawDao;
import com.igomall.entity.Member;
import com.igomall.entity.Withdraw;
import com.igomall.entity.Withdraw.Status;
import com.igomall.service.WithdrawService;

@Service("withdrawServiceImpl")
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw, Long> implements WithdrawService {

	@Resource(name = "withdrawDaoImpl")
	private WithdrawDao withdrawDao;

	@Resource(name = "withdrawDaoImpl")
	public void setBaseDao(WithdrawDao withdrawDao) {
		super.setBaseDao(withdrawDao);
	}

	@Transactional(readOnly = true)
	public Page<Withdraw> findPage(Member member,Status status, Pageable pageable, Date beginDate, Date endDate) {
		return withdrawDao.findPage(member, status, pageable, beginDate, endDate);
	}

	public BigDecimal count(Member member, Status success, Date beginDate, Date endDate) {
		return withdrawDao.count(member, success, beginDate, endDate);
	}

	public List<Withdraw> findList(Member member, Status status, Date beginDate, Date endDate) {
		return withdrawDao.findList(member, status, beginDate, endDate);
	}

	@Override
	public List<Withdraw> findList(Member member) {
		return withdrawDao.findList(member);
	}

}