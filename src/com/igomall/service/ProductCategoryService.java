
package com.igomall.service;

import java.util.List;

import com.igomall.entity.ProductCategory;
import com.igomall.entity.Shop;

public interface ProductCategoryService extends BaseService<ProductCategory, Long> {

	List<ProductCategory> findRoots(Boolean isEnabled);

	List<ProductCategory> findRoots(Integer count,Boolean isEnabled);

	List<ProductCategory> findRoots(Integer count, String cacheRegion,Boolean isEnabled);

	List<ProductCategory> findParents(ProductCategory productCategory,Boolean isEnabled);

	List<ProductCategory> findParents(ProductCategory productCategory, Integer count,Boolean isEnabled);

	List<ProductCategory> findParents(ProductCategory productCategory, Integer count, String cacheRegion,Boolean isEnabled);

	/**
	 * 排序树
	 * @return
	 */
	List<ProductCategory> findTree(Boolean isEnabled);

	List<ProductCategory> findChildren(ProductCategory productCategory,Boolean isEnabled);

	List<ProductCategory> findChildren(ProductCategory productCategory, Integer count,Boolean isEnabled);

	List<ProductCategory> findChildren(ProductCategory productCategory, Integer count, String cacheRegion,Boolean isEnabled);

	List<ProductCategory> findDirectChildren(ProductCategory productCategory,Integer count, String cacheRegion,Boolean isEnabled);

	List<ProductCategory> findDirectChildren(ProductCategory productCategory,Integer count,Boolean isEnabled);

	/**
	 * 查找分类
	 * @param isEnabled true:启用 false 禁用
	 * @return
	 */
	List<ProductCategory> findAll(boolean isEnabled);

	List<ProductCategory> findTree(ProductCategory productCategory, Boolean isEnabled);

	List<ProductCategory> findAll(Shop shop, Boolean isEnabled);

	List<ProductCategory> findRoots(Shop shop, Boolean isEnabled);

}