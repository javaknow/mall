
package com.igomall.dao;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.MoneyFlowingWater;

public interface MoneyFlowingWaterDao extends BaseDao<MoneyFlowingWater, Long> {

	Page<MoneyFlowingWater> findPage(Member member, Pageable pageable);

}