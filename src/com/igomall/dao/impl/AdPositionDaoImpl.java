
package com.igomall.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.AdPositionDao;
import com.igomall.entity.AdPosition;
import com.igomall.entity.AdPosition.Type;

import org.springframework.stereotype.Repository;

@Repository("adPositionDaoImpl")
public class AdPositionDaoImpl extends BaseDaoImpl<AdPosition, Long> implements AdPositionDao {

	@Override
	public Page<AdPosition> findPage(Pageable pageable, Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdPosition> criteriaQuery = criteriaBuilder.createQuery(AdPosition.class);
		Root<AdPosition> root = criteriaQuery.from(AdPosition.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(type!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.where(restrictions);
		
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<AdPosition> findList(Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdPosition> criteriaQuery = criteriaBuilder.createQuery(AdPosition.class);
		Root<AdPosition> root = criteriaQuery.from(AdPosition.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(type!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.where(restrictions);
		
		return super.findList(criteriaQuery, null, null, null, null);
	}

}