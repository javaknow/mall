package com.igomall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.InComeDao;
import com.igomall.entity.InCome;
import com.igomall.entity.Member;
import com.igomall.entity.InCome.Type;
import com.igomall.service.InComeService;

@Service("inComeServiceImpl")
public class InComeServiceImpl extends BaseServiceImpl<InCome, Long> implements InComeService {

    @Resource(name = "inComeDaoImpl")
    private InComeDao inComeDao;

    @Resource(name = "inComeDaoImpl")
    public void setBaseDao(InComeDao inComeDao) {
	super.setBaseDao(inComeDao);
    }

    @Transactional(readOnly = true)
    public Page<InCome> findPage(Member member, Pageable pageable) {
	return inComeDao.findPage(member, pageable);
    }

    public List<InCome> findInCome(Member member,Type type,Date beginDate, Date endDate) {
	return inComeDao.findInCome(member,type,beginDate, endDate);
    }

}