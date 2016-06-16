
package com.igomall.service;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.AdPosition;
import com.igomall.entity.AdPosition.Type;

public interface AdPositionService extends BaseService<AdPosition, Long> {

	AdPosition find(Long id, String cacheRegion);

	Page<AdPosition> findPage(Pageable pageable, Type type);

	List<AdPosition> findList(com.igomall.entity.AdPosition.Type phone);

}