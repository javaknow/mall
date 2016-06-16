
package com.igomall.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.dao.AreaDao;
import com.igomall.entity.Area;

@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {

	public List<Area> findRoots(Integer count) {
		String jpql = "select area from Area area where area.parent is null order by area.order asc";
		TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public Area findByName(String areaName) {
		if (areaName == null) {
			return null;
		}
		try {
			String jpql = "select area from Area area where lower(area.name) = lower(:name)";
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("name", areaName).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Area> findChildren(Area parent) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Area> criteriaQuery = criteriaBuilder.createQuery(Area.class);
		Root<Area> root = criteriaQuery.from(Area.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (parent == null) {
			return Collections.<Area> emptyList();
		}else{
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), parent));
		}
		
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null,null,null,null);
	}

}