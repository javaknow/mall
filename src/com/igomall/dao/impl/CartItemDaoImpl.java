/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import com.igomall.dao.CartItemDao;
import com.igomall.entity.CartItem;

import org.springframework.stereotype.Repository;

@Repository("cartItemDaoImpl")
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, Long> implements CartItemDao {

}