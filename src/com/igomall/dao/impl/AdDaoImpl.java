
package com.igomall.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.AdDao;
import com.igomall.entity.Ad;
import com.igomall.entity.AdPosition;
import com.igomall.entity.Ad.Category;

@Repository("adDaoImpl")
public class AdDaoImpl extends BaseDaoImpl<Ad, Long> implements AdDao {

	@Override
	public List<Ad> findList(AdPosition adPosition, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Ad> criteriaQuery = criteriaBuilder.createQuery(Ad.class);
		Root<Ad> root = criteriaQuery.from(Ad.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (adPosition == null) {
			return Collections.<Ad> emptyList();
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("adPosition"), adPosition));
		
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, first, count, filters, orders);
	}

	@Override
	public Page<Ad> findPage(Pageable pageable, Category category) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Ad> criteriaQuery = criteriaBuilder.createQuery(Ad.class);
		Root<Ad> root = criteriaQuery.from(Ad.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (category != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("category"), category));
		}
		
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}