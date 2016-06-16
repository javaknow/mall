
package com.igomall.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.IntegrateDao;
import com.igomall.entity.Integrate;
import com.igomall.entity.Member;

@Repository("integrateDaoImpl")
public class IntegrateDaoImpl extends BaseDaoImpl<Integrate, Long> implements IntegrateDao {

	public Page<Integrate> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<Integrate>(Collections.<Integrate> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Integrate> criteriaQuery = criteriaBuilder.createQuery(Integrate.class);
		Root<Integrate> root = criteriaQuery.from(Integrate.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<Integrate> findAll(Member member) {
		if (member == null) {
			return Collections.<Integrate> emptyList();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Integrate> criteriaQuery = criteriaBuilder.createQuery(Integrate.class);
		Root<Integrate> root = criteriaQuery.from(Integrate.class);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findList(criteriaQuery, null, null, null, null);
	}

}