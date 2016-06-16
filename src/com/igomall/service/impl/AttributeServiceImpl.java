/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import javax.annotation.Resource;

import com.igomall.dao.AttributeDao;
import com.igomall.entity.Attribute;
import com.igomall.service.AttributeService;

import org.springframework.stereotype.Service;

@Service("attributeServiceImpl")
public class AttributeServiceImpl extends BaseServiceImpl<Attribute, Long> implements AttributeService {

	@Resource(name = "attributeDaoImpl")
	public void setBaseDao(AttributeDao attributeDao) {
		super.setBaseDao(attributeDao);
	}

}