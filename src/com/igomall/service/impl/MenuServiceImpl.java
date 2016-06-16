
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.dao.MenuDao;
import com.igomall.entity.Menu;
import com.igomall.service.MenuService;

@Service("menuServiceImpl")
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

	@Resource(name = "menuDaoImpl")
	private MenuDao menuDao;

	@Resource(name = "menuDaoImpl")
	public void setBaseDao(MenuDao menuDao) {
		super.setBaseDao(menuDao);
	}

	@Transactional(readOnly = true)
	public List<Menu> findRoots() {
		return menuDao.findRoots(100);
	}

	@Transactional(readOnly = true)
	public List<Menu> findRoots(Integer count) {
		return menuDao.findRoots(count);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public void save(Menu menu) {
		super.save(menu);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public Menu update(Menu menu) {
		return super.update(menu);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public Menu update(Menu menu, String... ignoreProperties) {
		return super.update(menu, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public void delete(Menu menu) {
		super.delete(menu);
	}

	@Transactional(readOnly = true)
	public List<Menu> findTree(Boolean isEnabled) {
		return menuDao.findChildren(null, null,isEnabled);
	}

	@Override
	public List<Menu> findChildren(Menu menu, Boolean isEnabled) {
		return menuDao.findChildren(menu,null,isEnabled);
	}

	@Override
	public List<Menu> findRoots(Boolean isEnabled) {
		return menuDao.findRoots(isEnabled);
	}
}