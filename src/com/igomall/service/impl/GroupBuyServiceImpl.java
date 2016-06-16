/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.GroupBuyDao;
import com.igomall.entity.GroupBuy;
import com.igomall.service.GroupBuyService;

@Service("groupBuyServiceImpl")
public class GroupBuyServiceImpl extends BaseServiceImpl<GroupBuy, Long> implements GroupBuyService {

	@Resource(name = "groupBuyDaoImpl")
	private GroupBuyDao groupBuyDao;

	@Resource(name = "groupBuyDaoImpl")
	public void setBaseDao(GroupBuyDao groupBuyDao) {
		super.setBaseDao(groupBuyDao);
	}

	@Transactional(readOnly = true)
	public Page<GroupBuy> findPage(Boolean isEnabled, Boolean hasExpired, Pageable pageable) {
		return groupBuyDao.findPage(isEnabled, hasExpired, pageable);
	}

}