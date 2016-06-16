
package com.igomall.service;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Admin;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;

public interface ShopService extends BaseService<Shop, Long> {

	void update(Shop shop, Admin admin);

	void save(Shop shop, Admin admin);

	/**
	 * 
	 * @param isEnabled
	 * @return
	 */
	List<Shop> findList(Boolean isEnabled);

	Page<Shop> findPage(Member member, Pageable pageable);
	
	

}