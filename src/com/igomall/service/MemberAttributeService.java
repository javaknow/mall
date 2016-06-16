/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.entity.MemberAttribute;

public interface MemberAttributeService extends BaseService<MemberAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<MemberAttribute> findList();

	List<MemberAttribute> findList(String cacheRegion);

}