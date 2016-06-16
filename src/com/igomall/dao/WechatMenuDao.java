
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.WechatMenu;

public interface WechatMenuDao extends BaseDao<WechatMenu, Long> {

	List<WechatMenu> findRoots(Integer count);

	List<WechatMenu> findChildren(WechatMenu parent, Integer count, Boolean isEnabled);

	List<WechatMenu> findRoots(Boolean isEnabled);

}