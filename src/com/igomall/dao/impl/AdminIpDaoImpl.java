
package com.igomall.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.igomall.dao.AdminIpDao;
import com.igomall.entity.AdminIp;

@Repository("adminIpDaoImpl")
public class AdminIpDaoImpl extends BaseDaoImpl<AdminIp, Long> implements AdminIpDao {

	public boolean ipExists(String ip) {
		if (ip == null) {
			return false;
		}
		String jpql = "select count(*) from AdminIp adminIp where lower(adminIp.ip) = lower(:ip)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("ip", ip).getSingleResult();
		return count > 0;
	}

	public AdminIp findByIp(String ip) {
		if (ip == null) {
			return null;
		}
		try {
			String jpql = "select adminIp from AdminIp adminIp where lower(adminIp.ip) = lower(:ip)";
			return entityManager.createQuery(jpql, AdminIp.class).setFlushMode(FlushModeType.COMMIT).setParameter("ip", ip).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}