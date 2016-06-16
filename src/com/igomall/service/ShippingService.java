/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.Map;

import com.igomall.entity.Shipping;

public interface ShippingService extends BaseService<Shipping, Long> {

	Shipping findBySn(String sn);

	Map<String, Object> query(Shipping shipping);

}