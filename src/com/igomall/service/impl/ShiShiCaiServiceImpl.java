/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import javax.annotation.Resource;

import com.igomall.dao.ShiShiCaiDao;
import com.igomall.entity.ShiShiCai;
import com.igomall.service.ShiShiCaiService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("shiShiCaiServiceImpl")
public class ShiShiCaiServiceImpl extends BaseServiceImpl<ShiShiCai, Long> implements ShiShiCaiService {

	@Resource(name = "shiShiCaiDaoImpl")
	private ShiShiCaiDao shiShiCaiDao;

	@Resource(name = "shiShiCaiDaoImpl")
	public void setBaseDao(ShiShiCaiDao shiShiCaiDao) {
		super.setBaseDao(shiShiCaiDao);
	}

	@Transactional(readOnly = true)
	@Cacheable("shiShiCai")
	public ShiShiCai find(Long id, String cacheRegion) {
		return shiShiCaiDao.find(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public void save(ShiShiCai shiShiCai) {
		super.save(shiShiCai);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public ShiShiCai update(ShiShiCai shiShiCai) {
		return super.update(shiShiCai);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public ShiShiCai update(ShiShiCai shiShiCai, String... ignoreProperties) {
		return super.update(shiShiCai, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "shiShiCai", allEntries = true)
	public void delete(ShiShiCai shiShiCai) {
		super.delete(shiShiCai);
	}

}