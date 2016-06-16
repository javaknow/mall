/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import javax.annotation.Resource;

import com.igomall.dao.ParameterDao;
import com.igomall.entity.Parameter;
import com.igomall.service.ParameterService;

import org.springframework.stereotype.Service;

@Service("parameterServiceImpl")
public class ParameterServiceImpl extends BaseServiceImpl<Parameter, Long> implements ParameterService {

	@Resource(name = "parameterDaoImpl")
	public void setBaseDao(ParameterDao parameterDao) {
		super.setBaseDao(parameterDao);
	}

}