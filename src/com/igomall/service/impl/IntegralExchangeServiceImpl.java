
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.IntegralExchangeDao;
import com.igomall.entity.IntegralExchange;
import com.igomall.service.IntegralExchangeService;

@Service("integralExchangeServiceImpl")
public class IntegralExchangeServiceImpl extends BaseServiceImpl<IntegralExchange, Long> implements IntegralExchangeService {

	@Resource(name = "integralExchangeDaoImpl")
	private IntegralExchangeDao integralExchangeDao;
	
	@Resource(name = "integralExchangeDaoImpl")
	public void setBaseDao(IntegralExchangeDao integralExchangeDao) {
		super.setBaseDao(integralExchangeDao);
	}

}