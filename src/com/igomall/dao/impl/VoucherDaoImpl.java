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
import com.igomall.dao.VoucherDao;
import com.igomall.entity.Member;
import com.igomall.entity.Voucher;

@Repository("voucherDaoImpl")
public class VoucherDaoImpl extends BaseDaoImpl<Voucher, Long> implements VoucherDao {

	public Voucher findByCode(String code) {
		if (code == null) {
			return null;
		}
		String jpql = "select vouchers from Voucher vouchers where lower(vouchers.code) = lower(:code)";
		try {
			return entityManager.createQuery(jpql, Voucher.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("code", code)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Page<Voucher> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Voucher> criteriaQuery = criteriaBuilder.createQuery(Voucher.class);
		Root<Voucher> root = criteriaQuery.from(Voucher.class);
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

	public List<Voucher> findList(Member member, Date beginDate, Date endDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Voucher> criteriaQuery = criteriaBuilder.createQuery(Voucher.class);
		Root<Voucher> root = criteriaQuery.from(Voucher.class);
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
		List<Voucher> list = findList(member, beginDate, endDate);
		for (Voucher voucher : list) {
			money = money.add(voucher.getBalance());
		}
		return money;
	}

}