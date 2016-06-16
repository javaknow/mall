package com.igomall.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Voucher;

public interface VoucherDao extends BaseDao<Voucher, Long> {

    Page<Voucher> findPage(Member member,Pageable pageable, Date beginDate, Date endDate);

    List<Voucher> findList(Member member, Date beginDate, Date endDate);
    
    BigDecimal count(Member member, Date beginDate, Date endDate);

    Voucher findByCode(String code);

}