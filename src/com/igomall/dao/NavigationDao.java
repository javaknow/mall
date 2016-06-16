/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Navigation;
import com.igomall.entity.Navigation.Position;

public interface NavigationDao extends BaseDao<Navigation, Long> {

	List<Navigation> findList(Position position);

}