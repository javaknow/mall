/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.Payment;

public interface PaymentDao extends BaseDao<Payment, Long> {

	Payment findBySn(String sn);

}