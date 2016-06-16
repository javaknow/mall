/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.Seo;
import com.igomall.entity.Seo.Type;

public interface SeoDao extends BaseDao<Seo, Long> {

	Seo find(Type type);

}