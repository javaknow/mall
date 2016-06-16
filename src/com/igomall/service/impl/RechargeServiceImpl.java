package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.RechargeDao;
import com.igomall.entity.Member;
import com.igomall.entity.Recharge;
import com.igomall.entity.Recharge.Status;
import com.igomall.service.RechargeService;

@Service("rechargeServiceImpl")
public class RechargeServiceImpl extends BaseServiceImpl<Recharge, Long> implements RechargeService {

	@Resource(name = "rechargeDaoImpl")
	private RechargeDao rechargeDao;

	@Resource(name = "rechargeDaoImpl")
	public void setBaseDao(RechargeDao rechargeDao) {
		super.setBaseDao(rechargeDao);
	}

	
	
	@Transactional(readOnly = true)
	public Page<Recharge> findPage(Member member,Status status, Pageable pageable, Date beginDate, Date endDate) {
		return rechargeDao.findPage(member, status, pageable, beginDate, endDate);
	}

	public BigDecimal count(Member member, Status success, Date beginDate, Date endDate) {
		return rechargeDao.count(member, success, beginDate, endDate);
	}

	public List<Recharge> findList(Member member, Status status, Date beginDate, Date endDate) {
		return rechargeDao.findList(member, status, beginDate, endDate);
	}



	@Override
	@Transactional(readOnly = true)
	public Recharge findBySn(String sn) {
		
		return rechargeDao.findBySn(sn);
	}

}