
package com.igomall.dao;

import java.math.BigDecimal;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;
import com.igomall.entity.BonusCoupon.Type;

public interface BonusCouponDao extends BaseDao<BonusCoupon, Long> {

	List<BonusCoupon> findList(Type type);

	Page<BonusCoupon> findPage(Member member, Pageable pageable,Type type);

	List<BonusCoupon> findListByShop(Shop shop,Type type, Boolean isOut, Boolean hasEnd, Boolean isAlloted);

	BigDecimal countByShop(Shop shop, Type type, Boolean isOut, Boolean hasEnd, Boolean isAlloted);

	Page<BonusCoupon> findPage(Pageable pageable, Shop shop, Type type);

	Page<BonusCoupon> findPage(Pageable pageable, BonusCoupon bonusCoupon, Type type);

	Page<BonusCoupon> findPage(Member member, Pageable pageable, Type type, BonusCoupon bonusCoupon);

	BigDecimal count(Member member, Boolean isOut, Boolean hasEnd, Shop shop, Type type, Boolean isAlloted);
	
	List<BonusCoupon> findList(Member member, Boolean isOut,Boolean hasEnd, Shop shop, Type type, Boolean isAlloted);

}