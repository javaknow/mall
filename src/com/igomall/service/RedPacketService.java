package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.RedPacket;

public interface RedPacketService extends BaseService<RedPacket, Long> {

	Page<RedPacket> findPage(Member member, Pageable pageable, Date beginDate, Date endDate);

	List<RedPacket> findList(Member member, Date beginDate, Date endDate);

	BigDecimal count(Member member, Date beginDate, Date endDate);

	RedPacket findByCode(String code);

}