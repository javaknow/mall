
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.dao.IntegralExchangeProductDao;
import com.igomall.entity.IntegralExchangeProduct;
import com.igomall.service.IntegralExchangeProductService;

@Service("integralExchangeProductServiceImpl")
public class IntegralExchangeProductServiceImpl extends BaseServiceImpl<IntegralExchangeProduct, Long> implements IntegralExchangeProductService {

	@Resource(name = "integralExchangeProductDaoImpl")
	private IntegralExchangeProductDao integralEProductxchangeDao;
	
	@Resource(name = "integralExchangeProductDaoImpl")
	public void setBaseDao(IntegralExchangeProductDao integralExchangeProductDao) {
		super.setBaseDao(integralExchangeProductDao);
	}

	@Override
	public List<IntegralExchangeProduct> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
		return integralEProductxchangeDao.findList(hasBegun,hasEnded, count, filters, orders);
	}

}