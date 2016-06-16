
package com.igomall.service;

import java.util.List;

import com.igomall.entity.ShopAttribute;

public interface ShopAttributeService extends BaseService<ShopAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<ShopAttribute> findList();

	List<ShopAttribute> findList(String cacheRegion);

}