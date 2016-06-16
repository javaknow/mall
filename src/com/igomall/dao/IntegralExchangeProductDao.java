
package com.igomall.dao;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.IntegralExchangeProduct;

public interface IntegralExchangeProductDao extends BaseDao<IntegralExchangeProduct, Long> {

	List<IntegralExchangeProduct> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

}