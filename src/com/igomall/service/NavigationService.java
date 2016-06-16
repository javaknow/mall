/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Navigation;
import com.igomall.entity.Navigation.Position;

public interface NavigationService extends BaseService<Navigation, Long> {

	List<Navigation> findList(Position position);

	List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}