
package com.igomall.dao.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.BonusCouponDao;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;
import com.igomall.entity.BonusCoupon.Type;

@Repository("bonusCouponDaoImpl")
public class BonusCouponDaoImpl extends BaseDaoImpl<BonusCoupon, Long> implements BonusCouponDao {

	public List<BonusCoupon> findList(Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		if (type != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	@Override
	public Page<BonusCoupon> findPage(Member member, Pageable pageable,Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<BonusCoupon> findListByShop(Shop shop,Type type,Boolean isOut,Boolean hasEnd, Boolean isAlloted) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		}else{
			return Collections.<BonusCoupon> emptyList();
		}
		
		if (isOut != null) {
			if (isOut) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("balance2").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal> get("balance1"), root.<BigDecimal> get("balance2"))));
			} else {
				restrictions = criteriaBuilder.and(restrictions, root.get("balance2").isNotNull(), criteriaBuilder.lessThan(root.<BigDecimal> get("balance1"), root.<BigDecimal> get("balance2")));
			}
		}
		
		if (hasEnd != null) {
			if (hasEnd) {
				restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("endDate"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("endDate"), new Date()));
			}
		}
	
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		if (isAlloted != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAlloted"), isAlloted));
		}
		
		criteriaQuery.where(restrictions);
		
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	@Override
	public BigDecimal countByShop(Shop shop, Type type, Boolean isOut,Boolean hasEnd, Boolean isAlloted) {
		BigDecimal balance = BigDecimal.ZERO;
		
		List<BonusCoupon> list = findListByShop(shop, type, isOut, hasEnd, isAlloted);
		for (BonusCoupon bonusCoupon : list) {
			balance = balance.add(bonusCoupon.getBalance());
		}
		return balance;
	}

	@Override
	public Page<BonusCoupon> findPage(Pageable pageable, Shop shop, Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		}
		
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<BonusCoupon> findPage(Pageable pageable, BonusCoupon bonusCoupon, Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if (bonusCoupon != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), bonusCoupon));
		}
		
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<BonusCoupon> findPage(Member member, Pageable pageable, Type type, BonusCoupon bonusCoupon) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (bonusCoupon != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), bonusCoupon));
		}
		
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public BigDecimal count(Member member, Boolean isOut, Boolean hasEnd,Shop shop, Type type, Boolean isAlloted) {
		BigDecimal balance = BigDecimal.ZERO;
		
		List<BonusCoupon> list = findList(member,isOut, hasEnd, shop, type, isAlloted);
		for (BonusCoupon bonusCoupon : list) {
			balance = balance.add(bonusCoupon.getBalance());
		}
		return balance;
	}

	public List<BonusCoupon> findList(Member member, Boolean isOut,Boolean hasEnd, Shop shop, Type type, Boolean isAlloted) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BonusCoupon> criteriaQuery = criteriaBuilder.createQuery(BonusCoupon.class);
		Root<BonusCoupon> root = criteriaQuery.from(BonusCoupon.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		}
		
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}else{
			return Collections.<BonusCoupon> emptyList();
		}
		
		if (isOut != null) {
			if (isOut) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("balance2").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal> get("balance1"), root.<BigDecimal> get("balance2"))));
			} else {
				restrictions = criteriaBuilder.and(restrictions, root.get("balance2").isNotNull(), criteriaBuilder.lessThan(root.<BigDecimal> get("balance1"), root.<BigDecimal> get("balance2")));
			}
		}
		
		if (hasEnd != null) {
			if (hasEnd) {
				restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("endDate"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("endDate"), new Date()));
			}
		}
	
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		
		if (isAlloted != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isAlloted"), isAlloted));
		}
		
		criteriaQuery.where(restrictions);
		
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

}