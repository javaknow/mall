
package com.igomall.service;

import java.util.Date;
import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Point;
import com.igomall.entity.Point.Type;

public interface PointService extends BaseService<Point, Long> {

	List<Point> findList(Member member, Type type,Date beginDate,Date endDate);
	
	Page<Point> findPage(Member member, Type type,Date beginDate,Date endDate,Pageable pageable);

}