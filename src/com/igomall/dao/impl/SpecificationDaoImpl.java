
package com.igomall.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.igomall.dao.SpecificationDao;
import com.igomall.entity.Specification;
import com.igomall.entity.Specification.Type;

@Repository("specificationDaoImpl")
public class SpecificationDaoImpl extends BaseDaoImpl<Specification, Long> implements SpecificationDao {

	@Override
	public Specification findByTaoBaoId(Long taobaoId) {
		if (taobaoId == null) {
			return null;
		}
		try {
			String jpql = "select specifications from Specification specifications where lower(specifications.taobaoId) = lower(:taobaoId)";
			return entityManager.createQuery(jpql, Specification.class).setFlushMode(FlushModeType.COMMIT).setParameter("taobaoId", taobaoId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Specification createSpecificationTaoBaoId(Long taobaoId) {
		Specification specification = new Specification();
		specification.setName(taobaoId+"");
		specification.setTaobaoId(taobaoId);
		specification.setType(Type.text);
		specification.setProducts(null);
		specification.setMemo(taobaoId+"");
		persist(specification);
		
		return specification;
	}

}