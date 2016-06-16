
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Ad;
import com.igomall.entity.AdPosition;
import com.igomall.entity.Ad.Category;

public interface AdService extends BaseService<Ad, Long> {

	List<Ad> findList(AdPosition adPosition, Integer first, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	List<Ad> findList(AdPosition adPosition, Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<Ad> findPage(Pageable pageable, Category category);

}