/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.math.BigDecimal;

import com.igomall.entity.MemberRank;

public interface MemberRankDao extends BaseDao<MemberRank, Long> {

	boolean nameExists(String name);

	boolean amountExists(BigDecimal amount);

	MemberRank findDefault();

	MemberRank findByAmount(BigDecimal amount);

}