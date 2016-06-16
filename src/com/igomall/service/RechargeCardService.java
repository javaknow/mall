package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.RechargeCard;

public interface RechargeCardService extends BaseService<RechargeCard, Long> {

	Page<RechargeCard> findPage(Member member, Pageable pageable, Date beginDate, Date endDate);

	List<RechargeCard> findList(Member member, Date beginDate, Date endDate);

	BigDecimal count(Member member, Date beginDate, Date endDate);

	RechargeCard findByCode(String code);

}