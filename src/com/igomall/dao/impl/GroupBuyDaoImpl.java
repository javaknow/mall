/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.GroupBuyDao;
import com.igomall.entity.GroupBuy;

import org.springframework.stereotype.Repository;

@Repository("groupBuyDaoImpl")
public class GroupBuyDaoImpl extends BaseDaoImpl<GroupBuy, Long> implements GroupBuyDao {

	public Page<GroupBuy> findPage(Boolean isEnabled, Boolean hasExpired, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GroupBuy> criteriaQuery = criteriaBuilder.createQuery(GroupBuy.class);
		Root<GroupBuy> root = criteriaQuery.from(GroupBuy.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (isEnabled != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("endDate").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("endDate"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("endDate").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("endDate"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}