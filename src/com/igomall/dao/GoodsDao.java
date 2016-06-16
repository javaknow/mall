
package com.igomall.dao;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Goods;

public interface GoodsDao extends BaseDao<Goods, Long> {

	List<Goods> findList(Integer count, List<Filter> filters,List<Order> orders, int index);

}