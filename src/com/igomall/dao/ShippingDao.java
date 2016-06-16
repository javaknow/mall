/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.Shipping;

public interface ShippingDao extends BaseDao<Shipping, Long> {

	Shipping findBySn(String sn);

}