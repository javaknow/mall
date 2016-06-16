package com.igomall.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.RechargeDao;
import com.igomall.entity.Member;
import com.igomall.entity.Recharge;
import com.igomall.entity.Recharge.Status;

@Repository("rechargeDaoImpl")
public class RechargeDaoImpl extends BaseDaoImpl<Recharge, Long> implements RechargeDao {

	public Recharge findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select recharges from Recharge recharges where lower(recharges.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Recharge.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Page<Recharge> findPage(Member member,Status status, Pageable pageable, Date beginDate, Date endDate) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recharge> criteriaQuery = criteriaBuilder.createQuery(Recharge.class);
		Root<Recharge> root = criteriaQuery.from(Recharge.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public List<Recharge> findList(Member member, Status status, Date beginDate, Date endDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recharge> criteriaQuery = criteriaBuilder.createQuery(Recharge.class);
		Root<Recharge> root = criteriaQuery.from(Recharge.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	public BigDecimal count(Member member, Status status, Date beginDate, Date endDate) {
		BigDecimal money = new BigDecimal(0);
		List<Recharge> list = findList(member, status, beginDate, endDate);
		for (Recharge recharge : list) {
			money = money.add(recharge.getRealBalance());
		}
		return money;
	}

}