/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import com.igomall.dao.OrderItemDao;
import com.igomall.entity.OrderItem;

import org.springframework.stereotype.Repository;

@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {

}