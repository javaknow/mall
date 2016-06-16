/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.GroupBuy;

public interface GroupBuyService extends BaseService<GroupBuy, Long> {

	Page<GroupBuy> findPage(Boolean isEnabled, Boolean hasExpired, Pageable pageable);

}