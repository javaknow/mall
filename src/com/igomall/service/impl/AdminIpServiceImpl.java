
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.dao.AdminIpDao;
import com.igomall.entity.AdminIp;
import com.igomall.service.AdminIpService;

@Service("adminIpServiceImpl")
public class AdminIpServiceImpl extends BaseServiceImpl<AdminIp, Long> implements AdminIpService {

	@Resource(name = "adminIpDaoImpl")
	private AdminIpDao adminIpDao;

	@Resource(name = "adminIpDaoImpl")
	public void setBaseDao(AdminIpDao adminIpDao) {
		super.setBaseDao(adminIpDao);
	}

	@Transactional(readOnly = true)
	public boolean ipExists(String ip) {
		return adminIpDao.ipExists(ip);
	}

	@Transactional(readOnly = true)
	public AdminIp findByIp(String ip) {
		return adminIpDao.findByIp(ip);
	}
}