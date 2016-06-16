/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Coupon;

public interface CouponDao extends BaseDao<Coupon, Long> {

	Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

}