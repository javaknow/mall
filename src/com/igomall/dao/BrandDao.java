
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.Brand;
import com.igomall.entity.ProductCategory;

public interface BrandDao extends BaseDao<Brand, Long> {

	List<Brand> findList(List<ProductCategory> productCategorys, Integer count);

	List<Brand> findByPinYin(Character c);

}