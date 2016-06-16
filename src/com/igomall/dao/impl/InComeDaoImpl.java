package com.igomall.dao.impl;

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
import com.igomall.dao.InComeDao;
import com.igomall.entity.InCome;
import com.igomall.entity.Member;
import com.igomall.entity.InCome.Type;

@Repository("inComeDaoImpl")
public class InComeDaoImpl extends BaseDaoImpl<InCome, Long> implements InComeDao {

    public Page<InCome> findPage(Member member, Pageable pageable) {
	if (member == null) {
	    return new Page<InCome>(Collections.<InCome> emptyList(), 0, pageable);
	}
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<InCome> criteriaQuery = criteriaBuilder.createQuery(InCome.class);
	Root<InCome> root = criteriaQuery.from(InCome.class);
	criteriaQuery.select(root);
	criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
	return super.findPage(criteriaQuery, pageable);
    }

    public List<InCome> findInCome(Member member,Type type,Date beginDate, Date endDate) {
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InCome> criteriaQuery = criteriaBuilder.createQuery(InCome.class);
		Root<InCome> root = criteriaQuery.from(InCome.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
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

    public List<InCome> findInComeList(Date beginDate, Date endDate) {
	try {
	    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	    CriteriaQuery<InCome> criteriaQuery = criteriaBuilder.createQuery(InCome.class);
	    Root<InCome> root = criteriaQuery.from(InCome.class);
	    criteriaQuery.select(root);
	    Predicate restrictions = criteriaBuilder.conjunction();
	   
	    if (beginDate != null) {
	        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
	    }
	    if (endDate != null) {
	        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
	    }
	    criteriaQuery.where(restrictions);
	    return super.findList(criteriaQuery, null, null, null, null);
	} catch (Exception e) {
	    
	    e.printStackTrace();
	    return null;
	}
    }

}