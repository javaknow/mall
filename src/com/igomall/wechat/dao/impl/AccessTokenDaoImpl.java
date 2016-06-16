
package com.igomall.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.igomall.dao.impl.BaseDaoImpl;
import com.igomall.wechat.dao.AccessTokenDao;
import com.igomall.wechat.entity.AccessToken;

@Repository("accessTokenDaoImpl")
public class AccessTokenDaoImpl extends BaseDaoImpl<AccessToken, Long> implements AccessTokenDao {

	
}