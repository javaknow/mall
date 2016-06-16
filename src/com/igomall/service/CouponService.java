/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Coupon;

public interface CouponService extends BaseService<Coupon, Long> {

	Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

}