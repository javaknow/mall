
package com.igomall.dao.impl;

import java.util.Collections;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.MoneyFlowingWaterDao;
import com.igomall.entity.Member;
import com.igomall.entity.MoneyFlowingWater;

@Repository("moneyFlowingWaterDaoImpl")
public class MoneyFlowingWaterDaoImpl extends BaseDaoImpl<MoneyFlowingWater, Long> implements MoneyFlowingWaterDao {

	@Override
	public Page<MoneyFlowingWater> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<MoneyFlowingWater>(Collections.<MoneyFlowingWater> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MoneyFlowingWater> criteriaQuery = criteriaBuilder.createQuery(MoneyFlowingWater.class);
		Root<MoneyFlowingWater> root = criteriaQuery.from(MoneyFlowingWater.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}