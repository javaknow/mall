
package com.igomall.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.MemberDao;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.Product;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.util.AutoCodeUtils;

@Repository("memberDaoImpl")
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements MemberDao {

	public boolean usernameExists(String username) {
		if (username == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		return count > 0;
	}
	
	public boolean openIdExists(String openId) {
		if (openId == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.openId) = lower(:openId)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).getSingleResult();
		return count > 0;
	}

	public boolean emailExists(String email) {
		if (email == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	public Member findByUsername(String username) {
		if (username == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.username) = lower(:username)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Member> findListByEmail(String email) {
		if (email == null) {
			return Collections.<Member> emptyList();
		}
		String jpql = "select members from Member members where lower(members.email) = lower(:email)";
		return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getResultList();
	}

	public List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<Member> member = criteriaQuery.from(Member.class);
		Join<Product, Order> orders = member.join("orders");
		criteriaQuery.multiselect(member.get("id"), member.get("username"), member.get("email"), member.get("point"), member.get("amount"), member.get("balance"), criteriaBuilder.sum(orders.<BigDecimal> get("amountPaid")));
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(orders.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(orders.<Date> get("createDate"), endDate));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(orders.get("orderStatus"), OrderStatus.completed), criteriaBuilder.equal(orders.get("paymentStatus"), PaymentStatus.paid));
		criteriaQuery.where(restrictions);
		criteriaQuery.groupBy(member.get("id"), member.get("username"), member.get("email"), member.get("point"), member.get("amount"), member.get("balance"));
		criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(orders.<BigDecimal> get("amountPaid"))));
		TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		if (count != null && count >= 0) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public Member findByNumber(String number) {
		if (number == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.number) = lower(:number)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("number", number).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Boolean numberExists(String number) {
		if (number == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.number) = lower(:number)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("number", number).getSingleResult();
		return count > 0;
	}

	@Override
	public Page<Member> findChildrenPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), member));
		}else{
			return new Page<Member>(Collections.<Member> emptyList(), 0, pageable);
		}
		
		
		criteriaQuery.where(restrictions);
		
		return super.findPage(criteriaQuery, pageable);
	}
	
	public Member findByOpenId(String openId) {
		if (openId == null) {
			return null;
		}
System.out.println("==================="+openId);
		try {
			String jpql = "select members from Member members where lower(members.openId) = lower(:openId)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Member> findChildren(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), member));
		}else{
			return new ArrayList<Member>();
		}
		
		
		criteriaQuery.where(restrictions);
		
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public String setNumber(String memberNumberPrefix, Integer memberNumberLength) {
		String number = AutoCodeUtils.setNumber(memberNumberPrefix, memberNumberLength);
		while(numberExists(number)){
			number = AutoCodeUtils.setNumber(memberNumberPrefix, memberNumberLength);
		}
		return number;
	}

}