
package com.igomall.dao;

import com.igomall.entity.Cart;

public interface CartDao extends BaseDao<Cart, Long> {

	void evictExpired();

}