/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.entity.DeliveryTemplate;

public interface DeliveryTemplateService extends BaseService<DeliveryTemplate, Long> {

	DeliveryTemplate findDefault();

}