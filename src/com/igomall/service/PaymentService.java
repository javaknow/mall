
package com.igomall.service;

import com.igomall.entity.Payment;

public interface PaymentService extends BaseService<Payment, Long> {

	Payment findBySn(String sn);

	void handle(Payment payment);

}