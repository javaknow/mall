
package com.igomall.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.ShopDao;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;

@Repository("shopDaoImpl")
public class ShopDaoImpl extends BaseDaoImpl<Shop, Long> implements ShopDao {

	@Override
	public List<Shop> findList(Boolean isEnabled) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shop> criteriaQuery = criteriaBuilder.createQuery(Shop.class);
		Root<Shop> root = criteriaQuery.from(Shop.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (isEnabled != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
		}
		
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<Shop> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<Shop>(Collections.<Shop> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shop> criteriaQuery = criteriaBuilder.createQuery(Shop.class);
		Root<Shop> root = criteriaQuery.from(Shop.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));
		return super.findPage(criteriaQuery, pageable);
	}


}