
package com.igomall.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;
import com.igomall.entity.BonusCoupon.Type;

public interface BonusCouponService extends BaseService<BonusCoupon, Long> {

	List<BonusCoupon> findList(Type type);

	List<BonusCoupon> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	void create(com.igomall.entity.Order order, Type type,HttpServletRequest request);

	Page<BonusCoupon> findPage(Member member, Pageable pageable,Type type);

	List<BonusCoupon> findListByShop(Shop shop,Type type, Boolean isOut,Boolean hasEnd, Boolean isAlloted);

	BigDecimal countByShop(Shop shop, Type member,Boolean isOut,Boolean hasEnd, Boolean isAlloted);

	void create(BonusCoupon parent, Type type, BigDecimal balance);

	Page<BonusCoupon> findPage(Pageable pageable, Shop shop,Type type);

	Page<BonusCoupon> findPage(Pageable pageable, BonusCoupon bonusCoupon, Type type);

	Page<BonusCoupon> findPage(Member member, Pageable pageable, Type type, BonusCoupon bonusCoupon);

	void deveryBonusCoupns();

	BigDecimal count(Member member, Boolean isOut,Boolean hasEnd, Shop shop,Type type,Boolean isAlloted);
	
	List<BonusCoupon> findList(Member member, Boolean isOut,Boolean hasEnd, Shop shop, Type type, Boolean isAlloted);

}