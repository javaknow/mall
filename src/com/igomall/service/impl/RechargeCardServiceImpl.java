package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.RechargeCardDao;
import com.igomall.entity.Member;
import com.igomall.entity.RechargeCard;
import com.igomall.service.RechargeCardService;

@Service("rechargeCardServiceImpl")
public class RechargeCardServiceImpl extends BaseServiceImpl<RechargeCard, Long> implements RechargeCardService {

	@Resource(name = "rechargeCardDaoImpl")
	private RechargeCardDao rechargeCardDao;

	@Resource(name = "rechargeCardDaoImpl")
	public void setBaseDao(RechargeCardDao rechargeCardDao) {
		super.setBaseDao(rechargeCardDao);
	}

	
	
	@Transactional(readOnly = true)
	public Page<RechargeCard> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		return rechargeCardDao.findPage(member, pageable, beginDate, endDate);
	}

	public BigDecimal count(Member member,  Date beginDate, Date endDate) {
		return rechargeCardDao.count(member,beginDate, endDate);
	}

	public List<RechargeCard> findList(Member member, Date beginDate, Date endDate) {
		return rechargeCardDao.findList(member, beginDate, endDate);
	}



	@Override
	@Transactional(readOnly = true)
	public RechargeCard findByCode(String code) {
		return rechargeCardDao.findByCode(code);
	}

}