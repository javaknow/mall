
package com.igomall.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.MoneyFlowingWater;
import com.igomall.entity.MoneyFlowingWater.Type;

public interface MoneyFlowingWaterService extends BaseService<MoneyFlowingWater, Long> {

	void create(Member member,BigDecimal balance, Type type, String content,HttpServletRequest request);

	Page<MoneyFlowingWater> findPage(Member member, Pageable pageable);
}