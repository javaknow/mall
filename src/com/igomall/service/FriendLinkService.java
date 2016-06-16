/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.FriendLink;
import com.igomall.entity.FriendLink.Type;

public interface FriendLinkService extends BaseService<FriendLink, Long> {

	List<FriendLink> findList(Type type);

	List<FriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}