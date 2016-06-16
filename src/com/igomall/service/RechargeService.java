package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Recharge;
import com.igomall.entity.Recharge.Status;

public interface RechargeService extends BaseService<Recharge, Long> {

	Page<Recharge> findPage(Member member, Status status, Pageable pageable, Date beginDate, Date endDate);

	List<Recharge> findList(Member member, Status status, Date beginDate, Date endDate);

	BigDecimal count(Member member, Status success, Date beginDate, Date endDate);

	Recharge findBySn(String sn);

}