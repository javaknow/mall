package com.igomall.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.ShopDao;
import com.igomall.entity.Admin;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;
import com.igomall.service.ShopService;

@Service("shopServiceImpl")
public class ShopServiceImpl extends BaseServiceImpl<Shop, Long> implements ShopService {

	@Resource(name = "shopDaoImpl")
	private ShopDao shopDao;

	@Resource(name = "shopDaoImpl")
	public void setBaseDao(ShopDao shopDao) {
		super.setBaseDao(shopDao);
	}

	@Override
	public void update(Shop shop, Admin admin) {
		Assert.notNull(shop);
		shopDao.lock(shop, LockModeType.PESSIMISTIC_WRITE);
		shopDao.merge(shop);
		
	}

	@Override
	public void save(Shop shop, Admin admin) {
		Assert.notNull(shop);
		shopDao.persist(shop);
	}

	@Override
	public List<Shop> findList(Boolean isEnabled) {
		return shopDao.findList(isEnabled);
	}

	@Override
	public Page<Shop> findPage(Member member, Pageable pageable) {
		return shopDao.findPage(member,pageable);
	}


}