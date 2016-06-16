/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.math.BigDecimal;

import com.igomall.entity.ShopRank;

public interface ShopRankDao extends BaseDao<ShopRank, Long> {

	boolean nameExists(String name);

	boolean amountExists(BigDecimal amount);

	ShopRank findDefault();

	ShopRank findByAmount(BigDecimal amount);

}