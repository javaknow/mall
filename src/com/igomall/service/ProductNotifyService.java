
package com.igomall.service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.ProductNotify;

public interface ProductNotifyService extends BaseService<ProductNotify, Long> {

	boolean exists(Product product, String email);

	Page<ProductNotify> findPage(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable);

	Long count(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent);

	int send(Long[] ids);

}