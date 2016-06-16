
package com.igomall.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.dao.SpecificationValueDao;
import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;

@Repository("specificationValueDaoImpl")
public class SpecificationValueDaoImpl extends BaseDaoImpl<SpecificationValue, Long> implements SpecificationValueDao {

	@Override
	public SpecificationValue findByTaoBaoId(Specification specification,Long taobaoId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpecificationValue> criteriaQuery = criteriaBuilder.createQuery(SpecificationValue.class);
		Root<SpecificationValue> root = criteriaQuery.from(SpecificationValue.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (taobaoId == null) {
			return null;
		}
		if(specification!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("specification"), specification));
		}
		
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("taobaoId"), taobaoId));
		
		criteriaQuery.where(restrictions);
		List<SpecificationValue> list = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
		if(list==null||list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
		
	}

}