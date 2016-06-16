package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.VoucherDao;
import com.igomall.entity.Member;
import com.igomall.entity.Voucher;
import com.igomall.service.VoucherService;

@Service("voucherServiceImpl")
public class VoucherServiceImpl extends BaseServiceImpl<Voucher, Long> implements VoucherService {

	@Resource(name = "voucherDaoImpl")
	private VoucherDao voucherDao;

	@Resource(name = "voucherDaoImpl")
	public void setBaseDao(VoucherDao voucherDao) {
		super.setBaseDao(voucherDao);
	}

	
	
	@Transactional(readOnly = true)
	public Page<Voucher> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		return voucherDao.findPage(member, pageable, beginDate, endDate);
	}

	public BigDecimal count(Member member,  Date beginDate, Date endDate) {
		return voucherDao.count(member,beginDate, endDate);
	}

	public List<Voucher> findList(Member member, Date beginDate, Date endDate) {
		return voucherDao.findList(member, beginDate, endDate);
	}



	@Override
	@Transactional(readOnly = true)
	public Voucher findByCode(String code) {
		return voucherDao.findByCode(code);
	}

}