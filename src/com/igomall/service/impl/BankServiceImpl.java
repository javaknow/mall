
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.BankDao;
import com.igomall.entity.Bank;
import com.igomall.service.BankService;

@Service("bankServiceImpl")
public class BankServiceImpl extends BaseServiceImpl<Bank, Long> implements BankService {

	@Resource(name = "bankDaoImpl")
	private BankDao bankDao;

	@Resource(name = "bankDaoImpl")
	public void setBaseDao(BankDao bankDao) {
		super.setBaseDao(bankDao);
	}

	@Override
	public List<Bank> findAll(Boolean isEnabled) {
		return bankDao.findAll(isEnabled);
	}

}