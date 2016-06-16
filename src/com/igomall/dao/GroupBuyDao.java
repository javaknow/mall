/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.GroupBuy;

public interface GroupBuyDao extends BaseDao<GroupBuy, Long> {

	Page<GroupBuy> findPage(Boolean isEnabled, Boolean hasExpired, Pageable pageable);

}