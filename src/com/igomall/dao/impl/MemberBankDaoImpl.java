
package com.igomall.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.MemberBankDao;
import com.igomall.entity.Member;
import com.igomall.entity.MemberBank;

@Repository("memberBankDaoImpl")
public class MemberBankDaoImpl extends BaseDaoImpl<MemberBank, Long> implements MemberBankDao {

	@Override
	public Page<MemberBank> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberBank> criteriaQuery = criteriaBuilder.createQuery(MemberBank.class);
		Root<MemberBank> root = criteriaQuery.from(MemberBank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
	
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<MemberBank> findList(Member member) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberBank> criteriaQuery = criteriaBuilder.createQuery(MemberBank.class);
		Root<MemberBank> root = criteriaQuery.from(MemberBank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
	
		return super.findList(criteriaQuery,null,null,null,null);
	}


}