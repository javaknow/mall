/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.math.BigDecimal;

import com.igomall.entity.MemberRank;

public interface MemberRankService extends BaseService<MemberRank, Long> {

	boolean nameExists(String name);

	boolean nameUnique(String previousName, String currentName);

	boolean amountExists(BigDecimal amount);

	boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount);

	MemberRank findDefault();

	MemberRank findByAmount(BigDecimal amount);

}