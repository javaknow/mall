
package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.igomall.Filter;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Admin;
import com.igomall.entity.Cart;
import com.igomall.entity.CouponCode;
import com.igomall.entity.Member;
import com.igomall.entity.Order;
import com.igomall.entity.Payment;
import com.igomall.entity.PaymentMethod;
import com.igomall.entity.Receiver;
import com.igomall.entity.Refunds;
import com.igomall.entity.Returns;
import com.igomall.entity.Shipping;
import com.igomall.entity.ShippingMethod;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Order.ShippingStatus;

public interface OrderService extends BaseService<Order, Long> {

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

	/**
	 * 订单的创建
	 * @author black
	 * @param cart 购物车
	 * @param receiver 收货地址
	 * @param paymentMethod 付款方式
	 * @param shippingMethod 发货方式
	 * @param couponCode 优惠券
	 * @param isInvoice 是否要发票
	 * @param invoiceTitle 发票抬头
	 * @param useBalance 是否使用账户付款
	 * @param memo 给商家的备注
	 * @return
	 */
	Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo);

	Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo, Admin operator);

	void update(Order order, Admin operator);

	void confirm(Order order, Admin operator);

	/**
	 * 订单的完成（确认收货）
	 * @author black
	 * @param order
	 * @param operator
	 */
	void complete(Order order, Admin operator);

	void cancel(Order order, Admin operator);

	void payment(Order order, Payment payment, Admin operator);
	
	/**
	 * 退款操作
	 * @param order 订单
	 * @param refunds 退款方式
	 * @param operator 操作人
	 */
	void refunds(Order order, Refunds refunds, Admin operator);

	void shipping(Order order, Shipping shipping, Admin operator);

	void returns(Order order, Returns returns, Admin operator);

	Page<Order> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 待评价的订单
	 * @author black
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findWaitingNoevalPage(Member member, Pageable pageable);

	/**
	 * 分页查询待付款订单
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageWaitingPayment(Member member, Pageable pageable);

	/**
	 * 分页查询待发货订单
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageWaitingShipping(Member member, Pageable pageable);

	/**
	 * 分页查询待收货订单
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Order> findPageWaitingReceipt(Member member, Pageable pageable);

}