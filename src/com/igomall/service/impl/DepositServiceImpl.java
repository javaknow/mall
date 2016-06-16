
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.DepositDao;
import com.igomall.entity.Deposit;
import com.igomall.entity.Deposit.Type;
import com.igomall.entity.Member;
import com.igomall.service.DepositService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("depositServiceImpl")
public class DepositServiceImpl extends BaseServiceImpl<Deposit, Long> implements DepositService {

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "depositDaoImpl")
	public void setBaseDao(DepositDao depositDao) {
		super.setBaseDao(depositDao);
	}

	@Transactional(readOnly = true)
	public Page<Deposit> findPage(Member member, Pageable pageable) {
		return depositDao.findPage(member, pageable);
	}

	@Override
	public List<Deposit> findReChageList(Member member) {
		return depositDao.findReChageList(member);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Deposit> findRechargePage(Member member, Pageable pageable) {
		return depositDao.findRechargePage(member, pageable);
	}

	@Override
	public List<Deposit> findList(Member member,Type type) {
		return depositDao.findList(member,type);
	}

}