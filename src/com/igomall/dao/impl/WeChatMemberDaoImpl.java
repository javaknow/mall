
package com.igomall.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.igomall.dao.WeChatMemberDao;
import com.igomall.entity.Member;
import com.igomall.entity.WeChatMember;

@Repository("weChatMemberDaoImpl")
public class WeChatMemberDaoImpl extends BaseDaoImpl<WeChatMember, Long> implements WeChatMemberDao {
	
	public boolean openIdExists(String openId) {
		if (openId == null) {
			return false;
		}
		String jpql = "select count(*) from WeChatMember weChatMembers where lower(weChatMembers.openid) = lower(:openId)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).getSingleResult();
		return count > 0;
	}

	public WeChatMember findByOpenId(String openId) {
		if (openId == null||"".equals(openId)) {
			return null;
		}
		try {
			String jpql = "select weChatMembers from WeChatMember weChatMembers where lower(weChatMembers.openid) = lower(:openId)";
			return entityManager.createQuery(jpql, WeChatMember.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public WeChatMember getWeChatMember(Member member) {
		if (member == null) {
			return null;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WeChatMember> criteriaQuery = criteriaBuilder.createQuery(WeChatMember.class);
		Root<WeChatMember> root = criteriaQuery.from(WeChatMember.class);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		try {
			return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}