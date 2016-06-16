
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.dao.WechatMenuDao;
import com.igomall.entity.WechatMenu;
import com.igomall.service.WechatMenuService;

@Service("wechatMenuServiceImpl")
public class WechatMenuServiceImpl extends BaseServiceImpl<WechatMenu, Long> implements WechatMenuService {

	@Resource(name = "wechatMenuDaoImpl")
	private WechatMenuDao wechatMenuDao;

	@Resource(name = "wechatMenuDaoImpl")
	public void setBaseDao(WechatMenuDao wechatMenuDao) {
		super.setBaseDao(wechatMenuDao);
	}

	@Transactional(readOnly = true)
	public List<WechatMenu> findRoots() {
		return wechatMenuDao.findRoots(100);
	}

	@Transactional(readOnly = true)
	public List<WechatMenu> findRoots(Integer count) {
		return wechatMenuDao.findRoots(count);
	}

	@Override
	@Transactional
	public void save(WechatMenu wechatMenu) {
		super.save(wechatMenu);
	}

	@Override
	@Transactional
	public WechatMenu update(WechatMenu wechatMenu) {
		return super.update(wechatMenu);
	}

	@Override
	@Transactional
	public WechatMenu update(WechatMenu wechatMenu, String... ignoreProperties) {
		return super.update(wechatMenu, ignoreProperties);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(WechatMenu wechatMenu) {
		super.delete(wechatMenu);
	}

	@Transactional(readOnly = true)
	public List<WechatMenu> findTree(Boolean isEnabled) {
		return wechatMenuDao.findChildren(null, null,isEnabled);
	}

	@Override
	public List<WechatMenu> findChildren(WechatMenu wechatMenu, Boolean isEnabled) {
		return wechatMenuDao.findChildren(wechatMenu,null,isEnabled);
	}

	@Override
	public List<WechatMenu> findRoots(Boolean isEnabled) {
		return wechatMenuDao.findRoots(isEnabled);
	}
}