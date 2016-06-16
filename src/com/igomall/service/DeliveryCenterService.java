/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.entity.DeliveryCenter;

public interface DeliveryCenterService extends BaseService<DeliveryCenter, Long> {

	DeliveryCenter findDefault();

}