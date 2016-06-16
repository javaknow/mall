package com.igomall.service;

import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.InCome;
import com.igomall.entity.Member;
import com.igomall.entity.InCome.Type;

public interface InComeService extends BaseService<InCome, Long> {

    Page<InCome> findPage(Member member, Pageable pageable);

    List<InCome> findInCome(Member member, Type type, Date beginDate, Date endDate);

}