
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.RoleControlDao;
import com.igomall.entity.RoleControl;
import com.igomall.service.RoleControlService;

@Service("roleControlServiceImpl")
public class RoleControlServiceImpl extends BaseServiceImpl<RoleControl, Long> implements RoleControlService {

	@Resource(name = "roleControlDaoImpl")
	private RoleControlDao roleControlDao;

	@Resource(name = "roleControlDaoImpl")
	public void setBaseDao(RoleControlDao roleControlDao) {
		super.setBaseDao(roleControlDao);
	}

}