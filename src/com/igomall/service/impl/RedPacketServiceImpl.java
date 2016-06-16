package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.RedPacketDao;
import com.igomall.entity.Member;
import com.igomall.entity.RedPacket;
import com.igomall.service.RedPacketService;

@Service("redPacketServiceImpl")
public class RedPacketServiceImpl extends BaseServiceImpl<RedPacket, Long> implements RedPacketService {

	@Resource(name = "redPacketDaoImpl")
	private RedPacketDao redPacketDao;

	@Resource(name = "redPacketDaoImpl")
	public void setBaseDao(RedPacketDao redPacketDao) {
		super.setBaseDao(redPacketDao);
	}

	
	
	@Transactional(readOnly = true)
	public Page<RedPacket> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		return redPacketDao.findPage(member, pageable, beginDate, endDate);
	}

	public BigDecimal count(Member member,  Date beginDate, Date endDate) {
		return redPacketDao.count(member,beginDate, endDate);
	}

	public List<RedPacket> findList(Member member, Date beginDate, Date endDate) {
		return redPacketDao.findList(member, beginDate, endDate);
	}



	@Override
	@Transactional(readOnly = true)
	public RedPacket findByCode(String code) {
		return redPacketDao.findByCode(code);
	}

}