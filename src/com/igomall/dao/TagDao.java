/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Tag;
import com.igomall.entity.Tag.Type;

public interface TagDao extends BaseDao<Tag, Long> {

	List<Tag> findList(Type type);

}