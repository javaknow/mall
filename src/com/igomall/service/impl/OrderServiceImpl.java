
package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.igomall.Filter;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.Setting;
import com.igomall.Setting.StockAllocationTime;
import com.igomall.dao.CartDao;
import com.igomall.dao.CouponCodeDao;
import com.igomall.dao.DepositDao;
import com.igomall.dao.MemberDao;
import com.igomall.dao.MemberRankDao;
import com.igomall.dao.OrderDao;
import com.igomall.dao.OrderItemDao;
import com.igomall.dao.OrderLogDao;
import com.igomall.dao.PaymentDao;
import com.igomall.dao.ProductDao;
import com.igomall.dao.RefundsDao;
import com.igomall.dao.ReturnsDao;
import com.igomall.dao.ShippingDao;
import com.igomall.dao.SnDao;
import com.igomall.entity.Admin;
import com.igomall.entity.Cart;
import com.igomall.entity.CartItem;
import com.igomall.entity.Coupon;
import com.igomall.entity.CouponCode;
import com.igomall.entity.Deposit;
import com.igomall.entity.GiftItem;
import com.igomall.entity.Member;
import com.igomall.entity.MemberRank;
import com.igomall.entity.Order;
import com.igomall.entity.OrderItem;
import com.igomall.entity.OrderLog;
import com.igomall.entity.Payment;
import com.igomall.entity.PaymentMethod;
import com.igomall.entity.Product;
import com.igomall.entity.Promotion;
import com.igomall.entity.Receiver;
import com.igomall.entity.Refunds;
import com.igomall.entity.Returns;
import com.igomall.entity.ReturnsItem;
import com.igomall.entity.Shipping;
import com.igomall.entity.ShippingItem;
import com.igomall.entity.ShippingMethod;
import com.igomall.entity.Sn;
import com.igomall.entity.Order.OrderStatus;
import com.igomall.entity.Order.PaymentStatus;
import com.igomall.entity.Order.ShippingStatus;
import com.igomall.entity.OrderLog.Type;
import com.igomall.service.OrderService;
import com.igomall.service.StaticService;
import com.igomall.util.SettingUtils;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;
	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;
	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;
	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;
	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;
	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;
	@Resource(name = "returnsDaoImpl")
	private ReturnsDao returnsDao;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "orderDaoImpl")
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}

	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return orderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Order> findList(Member member, Integer count, List<Filter> filters, List<com.igomall.Order> orders) {
		return orderDao.findList(member, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Member member, Pageable pageable) {
		return orderDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		return orderDao.count(orderStatus, paymentStatus, shippingStatus, hasExpired);
	}

	@Transactional(readOnly = true)
	public Long waitingPaymentCount(Member member) {
		return orderDao.waitingPaymentCount(member);
	}

	@Transactional(readOnly = true)
	public Long waitingShippingCount(Member member) {
		return orderDao.waitingShippingCount(member);
	}

	@Transactional(readOnly = true)
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		return orderDao.getSalesAmount(beginDate, endDate);
	}

	@Transactional(readOnly = true)
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		return orderDao.getSalesVolume(beginDate, endDate);
	}

	public void releaseStock() {
		orderDao.releaseStock();
	}

	@Transactional(readOnly = true)
	public Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());

		Order order = new Order();
		order.setShippingStatus(ShippingStatus.unshipped);//设置发货状态
		order.setFee(new BigDecimal(0));//设置运费
		order.setPromotionDiscount(cart.getDiscount());//设置促销的折扣
		order.setCouponDiscount(new BigDecimal(0));
		order.setOffsetAmount(new BigDecimal(0));
		order.setPoint(cart.getEffectivePoint());
		order.setMemo(memo);
		order.setMember(cart.getMember());
		order.setIsNoeval(false);

		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}

		if (!cart.getPromotions().isEmpty()) {
			StringBuffer promotionName = new StringBuffer();
			for (Promotion promotion : cart.getPromotions()) {
				if (promotion != null && promotion.getName() != null) {
					promotionName.append(" " + promotion.getName());
				}
			}
			if (promotionName.length() > 0) {
				promotionName.deleteCharAt(0);
			}
			order.setPromotion(promotionName.toString());
		}

		order.setPaymentMethod(paymentMethod);

		if (shippingMethod != null && paymentMethod != null && paymentMethod.getShippingMethods().contains(shippingMethod)) {
			BigDecimal freight = shippingMethod.calculateFreight(cart.getWeight());
			for (Promotion promotion : cart.getPromotions()) {
				if (promotion.getIsFreeShipping()) {
					freight = new BigDecimal(0);
					break;
				}
			}
			order.setFreight(freight);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(new BigDecimal(0));
		}

		if (couponCode != null && cart.isCouponAllowed()) {
			couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
			if (!couponCode.getIsUsed() && couponCode.getCoupon() != null && cart.isValid(couponCode.getCoupon())) {
				BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getQuantity(), cart.getEffectivePrice()));
				couponDiscount = couponDiscount.compareTo(new BigDecimal(0)) > 0 ? couponDiscount : new BigDecimal(0);
				order.setCouponDiscount(couponDiscount);
				order.setCouponCode(couponCode);
			}
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem != null && cartItem.getProduct() != null) {
				Product product = cartItem.getProduct();
				OrderItem orderItem = new OrderItem();
				orderItem.setParent(cartItem.getParent());
				orderItem.setType(cartItem.getType());
				orderItem.setIntegralExchangeProduct(cartItem.getIntegralExchangeProduct());
				orderItem.setSn(product.getSn());
				orderItem.setName(product.getName());
				orderItem.setFullName(product.getFullName());
				orderItem.setPrice(cartItem.getPrice());
				orderItem.setIsNoeval(false);
				/**
				 * 应付金额
				 */
				orderItem.setAmountPaid(cartItem.getEffectivePrice());
				orderItem.setWeight(product.getWeight());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setIsGift(false);
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(product);
				orderItem.setOrder(order);
				if(product.getPromotions()!=null&&product.getPromotions().size()>0){
					Promotion promotion = product.getPromotions().iterator().next();
					orderItem.setPromotion(promotion);
				}
				
				orderItems.add(orderItem);
			}
		}

		for (GiftItem giftItem : cart.getGiftItems()) {
			if (giftItem != null && giftItem.getGift() != null) {
				Product gift = giftItem.getGift();
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setFullName(gift.getFullName());
				orderItem.setPrice(new BigDecimal(0));
				orderItem.setWeight(gift.getWeight());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setIsGift(true);
				orderItem.setQuantity(giftItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(gift);
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
		}

		Setting setting = SettingUtils.get();
		if (setting.getIsInvoiceEnabled() && isInvoice && StringUtils.isNotEmpty(invoiceTitle)) {
			order.setIsInvoice(true);
			order.setInvoiceTitle(invoiceTitle);
			order.setTax(order.calculateTax());
		} else {
			order.setIsInvoice(false);
			order.setTax(new BigDecimal(0));
		}

		if (useBalance) {
			Member member = cart.getMember();
			if (member.getBalance().compareTo(order.getAmount()) >= 0) {
				order.setAmountPaid(order.getAmount());
			} else {
				order.setAmountPaid(member.getBalance());
			}
		} else {
			order.setAmountPaid(new BigDecimal(0));
		}

		if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.paid);
		} else if (order.getAmountPayable().compareTo(new BigDecimal(0)) > 0 && order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.partialPayment);
		} else {
			order.setOrderStatus(OrderStatus.unconfirmed);
			order.setPaymentStatus(PaymentStatus.unpaid);
		}

		if (paymentMethod != null && paymentMethod.getTimeout() != null && order.getPaymentStatus() == PaymentStatus.unpaid) {
			order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
		}
		return order;
	}

	public Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo, Admin operator) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());
		Assert.notNull(receiver);
		Assert.notNull(paymentMethod);
		Assert.notNull(shippingMethod);

		Order order = build(cart, receiver, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo);

		order.setSn(snDao.generate(Sn.Type.order));
		if (paymentMethod.getMethod() == PaymentMethod.Method.online) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(operator);
		}

		if (order.getCouponCode() != null) {
			couponCode.setIsUsed(true);
			couponCode.setUsedDate(new Date());
			couponCodeDao.merge(couponCode);
		}

		for (Promotion promotion : cart.getPromotions()) {
			for (Coupon coupon : promotion.getCoupons()) {
				order.getCoupons().add(coupon);
			}
		}

		Setting setting = SettingUtils.get();
		if (setting.getStockAllocationTime() == StockAllocationTime.order || (setting.getStockAllocationTime() == StockAllocationTime.payment && (order.getPaymentStatus() == PaymentStatus.partialPayment || order.getPaymentStatus() == PaymentStatus.paid))) {
			order.setIsAllocatedStock(true);
		} else {
			order.setIsAllocatedStock(false);
		}

		orderDao.persist(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.create);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		Member member = cart.getMember();
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(order.getAmountPaid()));
			memberDao.merge(member);
			
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(order.getAmountPaid());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.persist(deposit);
		}

		if (setting.getStockAllocationTime() == StockAllocationTime.order || (setting.getStockAllocationTime() == StockAllocationTime.payment && (order.getPaymentStatus() == PaymentStatus.partialPayment || order.getPaymentStatus() == PaymentStatus.paid))) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
		}

		cartDao.remove(cart);
		return order;
	}

	public void update(Order order, Admin operator) {
		Assert.notNull(order);

		Order pOrder = orderDao.find(order.getId());

		if (pOrder.getIsAllocatedStock()) {
			for (OrderItem orderItem : pOrder.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						productDao.flush();
						staticService.build(product);
					}
				}
			}
		}

		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.modify);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void confirm(Order order, Admin operator) {
		Assert.notNull(order);

		order.setOrderStatus(OrderStatus.confirmed);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void complete(Order order, Admin operator) {
		Assert.notNull(order);

		Member member = order.getMember();
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (order.getShippingStatus() == ShippingStatus.partialShipment || order.getShippingStatus() == ShippingStatus.shipped) {
			member.setPoint(member.getPoint() + order.getPoint());
			for (Coupon coupon : order.getCoupons()) {
				couponCodeDao.build(coupon, member);
			}
		}

		if (order.getShippingStatus() == ShippingStatus.unshipped || order.getShippingStatus() == ShippingStatus.returned) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(false);
				couponCode.setUsedDate(null);
				couponCodeDao.merge(couponCode);

				order.setCouponCode(null);
				orderDao.merge(order);
			}
		}

		member.setAmount(member.getAmount().add(order.getAmountPaid()));
		if (!member.getMemberRank().getIsSpecial()) {
			MemberRank memberRank = memberRankDao.findByAmount(member.getAmount());
			if (memberRank != null && memberRank.getAmount().compareTo(member.getMemberRank().getAmount()) > 0) {
				member.setMemberRank(memberRank);
			}
		}
		memberDao.merge(member);

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem != null) {
				Product product = orderItem.getProduct();
				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if (product != null) {
					Integer quantity = orderItem.getQuantity();
					Calendar nowCalendar = Calendar.getInstance();
					Calendar weekSalesCalendar = DateUtils.toCalendar(product.getWeekSalesDate());
					Calendar monthSalesCalendar = DateUtils.toCalendar(product.getMonthSalesDate());
					if (nowCalendar.get(Calendar.YEAR) != weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
						product.setWeekSales((long) quantity);
					} else {
						product.setWeekSales(product.getWeekSales() + quantity);
					}
					if (nowCalendar.get(Calendar.YEAR) != monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
						product.setMonthSales((long) quantity);
					} else {
						product.setMonthSales(product.getMonthSales() + quantity);
					}
					product.setSales(product.getSales() + quantity);
					product.setWeekSalesDate(new Date());
					product.setMonthSalesDate(new Date());
					productDao.merge(product);
					orderDao.flush();
					staticService.build(product);
				}
			}
		}

		order.setOrderStatus(OrderStatus.completed);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.complete);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void cancel(Order order, Admin operator) {
		Assert.notNull(order);

		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			couponCode.setIsUsed(false);
			couponCode.setUsedDate(null);
			couponCodeDao.merge(couponCode);

			order.setCouponCode(null);
			orderDao.merge(order);
		}

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setOrderStatus(OrderStatus.cancelled);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.cancel);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void payment(Order order, Payment payment, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(payment);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		payment.setOrder(order);
		paymentDao.merge(payment);
		if (payment.getMethod() == Payment.Method.deposit) {
			Member member = order.getMember();
			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(payment.getAmount()));
			memberDao.merge(member);

			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(payment.getAmount());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.persist(deposit);
		}

		Setting setting = SettingUtils.get();
		if (!order.getIsAllocatedStock() && setting.getStockAllocationTime() == StockAllocationTime.payment) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(true);
		}

		order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
		order.setFee(payment.getFee());
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.paid);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.partialPayment);
		}
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.payment);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void refunds(Order order, Refunds refunds, Admin operator) {
		Assert.notNull(order);
		//Assert.notNull(refunds);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		refunds.setOrder(order);
		refundsDao.persist(refunds);
		//if (refunds.getMethod() == Refunds.Method.deposit) {
			
			Member member = order.getMember();
			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(refunds.getAmount()));
			memberDao.merge(member);
			

			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.adminRefunds);
			deposit.setCredit(refunds.getAmount());
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.persist(deposit);
		//}

		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) == 0) {
			order.setPaymentStatus(PaymentStatus.refunded);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setPaymentStatus(PaymentStatus.partialRefunds);
		}
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.refunds);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void shipping(Order order, Shipping shipping, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(shipping);
		Assert.notEmpty(shipping.getShippingItems());

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		Setting setting = SettingUtils.get();
		if (!order.getIsAllocatedStock() && setting.getStockAllocationTime() == StockAllocationTime.ship) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(true);
		}

		shipping.setOrder(order);
		shippingDao.persist(shipping);
		for (ShippingItem shippingItem : shipping.getShippingItems()) {
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem != null) {
				Product product = orderItem.getProduct();
				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if (product != null) {
					if (product.getStock() != null) {
						product.setStock(product.getStock() - shippingItem.getQuantity());
						if (order.getIsAllocatedStock()) {
							product.setAllocatedStock(product.getAllocatedStock() - shippingItem.getQuantity());
						}
					}
					productDao.merge(product);
					orderDao.flush();
					staticService.build(product);
				}
				orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
				orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
			}
		}
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setShippingStatus(ShippingStatus.shipped);
			order.setIsAllocatedStock(false);
		} else if (order.getShippedQuantity() > 0) {
			order.setShippingStatus(ShippingStatus.partialShipment);
		}
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.shipping);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	public void returns(Order order, Returns returns, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(returns);
		Assert.notEmpty(returns.getReturnsItems());

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		returns.setOrder(order);
		returnsDao.persist(returns);
		for (ReturnsItem returnsItem : returns.getReturnsItems()) {
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem != null) {
				orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
				orderItem.setReturnQuantity(orderItem.getReturnQuantity() + returnsItem.getQuantity());
			}
		}
		if (order.getReturnQuantity() >= order.getShippedQuantity()) {
			order.setShippingStatus(ShippingStatus.returned);
		} else if (order.getReturnQuantity() > 0) {
			order.setShippingStatus(ShippingStatus.partialReturns);
		}
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.returns);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
	}

	@Override
	public void delete(Order order) {
		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.merge(product);
						orderDao.flush();
						staticService.build(product);
					}
				}
			}
		}
		super.delete(order);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return orderDao.findPage(member,orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Override
	public Page<Order> findWaitingNoevalPage(Member member, Pageable pageable) {
		return orderDao.findWaitingNoevalPage(member,pageable);
	}

	@Override
	public Page<Order> findPageWaitingPayment(Member member, Pageable pageable) {
		return orderDao.findPageWaitingPayment(member,pageable);
	}

	@Override
	public Page<Order> findPageWaitingShipping(Member member, Pageable pageable) {
		return orderDao.findPageWaitingShipping(member,pageable);
	}

	@Override
	public Page<Order> findPageWaitingReceipt(Member member, Pageable pageable) {
		return orderDao.findPageWaitingReceipt(member,pageable);
	}
}