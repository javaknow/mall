/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import com.igomall.dao.OrderLogDao;
import com.igomall.entity.OrderLog;

import org.springframework.stereotype.Repository;

@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, Long> implements OrderLogDao {

}