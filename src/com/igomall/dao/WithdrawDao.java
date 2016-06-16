package com.igomall.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Withdraw;
import com.igomall.entity.Withdraw.Status;

public interface WithdrawDao extends BaseDao<Withdraw, Long> {

    Page<Withdraw> findPage(Member member,Status status, Pageable pageable, Date beginDate, Date endDate);

    List<Withdraw> findList(Member member, Status status, Date beginDate, Date endDate);
    
    BigDecimal count(Member member, Status success, Date beginDate, Date endDate);

	List<Withdraw> findList(Member member);

}