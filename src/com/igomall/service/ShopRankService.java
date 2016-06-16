/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.math.BigDecimal;

import com.igomall.entity.ShopRank;

public interface ShopRankService extends BaseService<ShopRank, Long> {

	boolean nameExists(String name);

	boolean nameUnique(String previousName, String currentName);

	boolean amountExists(BigDecimal amount);

	boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount);

	ShopRank findDefault();

	ShopRank findByAmount(BigDecimal amount);

}