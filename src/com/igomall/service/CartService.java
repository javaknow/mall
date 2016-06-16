
package com.igomall.service;

import com.igomall.entity.Cart;
import com.igomall.entity.Member;

public interface CartService extends BaseService<Cart, Long> {

	/**
	 * 获取当前购物车
	 * @return
	 */
	Cart getCurrent();

	void merge(Member member, Cart cart);

	void evictExpired();

}