
package com.igomall.service;

import java.util.List;

import com.igomall.entity.Menu;

public interface MenuService extends BaseService<Menu, Long> {

	List<Menu> findRoots();

	List<Menu> findRoots(Integer count);
	
	List<Menu> findRoots(Boolean isEnabled);

	List<Menu> findTree(Boolean isEnabled);

	List<Menu> findChildren(Menu menu, Boolean isEnabled);

}