
package com.igomall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.igomall.dao.WechatMenuDao;
import com.igomall.entity.WechatMenu;

@Repository("wechatMenuDaoImpl")
public class WechatMenuDaoImpl extends BaseDaoImpl<WechatMenu, Long> implements WechatMenuDao {

	public List<WechatMenu> findRoots(Integer count) {
		String jpql = "select wechatMenu from Menu wechatMenu where wechatMenu.parent is null order by wechatMenu.order asc";
		TypedQuery<WechatMenu> query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<WechatMenu> findChildren(WechatMenu parent, Integer count,Boolean isEnabled) {
		TypedQuery<WechatMenu> query;
		if (parent != null) {
			String jpql = "select wechatMenu from WechatMenu wechatMenu where wechatMenu.treePath like :treePath order by wechatMenu.order asc";
			if(isEnabled!=null){
				jpql = "select wechatMenu from WechatMenu wechatMenu where wechatMenu.treePath like :treePath and wechatMenu.isEnabled =:isEnabled order by wechatMenu.order asc";
				query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + WechatMenu.TREE_PATH_SEPARATOR + parent.getId() + WechatMenu.TREE_PATH_SEPARATOR + "%").setParameter("isEnabled", isEnabled);
			}else{
				query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + WechatMenu.TREE_PATH_SEPARATOR + parent.getId() + WechatMenu.TREE_PATH_SEPARATOR + "%");
			}
		} else {
			String jpql = "select wechatMenu from WechatMenu wechatMenu order by wechatMenu.order asc";
			if(isEnabled!=null){
				jpql = "select wechatMenu from Menu wechatMenu where wechatMenu.isEnabled =:isEnabled order by wechatMenu.order asc";
				query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT).setParameter("isEnabled", isEnabled);
			}else{
				query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT);
			}
		}
		
		
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), parent);
	}
	
	private List<WechatMenu> sort(List<WechatMenu> wechatMenus, WechatMenu parent) {
		List<WechatMenu> result = new ArrayList<WechatMenu>();
		if (wechatMenus != null) {
			for (WechatMenu wechatMenu : wechatMenus) {
				if ((wechatMenu.getParent() != null && wechatMenu.getParent().equals(parent)) || (wechatMenu.getParent() == null && parent == null)) {
					result.add(wechatMenu);
					result.addAll(sort(wechatMenus, wechatMenu));
				}
			}
		}
		return result;
	}
	
	
	@Override
	public void persist(WechatMenu wechatMenu) {
		Assert.notNull(wechatMenu);
		setValue(wechatMenu);
		super.persist(wechatMenu);
	}

	@Override
	public WechatMenu merge(WechatMenu wechatMenu) {
		Assert.notNull(wechatMenu);
		setValue(wechatMenu);
		for (WechatMenu wechatMenu1 : findChildren(wechatMenu, null,null)) {
			setValue(wechatMenu1);
		}
		return super.merge(wechatMenu);
	}
	
	private void setValue(WechatMenu wechatMenu) {
		if (wechatMenu == null) {
			return;
		}
		WechatMenu parent = wechatMenu.getParent();
		if (parent != null) {
			wechatMenu.setTreePath(parent.getTreePath() + parent.getId() + WechatMenu.TREE_PATH_SEPARATOR);
		} else {
			wechatMenu.setTreePath(WechatMenu.TREE_PATH_SEPARATOR);
		}
		wechatMenu.setGrade(wechatMenu.getTreePaths().size());
	}

	@Override
	public List<WechatMenu> findRoots(Boolean isEnabled) {
		String jpql = "select wechatMenu from WechatMenu wechatMenu where wechatMenu.parent is null and isEnabled =:isEnabled order by wechatMenu.order asc";
		TypedQuery<WechatMenu> query = entityManager.createQuery(jpql, WechatMenu.class).setFlushMode(FlushModeType.COMMIT).setParameter("isEnabled", isEnabled);;
		return query.getResultList();
	}
}