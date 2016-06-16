/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.DeliveryCenter;

public interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long> {

	DeliveryCenter findDefault();

}