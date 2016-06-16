
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.ProductCategory;
import com.igomall.entity.Shop;

public interface ProductCategoryDao extends BaseDao<ProductCategory, Long> {

	/**
	 * 查找产品的一级分类
	 * @param count 查找的数量
	 * @return
	 */
	List<ProductCategory> findRoots(Integer count,Boolean isEnabled);

	/**
	 * 查找某个分类的上级分类
	 * @param productCategory
	 * @param count
	 * @return
	 */
	List<ProductCategory> findParents(ProductCategory productCategory, Integer count,Boolean isEnabled);

	/**
	 * 查找某个分类的下级分类
	 * @param productCategory
	 * @param count
	 * @return
	 */
	List<ProductCategory> findChildren(ProductCategory productCategory, Integer count,Boolean isEnabled);

	/**
	 * 查找某个分类下的直接子分类
	 * @param productCategory 
	 * @param count
	 * @return
	 */
	List<ProductCategory> findDirectChildren(ProductCategory productCategory,Integer count,Boolean isEnabled);

	List<ProductCategory> findAll(Boolean isEnabled);

	List<ProductCategory> findAll(Shop shop, Boolean flag);

	List<ProductCategory> findRoots(Shop shop, Integer count, Boolean isEnabled);

}