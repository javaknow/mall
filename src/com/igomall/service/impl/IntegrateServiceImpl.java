
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.IntegrateDao;
import com.igomall.entity.Integrate;
import com.igomall.entity.Member;
import com.igomall.service.IntegrateService;

@Service("integrateServiceImpl")
public class IntegrateServiceImpl extends BaseServiceImpl<Integrate, Long> implements IntegrateService {

	@Resource(name = "integrateDaoImpl")
	private IntegrateDao integrateDao;

	@Resource(name = "integrateDaoImpl")
	public void setBaseDao(IntegrateDao integrateDao) {
		super.setBaseDao(integrateDao);
	}

	@Transactional(readOnly = true)
	public Page<Integrate> findPage(Member member, Pageable pageable) {
		return integrateDao.findPage(member, pageable);
	}

	@Override
	public List<Integrate> findAll(Member member) {
		return integrateDao.findAll(member);
	}

}