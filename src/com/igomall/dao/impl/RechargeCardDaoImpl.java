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
import com.igomall.dao.RechargeCardDao;
import com.igomall.entity.Member;
import com.igomall.entity.RechargeCard;

@Repository("rechargeCardDaoImpl")
public class RechargeCardDaoImpl extends BaseDaoImpl<RechargeCard, Long> implements RechargeCardDao {

	public RechargeCard findByCode(String code) {
		if (code == null) {
			return null;
		}
		String jpql = "select rechargeCards from RechargeCard rechargeCards where lower(rechargeCards.code) = lower(:code)";
		try {
			return entityManager.createQuery(jpql, RechargeCard.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("code", code)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Page<RechargeCard> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RechargeCard> criteriaQuery = criteriaBuilder.createQuery(RechargeCard.class);
		Root<RechargeCard> root = criteriaQuery.from(RechargeCard.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
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

	public List<RechargeCard> findList(Member member, Date beginDate, Date endDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RechargeCard> criteriaQuery = criteriaBuilder.createQuery(RechargeCard.class);
		Root<RechargeCard> root = criteriaQuery.from(RechargeCard.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
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

	public BigDecimal count(Member member, Date beginDate, Date endDate) {
		BigDecimal money = new BigDecimal(0);
		List<RechargeCard> list = findList(member, beginDate, endDate);
		for (RechargeCard rechargeCard : list) {
			money = money.add(rechargeCard.getBalance());
		}
		return money;
	}

}