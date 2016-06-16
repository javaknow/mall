
package com.igomall.dao;

import java.util.List;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Shop;

public interface ShopDao extends BaseDao<Shop, Long> {

	List<Shop> findList(Boolean isEnabled);

	Page<Shop> findPage(Member member, Pageable pageable);


}