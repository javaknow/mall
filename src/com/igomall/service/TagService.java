/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Tag;
import com.igomall.entity.Tag.Type;

public interface TagService extends BaseService<Tag, Long> {

	List<Tag> findList(Type type);

	List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}