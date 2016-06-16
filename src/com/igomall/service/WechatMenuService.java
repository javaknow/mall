
package com.igomall.service;

import java.util.List;

import com.igomall.entity.WechatMenu;

public interface WechatMenuService extends BaseService<WechatMenu, Long> {

	List<WechatMenu> findRoots();

	List<WechatMenu> findRoots(Integer count);
	
	List<WechatMenu> findRoots(Boolean isEnabled);

	List<WechatMenu> findTree(Boolean isEnabled);

	List<WechatMenu> findChildren(WechatMenu wechatMenu, Boolean isEnabled);

}