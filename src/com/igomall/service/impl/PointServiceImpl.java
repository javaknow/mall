
package com.igomall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.PointDao;
import com.igomall.entity.Member;
import com.igomall.entity.Point;
import com.igomall.entity.Point.Type;
import com.igomall.service.PointService;

@Service("pointServiceImpl")
public class PointServiceImpl extends BaseServiceImpl<Point, Long> implements PointService {

	@Resource(name = "pointDaoImpl")
	private PointDao pointDao;
	
	@Resource(name = "pointDaoImpl")
	public void setBaseDao(PointDao pointDao) {
		super.setBaseDao(pointDao);
	}

	@Override
	public List<Point> findList(Member member, Type type, Date beginDate, Date endDate) {
		return pointDao.findList(member, type, beginDate, endDate);
	}

	@Override
	public Page<Point> findPage(Member member, Type type, Date beginDate, Date endDate, Pageable pageable) {
		return pointDao.findPage(member, type, beginDate, endDate, pageable);
	}
	
	

}