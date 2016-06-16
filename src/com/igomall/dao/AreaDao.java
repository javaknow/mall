
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Area;

public interface AreaDao extends BaseDao<Area, Long> {

	List<Area> findRoots(Integer count);

	Area findByName(String areaName);

	List<Area> findChildren(Area parent);

}