
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.entity.Brand;
import com.igomall.entity.ProductCategory;

public interface BrandService extends BaseService<Brand, Long> {

	List<Brand> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	List<Brand> findList(List<ProductCategory> productCategorys, Integer count,String cacheRegion);

	List<Brand> findList(List<ProductCategory> productCategorys, Integer count);

	List<Brand> findByPinYin(Character c);

}