/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.entity.Seo;
import com.igomall.entity.Seo.Type;

public interface SeoService extends BaseService<Seo, Long> {

	Seo find(Type type);

	Seo find(Type type, String cacheRegion);

}