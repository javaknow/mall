
package com.igomall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.Order;
import com.igomall.dao.BankDao;
import com.igomall.entity.Bank;

@Repository("bankDaoImpl")
public class BankDaoImpl extends BaseDaoImpl<Bank, Long> implements BankDao {

	@Override
	public List<Bank> findAll(Boolean isEnabled) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Bank> criteriaQuery = criteriaBuilder.createQuery(Bank.class);
		Root<Bank> root = criteriaQuery.from(Bank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		
		if(isEnabled!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
		}
		
		
		criteriaQuery.where(restrictions);
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("order"));
		return super.findList(criteriaQuery, null, null, null, orders);
	}

}