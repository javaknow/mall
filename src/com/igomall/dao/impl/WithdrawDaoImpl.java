package com.igomall.dao.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.WithdrawDao;
import com.igomall.entity.Member;
import com.igomall.entity.Withdraw;
import com.igomall.entity.Withdraw.Status;

@Repository("withdrawDaoImpl")
public class WithdrawDaoImpl extends BaseDaoImpl<Withdraw, Long> implements WithdrawDao {

	public Page<Withdraw> findPage(Member member,Status status, Pageable pageable, Date beginDate, Date endDate) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Withdraw> criteriaQuery = criteriaBuilder.createQuery(Withdraw.class);
		Root<Withdraw> root = criteriaQuery.from(Withdraw.class);
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

	public List<Withdraw> findList(Member member, Status status, Date beginDate, Date endDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Withdraw> criteriaQuery = criteriaBuilder.createQuery(Withdraw.class);
		Root<Withdraw> root = criteriaQuery.from(Withdraw.class);
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
		List<Withdraw> list = findList(member, status, beginDate, endDate);
		for (Withdraw withdraw : list) {
			money = money.add(withdraw.getRealBalance());
		}
		return money;
	}

	@Override
	public List<Withdraw> findList(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Withdraw> criteriaQuery = criteriaBuilder.createQuery(Withdraw.class);
		Root<Withdraw> root = criteriaQuery.from(Withdraw.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}else{
			return Collections.<Withdraw> emptyList();
		}

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

}