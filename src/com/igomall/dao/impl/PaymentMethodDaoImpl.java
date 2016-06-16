/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import com.igomall.dao.PaymentMethodDao;
import com.igomall.entity.PaymentMethod;

import org.springframework.stereotype.Repository;

@Repository("paymentMethodDaoImpl")
public class PaymentMethodDaoImpl extends BaseDaoImpl<PaymentMethod, Long> implements PaymentMethodDao {

}