/*
 * 
 * 
 * 
 */
package com.igomall.service.impl;

import javax.annotation.Resource;

import com.igomall.dao.CartItemDao;
import com.igomall.entity.CartItem;
import com.igomall.service.CartItemService;

import org.springframework.stereotype.Service;

@Service("cartItemServiceImpl")
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, Long> implements CartItemService {

	@Resource(name = "cartItemDaoImpl")
	public void setBaseDao(CartItemDao cartItemDao) {
		super.setBaseDao(cartItemDao);
	}

}