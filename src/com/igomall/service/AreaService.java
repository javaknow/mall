
package com.igomall.service;

import java.util.List;

import com.igomall.entity.Area;

public interface AreaService extends BaseService<Area, Long> {

	List<Area> findRoots();

	List<Area> findRoots(Integer count);

	Area findByName(String areaName);

	List<Area> findChildren(Area parent);

}