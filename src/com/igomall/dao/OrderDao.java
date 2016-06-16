
package com.igomall.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Filter;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Order.ShippingStatus;

public interface OrderDao extends BaseDao<Order, Long> {

	Order findBySn(String sn);

	List<Order> findList(Member member, Integer count, List<Filter> filters, List<com.igomall.Order> orders);

	Page<Order> findPage(Member member, Pageable pageable);

	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	Long count(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	Long waitingPaymentCount(Member member);

	Long waitingShippingCount(Member member);

	BigDecimal getSalesAmount(Date beginDate, Date endDate);

	Integer getSalesVolume(Date beginDate, Date endDate);

	void releaseStock();

	Page<Order> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);
	
	/**
	 * 待评价
	 * @author black
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findWaitingNoevalPage(Member member, Pageable pageable);

	Page<Order> findPageWaitingPayment(Member member, Pageable pageable);

	Page<Order> findPageWaitingShipping(Member member, Pageable pageable);

	Page<Order> findPageWaitingReceipt(Member member, Pageable pageable);

}