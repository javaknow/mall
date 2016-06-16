/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Coupon;
import com.igomall.entity.CouponCode;
import com.igomall.entity.Member;

public interface CouponCodeService extends BaseService<CouponCode, Long> {

	boolean codeExists(String code);

	CouponCode findByCode(String code);

	CouponCode build(Coupon coupon, Member member);

	List<CouponCode> build(Coupon coupon, Member member, Integer count);

	CouponCode exchange(Coupon coupon, Member member);

	Page<CouponCode> findPage(Member member, Pageable pageable);

	Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

}