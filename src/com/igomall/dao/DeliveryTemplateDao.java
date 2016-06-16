/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.DeliveryTemplate;

public interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate, Long> {

	DeliveryTemplate findDefault();

}