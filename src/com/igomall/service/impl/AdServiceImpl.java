
package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.AdDao;
import com.igomall.entity.Ad;
import com.igomall.entity.AdPosition;
import com.igomall.entity.Ad.Category;
import com.igomall.service.AdService;

@Service("adServiceImpl")
public class AdServiceImpl extends BaseServiceImpl<Ad, Long> implements AdService {

	@Resource(name = "adDaoImpl")
	private AdDao adDao;
	
	@Resource(name = "adDaoImpl")
	public void setBaseDao(AdDao adDao) {
		super.setBaseDao(adDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void save(Ad ad) {
		super.save(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad) {
		return super.update(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad, String... ignoreProperties) {
		return super.update(ad, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Ad ad) {
		super.delete(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public List<Ad> findList(AdPosition adPosition, Integer first, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return adDao.findList(adPosition, first, count, filters, orders);
	}

	@Override
	public List<Ad> findList(AdPosition adPosition, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		return adDao.findList(adPosition, first, count, filters, orders);
	}

	@Override
	public Page<Ad> findPage(Pageable pageable, Category category) {
		return adDao.findPage(pageable, category);
	}

}