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
import com.igomall.dao.RedPacketDao;
import com.igomall.entity.Member;
import com.igomall.entity.RedPacket;

@Repository("redPacketDaoImpl")
public class RedPacketDaoImpl extends BaseDaoImpl<RedPacket, Long> implements RedPacketDao {

	public RedPacket findByCode(String code) {
		if (code == null) {
			return null;
		}
		String jpql = "select redPackets from RedPacket redPackets where lower(redPackets.code) = lower(:code)";
		try {
			return entityManager.createQuery(jpql, RedPacket.class)
					.setFlushMode(FlushModeType.COMMIT).setParameter("code", code)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Page<RedPacket> findPage(Member member,Pageable pageable, Date beginDate, Date endDate) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RedPacket> criteriaQuery = criteriaBuilder.createQuery(RedPacket.class);
		Root<RedPacket> root = criteriaQuery.from(RedPacket.class);
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

	public List<RedPacket> findList(Member member, Date beginDate, Date endDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RedPacket> criteriaQuery = criteriaBuilder.createQuery(RedPacket.class);
		Root<RedPacket> root = criteriaQuery.from(RedPacket.class);
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
		List<RedPacket> list = findList(member, beginDate, endDate);
		for (RedPacket redPacket : list) {
			money = money.add(redPacket.getBalance());
		}
		return money;
	}

}