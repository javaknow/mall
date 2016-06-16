
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Menu;

public interface MenuDao extends BaseDao<Menu, Long> {

	List<Menu> findRoots(Integer count);

	List<Menu> findChildren(Menu parent, Integer count, Boolean isEnabled);

	List<Menu> findRoots(Boolean isEnabled);

}