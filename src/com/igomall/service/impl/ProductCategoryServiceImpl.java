
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.igomall.dao.ProductCategoryDao;
import com.igomall.entity.Ad;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Shop;
import com.igomall.service.ProductCategoryService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("productCategoryServiceImpl")
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory, Long> implements ProductCategoryService {

	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;

	@Resource(name = "productCategoryDaoImpl")
	public void setBaseDao(ProductCategoryDao productCategoryDao) {
		super.setBaseDao(productCategoryDao);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Boolean isEnabled) {
		return productCategoryDao.findRoots(null,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count,Boolean isEnabled) {
		return productCategoryDao.findRoots(count,isEnabled);
	}

	@Transactional(readOnly = true)
	@Cacheable("productCategory")
	public List<ProductCategory> findRoots(Integer count, String cacheRegion,Boolean isEnabled) {
		return productCategoryDao.findRoots(count,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory,Boolean isEnabled) {
		return productCategoryDao.findParents(productCategory, null,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory, Integer count,Boolean isEnabled) {
		return productCategoryDao.findParents(productCategory, count,isEnabled);
	}

	@Transactional(readOnly = true)
	@Cacheable("productCategory")
	public List<ProductCategory> findParents(ProductCategory productCategory, Integer count, String cacheRegion,Boolean isEnabled) {
		return productCategoryDao.findParents(productCategory, count,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findTree(Boolean isEnabled) {
		return productCategoryDao.findChildren(null, null,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory,Boolean isEnabled) {
		return productCategoryDao.findChildren(productCategory, null,isEnabled);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory, Integer count,Boolean isEnabled) {
		return productCategoryDao.findChildren(productCategory, count,isEnabled);
	}

	@Transactional(readOnly = true)
	@Cacheable("productCategory")
	public List<ProductCategory> findChildren(ProductCategory productCategory, Integer count, String cacheRegion,Boolean isEnabled) {
		return productCategoryDao.findChildren(productCategory, count,isEnabled);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void save(ProductCategory productCategory) {
		super.save(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory) {
		return super.update(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory, String... ignoreProperties) {
		return super.update(productCategory, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(ProductCategory productCategory) {
		super.delete(productCategory);
	}

	@Override
	public List<ProductCategory> findDirectChildren(ProductCategory productCategory, Integer count, String cacheRegion,Boolean isEnabled) {
		return productCategoryDao.findDirectChildren(productCategory, count,isEnabled);
	}

	@Override
	public List<ProductCategory> findDirectChildren(ProductCategory productCategory, Integer count,Boolean isEnabled) {
		return productCategoryDao.findDirectChildren(productCategory, count,isEnabled);
	}

	@Override
	public List<ProductCategory> findAll(boolean isEnabled) {
		return productCategoryDao.findAll(isEnabled);
	}

	@Override
	public List<ProductCategory> findTree(ProductCategory productCategory,Boolean isEnabled) {
		return productCategoryDao.findChildren(productCategory, null,isEnabled);
	}

	@Override
	public List<ProductCategory> findAll(Shop shop, Boolean flag) {
		return productCategoryDao.findAll(shop,flag);
	}

	@Override
	public List<ProductCategory> findRoots(Shop shop, Boolean isEnabled) {
		return productCategoryDao.findRoots(shop,null,isEnabled);
	}

}