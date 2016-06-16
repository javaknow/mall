/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.MemberAttribute;

public interface MemberAttributeDao extends BaseDao<MemberAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<MemberAttribute> findList();

}