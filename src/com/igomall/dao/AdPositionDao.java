
package com.igomall.dao;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.AdPosition;
import com.igomall.entity.AdPosition.Type;

public interface AdPositionDao extends BaseDao<AdPosition, Long> {

	Page<AdPosition> findPage(Pageable pageable, Type type);

	List<AdPosition> findList(Type type);

}