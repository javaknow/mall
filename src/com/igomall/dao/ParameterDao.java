/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;
import java.util.Set;

import com.igomall.entity.Parameter;
import com.igomall.entity.ParameterGroup;

public interface ParameterDao extends BaseDao<Parameter, Long> {

	List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes);

}