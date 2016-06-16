
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.ShopAttribute;

public interface ShopAttributeDao extends BaseDao<ShopAttribute, Long> {

	Integer findUnusedPropertyIndex();

	List<ShopAttribute> findList();

}