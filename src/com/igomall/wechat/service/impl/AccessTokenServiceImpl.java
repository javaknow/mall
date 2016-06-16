
package com.igomall.wechat.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.service.impl.BaseServiceImpl;
import com.igomall.wechat.dao.AccessTokenDao;
import com.igomall.wechat.entity.AccessToken;
import com.igomall.wechat.service.AccessTokenService;

@Service("accessTokenServiceImpl")
public class AccessTokenServiceImpl extends BaseServiceImpl<AccessToken, Long> implements AccessTokenService {

	@Resource(name = "accessTokenDaoImpl")
	private AccessTokenDao accessTokenDao;

	@Resource(name = "accessTokenDaoImpl")
	public void setBaseDao(AccessTokenDao accessTokenDao) {
		super.setBaseDao(accessTokenDao);
	}
}