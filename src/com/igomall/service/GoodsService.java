
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Goods;

public interface GoodsService extends BaseService<Goods, Long> {

	List<Goods> findList(Integer count, List<Filter> filters, List<Order> orders, int index);

}