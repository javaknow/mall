/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.FriendLink;
import com.igomall.entity.FriendLink.Type;

public interface FriendLinkDao extends BaseDao<FriendLink, Long> {

	List<FriendLink> findList(Type type);

}