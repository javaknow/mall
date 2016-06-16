
package com.igomall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.igomall.dao.MenuDao;
import com.igomall.entity.Menu;

@Repository("menuDaoImpl")
public class MenuDaoImpl extends BaseDaoImpl<Menu, Long> implements MenuDao {

	public List<Menu> findRoots(Integer count) {
		String jpql = "select menu from Menu menu where menu.parent is null order by menu.order asc";
		TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<Menu> findChildren(Menu parent, Integer count,Boolean isEnabled) {
		TypedQuery<Menu> query;
		if (parent != null) {
			String jpql = "select menu from Menu menu where menu.treePath like :treePath order by menu.order asc";
			if(isEnabled!=null){
				jpql = "select menu from Menu menu where menu.treePath like :treePath and menu.isEnabled =:isEnabled order by menu.order asc";
				query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + Menu.TREE_PATH_SEPARATOR + parent.getId() + Menu.TREE_PATH_SEPARATOR + "%").setParameter("isEnabled", isEnabled);
			}else{
				query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + Menu.TREE_PATH_SEPARATOR + parent.getId() + Menu.TREE_PATH_SEPARATOR + "%");
			}
		} else {
			String jpql = "select menu from Menu menu order by menu.order asc";
			if(isEnabled!=null){
				jpql = "select menu from Menu menu where menu.isEnabled =:isEnabled order by menu.order asc";
				query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT).setParameter("isEnabled", isEnabled);
			}else{
				query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT);
			}
		}
		
		
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), parent);
	}
	
	private List<Menu> sort(List<Menu> menus, Menu parent) {
		List<Menu> result = new ArrayList<Menu>();
		if (menus != null) {
			for (Menu menu : menus) {
				if ((menu.getParent() != null && menu.getParent().equals(parent)) || (menu.getParent() == null && parent == null)) {
					result.add(menu);
					result.addAll(sort(menus, menu));
				}
			}
		}
		return result;
	}
	
	
	@Override
	public void persist(Menu menu) {
		Assert.notNull(menu);
		setValue(menu);
		super.persist(menu);
	}

	@Override
	public Menu merge(Menu menu) {
		Assert.notNull(menu);
		setValue(menu);
		for (Menu menu1 : findChildren(menu, null,null)) {
			setValue(menu1);
		}
		return super.merge(menu);
	}
	
	private void setValue(Menu menu) {
		if (menu == null) {
			return;
		}
		Menu parent = menu.getParent();
		if (parent != null) {
			menu.setTreePath(parent.getTreePath() + parent.getId() + Menu.TREE_PATH_SEPARATOR);
		} else {
			menu.setTreePath(Menu.TREE_PATH_SEPARATOR);
		}
		menu.setGrade(menu.getTreePaths().size());
	}

	@Override
	public List<Menu> findRoots(Boolean isEnabled) {
		String jpql = "select menu from Menu menu where menu.parent is null and isEnabled =:isEnabled order by menu.order asc";
		TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class).setFlushMode(FlushModeType.COMMIT).setParameter("isEnabled", isEnabled);;
		return query.getResultList();
	}
}