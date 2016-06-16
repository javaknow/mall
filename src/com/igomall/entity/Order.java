/**
 * 1.客服付款：confirmed====paid===unshipped
 * 2.发货：confirmed====paid===shipped
 * 3.完成：completed====paid===shipped
 * 
 * 
 * 
 */
package com.igomall.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igomall.Message;
import com.igomall.Setting;
import com.igomall.util.SettingUtils;

@Entity
@Table(name = "lx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_order_sequence")
public class Order extends BaseEntity {

	private static final long serialVersionUID = 8370942500343156156L;

	private static final String NAME_SEPARATOR = " ";

	/**
	 * 1.未付款 可以取消订单
	 * 2.付款了，只能申请退款
	 * 3.发货了，只能确认收货和退货
	 */
	/**
	 * 订单状态整理
	 * ------事件-------------------------orderStatus---------------paymentStatus--------------ShippingStatus
	 * 订单生成（未付款）                                     未确认 ==unconfirmed	       未支付 ==unpaid	                     未发货 ==unshipped
	 * 部分付款                                                       已确认 ==confirmed	               部分支付 ==partialPayment	      未发货 ==unshipped
	 * 全部付款（未发货）                                     已确认 ==confirmed	               已支付 ==paid	                              未发货 ==unshipped
	 * 部分发货                                                       已确认 ==confirmed	               已支付 ==paid	                               部分发货 ==partialShipment
	 * 全部发货                                                       已确认 ==confirmed	               已支付 ==paid	                              已发货 ==shipped
	 * 确认收货                                                       已完成 ==completed	               已支付 ==paid	                              已发货 ==shipped
	 * 确认                                                              已确认 ==confirmed	               未支付 ==unpaid	                      未发货 ==unshipped
	 * 完成                                                              已完成 ==completed	               未支付 ==unpaid	                      未发货 ==unshipped
	 * 部分退款                                                       已确认 ==confirmed	               部分退款 ==partialRefunds	      已发货 ==shipped
	 * 全部退款                                                       已确认 ==confirmed	               已退款 ==refunded	                      已发货 ==shipped
	 * 退货                                                              已确认 ==confirmed	               已退款 ==refunded	                      已退货 ==returned
	 * 部分退货                                                       已确认 ==confirmed	               未支付 ==unpaid	                      部分退货 ==partialReturns
	 * 取消                                                              已取消 ==cancelled	               未支付 ==unpaid	                      未发货 ==unshipped
	 * @author blackboy
	 * 
	 * 流程：
	 * ---节点----------------------------前台--------------------------------后台
	 * 未付款                                                         付款   取消                                                        
	 * 部分付款                                                      付款   退款                                                         不能取消
	 * 全部付款 （未发货）                                    退款                                                                  不能取消
	 * 部分发货                                                                                                                          发货，退款 退货
	 * 全部发货                                                        收货
	 * 收货                                                                退货
	 * 完成
	 */
	public enum OrderStatus {

		unconfirmed,

		confirmed,

		completed,

		cancelled
	}

	public enum PaymentStatus {

		unpaid,

		partialPayment,

		paid,

		partialRefunds,

		refunded
	}

	public enum ShippingStatus {

		unshipped,

		partialShipment,

		shipped,

		partialReturns,

		returned
	}

	public enum Type {//订单类型
		normal,//普通订单
		auction,//竞拍订单
		integration//积分订单
		
	}
	
	private String sn;// 订单编号

	private OrderStatus orderStatus;// 订单状态

	private PaymentStatus paymentStatus;// 付款状态

	private ShippingStatus shippingStatus;// 发货状态

	private BigDecimal fee;// 运费

	private BigDecimal freight;

	private BigDecimal promotionDiscount;

	private BigDecimal couponDiscount;

	private BigDecimal offsetAmount;

	private BigDecimal amountPaid;// 总金额

	private Long point;

	private String consignee;//收货人

	private String areaName;//

	private String address;//地址

	private String zipCode;//邮编

	private String phone;//手机号码

	private Boolean isInvoice;

	private String invoiceTitle;

	private BigDecimal tax;

	private String memo;

	private String promotion;

	private Date expire;

	private Date lockExpire;

	private Boolean isAllocatedStock;

	private String paymentMethodName;

	private String shippingMethodName;

	private Area area;

	private PaymentMethod paymentMethod;

	private ShippingMethod shippingMethod;

	private Admin operator;

	private Member member;

	private CouponCode couponCode;

	private List<Coupon> coupons = new ArrayList<Coupon>();

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

	private Set<Deposit> deposits = new HashSet<Deposit>();

	private Set<Payment> payments = new HashSet<Payment>();

	private Set<Refunds> refunds = new HashSet<Refunds>();

	private Set<Shipping> shippings = new HashSet<Shipping>();

	private Set<Returns> returns = new HashSet<Returns>();
	
	private Type type;//订单类型
	
	private Boolean isNoeval;//是否评价过 false 未评价 true：已评价
	
	private Shop shop;//店铺  订单属于哪个店铺的
	
	private OrderGroup orderGroup;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	public OrderGroup getOrderGroup() {
		return orderGroup;
	}

	public void setOrderGroup(OrderGroup orderGroup) {
		this.orderGroup = orderGroup;
	}

	@ManyToOne
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Boolean getIsNoeval() {
		return isNoeval;
	}

	public void setIsNoeval(Boolean isNoeval) {
		this.isNoeval = isNoeval;
	}
	
	@Transient
	@JsonProperty
	public String getState_desc() {
		Message.error("admin.articleCategory.deleteExistChildrenNotAllowed").getContent();
		String status="";
		if(isExpired()){
			status = Message.error("shop.member.order.hasExpired").getContent();
		}else if(getOrderStatus()==OrderStatus.completed||getOrderStatus()==OrderStatus.cancelled){
			status = Message.error("Order.OrderStatus."+getOrderStatus()).getContent();
		}else if(getPaymentStatus()==PaymentStatus.unpaid||getPaymentStatus()==PaymentStatus.partialPayment){
			status = Message.error("shop.member.order.waitingPayment").getContent();
			if(getShippingStatus()==ShippingStatus.unshipped){
				status = status + " " + Message.error("Order.ShippingStatus." + getShippingStatus()).getContent();
			}
		}else{
			status = Message.error("Order.PaymentStatus." + getPaymentStatus()).getContent();
			if(getPaymentStatus()==PaymentStatus.paid&&getShippingStatus()==ShippingStatus.unshipped){
				status = status + " " + Message.error("shop.member.order.waitingShipping").getContent();
			}else{
				status = status + " " + Message.error("Order.ShippingStatus." + getShippingStatus()).getContent();
			}
		}
		return status;
	}
	
	@Transient
	@JsonProperty
	public Boolean getExpired(){
		return isExpired();
	}

	/**
	 * 是否可删除
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsDelete() {
		return false;
	}

	/**
	 * 是否锁定
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsLock() {
		
		return false;
	}

	/**
	 * 是否可支付
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsPayment(){
		
		if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
			return false;
		}
		if(isExpired()){//已过期的订单
			return false;
		}
		if(getOrderStatus()==OrderStatus.completed){//已完成
			return false;
		}
		//已支付 未发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
			if(getPaymentStatus()==PaymentStatus.partialPayment){//部分支付 是可以再支付的
				return true;
			}else{
				return false;
			}
			
		}
		//已支付 已发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped||getShippingStatus()==ShippingStatus.partialShipment)){
			return false;
		}
		//已支付 已退货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
			return false;
		}
		//已退款 部分退货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
			return false;
		}
		//部分退款 已发货
		if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
			return false;
		}
		//已退款 未发货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//等待付款 未发货
		if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return true;
		}
		
		return true;
	}
	
	/**
	 * 是否可取消订单
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsCancel() {
		if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
			return false;
		}
		if(isExpired()){//已过期的订单
			return false;
		}
		if(getOrderStatus()==OrderStatus.completed){//已完成
			return false;
		}
		//已支付 未发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return true;
		}
		//已支付 已发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped||getShippingStatus()==ShippingStatus.partialShipment)){
			return false;
		}
		//已支付 已退货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
			return false;
		}
		//已退款 部分退货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
			return false;
		}
		//部分退款 已发货
		if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
			return false;
		}
		//已退款 未发货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//等待付款 未发货
		if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return true;
		}
		
		return true;
	}

	/**
	 * 是否可查看物流
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsDeliver() {
		if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
			return false;
		}
		if(isExpired()){//已过期的订单
			return false;
		}
		if(getOrderStatus()==OrderStatus.completed){//已完成
			return false;
		}
		
		//已支付 未发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//已支付 已发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped||getShippingStatus()==ShippingStatus.partialShipment)){
			return true;
		}
		//已支付 已退货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
			return false;
		}
		//已退款 部分退货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
			return false;
		}
		//部分退款 已发货
		if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
			return true;
		}
		//已退款 未发货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//等待付款 未发货
		if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		
		return true;
	}

	/**
	 * 是否可收货
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsReceive() {
		if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
			return false;
		}
		if(isExpired()){//已过期的订单
			return false;
		}
		if(getOrderStatus()==OrderStatus.completed){//已完成
			return false;
		}
		//已支付 未发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//已支付 已发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped)){
			return true;
		}
		//已支付 已退货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
			return false;
		}
		//已退款 部分退货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
			return true;
		}
		//部分退款 已发货
		if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
			return false;
		}
		//已退款 未发货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//等待付款 未发货
		if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		return true;
	}

	/**
	 * 是否可以评价
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsEvaluation() {
		/*if(getIsNoeval()){
			return false;
		}else{
			if(getOrderStatus()==OrderStatus.completed){//已完成
				return true;
			}
			if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
				return false;
			}
			if(isExpired()){//已过期的订单
				return false;
			}
			//已支付 未发货
			if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
				return false;
			}
			//已支付 已发货
			if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped)){
				return false;
			}
			//已支付 已退货
			if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
				return false;
			}
			//已退款 部分退货
			if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
				return false;
			}
			//部分退款 已发货
			if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
				return false;
			}
			//已退款 未发货
			if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
				return false;
			}
			//等待付款 未发货
			if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
				return false;
			}
		}*/
		
		return true;
	}

	/**
	 * 是否可再次评价
	 * @author black
	 * @return
	 */
	@Transient
	@JsonProperty
	public Boolean getIsEvaluationAgain() {
		
		/*if(getOrderStatus()==OrderStatus.completed){//已完成
			return true;
		}
		if(getOrderStatus()==OrderStatus.cancelled){//已经取消了
			return false;
		}
		if(isExpired()){//已过期的订单
			return false;
		}
		//已支付 未发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//已支付 已发货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.shipped)){
			return false;
		}
		//已支付 已退货
		if((getPaymentStatus()==PaymentStatus.paid||getPaymentStatus()==PaymentStatus.partialPayment)&&(getShippingStatus()==ShippingStatus.returned)){
			return false;
		}
		//已退款 部分退货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.partialReturns)){
			return false;
		}
		//部分退款 已发货
		if((getPaymentStatus()==PaymentStatus.partialRefunds)&&(getShippingStatus()==ShippingStatus.shipped)){
			return false;
		}
		//已退款 未发货
		if((getPaymentStatus()==PaymentStatus.refunded)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}
		//等待付款 未发货
		if((getPaymentStatus()==PaymentStatus.unpaid)&&(getShippingStatus()==ShippingStatus.unshipped)){
			return false;
		}*/
		return false;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@JsonProperty
	@Column(nullable = false)
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(nullable = false)
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Column(nullable = false)
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@JsonProperty
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@JsonProperty
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	@JsonProperty
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	@JsonProperty
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	@JsonProperty
	@NotNull
	@Min(0)
	@Column(nullable = false)
	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@Column(nullable = false)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	@Length(max = 200)
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Length(max = 2000)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(updatable = false)
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	@Column(nullable = false)
	public Boolean getIsAllocatedStock() {
		return isAllocatedStock;
	}

	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	@Column(nullable = false)
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	@Column(nullable = false)
	public String getShippingMethodName() {
		return shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Admin getOperator() {
		return operator;
	}

	public void setOperator(Admin operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_order_coupon")
	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@JsonProperty
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("isGift asc")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@JsonProperty
	@Transient
	public List<OrderItem> getGiftItems() {
		List<OrderItem> giftItems = new ArrayList<OrderItem>();
		for (OrderItem orderItem : getOrderItems()) {
			if(orderItem.getIsGift()){
				giftItems.add(orderItem);
			}
		}
		return giftItems;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	public Set<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return shippings;
	}

	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Returns> getReturns() {
		return returns;
	}

	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	@Transient
	public String getName() {
		StringBuffer name = new StringBuffer();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getFullName() != null) {
					name.append(NAME_SEPARATOR).append(orderItem.getFullName());
				}
			}
			if (name.length() > 0) {
				name.deleteCharAt(0);
			}
		}
		return name.toString();
	}

	@Transient
	public int getWeight() {
		int weight = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					weight += orderItem.getTotalWeight();
				}
			}
		}
		return weight;
	}

	@JsonProperty
	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getQuantity() != null) {
					quantity += orderItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	@Transient
	public int getShippedQuantity() {
		int shippedQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getShippedQuantity() != null) {
					shippedQuantity += orderItem.getShippedQuantity();
				}
			}
		}
		return shippedQuantity;
	}

	@Transient
	public int getReturnQuantity() {
		int returnQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getReturnQuantity() != null) {
					returnQuantity += orderItem.getReturnQuantity();
				}
			}
		}
		return returnQuantity;
	}

	// 总金额
	@Transient
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSubtotal() != null&&orderItem.getIntegralExchangeProduct()==null) {
					price = price.add(orderItem.getSubtotal());
				}
			}
		}
		return price;
	}

	@JsonProperty
	// 总金额
	@Transient
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice();
		if (getFee() != null) {
			amount = amount.add(getFee());
		}
		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getTax() != null) {
			amount = amount.add(getTax());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount: new BigDecimal(0);
	}

	@JsonProperty
	// 总金额
	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
		return amountPayable.compareTo(new BigDecimal(0)) > 0 ? amountPayable : new BigDecimal(0);
	}

	@Transient
	public boolean isExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	@Transient
	public OrderItem getOrderItem(String sn) {
		if (sn != null && getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && sn.equalsIgnoreCase(orderItem.getSn())) {
					return orderItem;
				}
			}
		}
		return null;
	}

	@Transient
	public boolean isLocked(Admin operator) {
		return getLockExpire() != null&& new Date().before(getLockExpire())&& ((operator != null && !operator.equals(getOperator())) || (operator == null && getOperator() != null));
	}

	@Transient
	public BigDecimal calculateTax() {
		BigDecimal tax = new BigDecimal(0);
		Setting setting = SettingUtils.get();
		if (setting.getIsTaxPriceEnabled()) {
			BigDecimal amount = getPrice();
			if (getPromotionDiscount() != null) {
				amount = amount.subtract(getPromotionDiscount());
			}
			if (getCouponDiscount() != null) {
				amount = amount.subtract(getCouponDiscount());
			}
			if (getOffsetAmount() != null) {
				amount = amount.add(getOffsetAmount());
			}
			tax = amount.multiply(new BigDecimal(setting.getTaxRate()
					.toString()));
		}
		return setting.setScale(tax);
	}

	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	@PreRemove
	public void preRemove() {
		Set<Deposit> deposits = getDeposits();
		if (deposits != null) {
			for (Deposit deposit : deposits) {
				deposit.setOrder(null);
			}
		}
	}
/**********************************店铺的基本信息   ajax调用  开始********************************************************************************************/
	@Transient
	@JsonProperty
	public Boolean getOwnshop() {
		return true;
	}
	
	@Transient
	@JsonProperty
	public Long getStore_id() {
		if(shop!=null){
			return getShop().getId();
		}else{
			return null;
		}
		
	}
	
	@Transient
	@JsonProperty
	public String getStore_name() {
		if(shop!=null){
			return getShop().getName();
		}else{
			return null;
		}
	}

/**********************************订单的基本信息   ajax调用  开始********************************************************************************************/
	@Transient
	@JsonProperty
	public Integer getOrder_state() {
		Integer state=30;
		if(isExpired()){
			state=0;
		}else if(getOrderStatus()==OrderStatus.completed||getOrderStatus()==OrderStatus.cancelled){
			state=20;
		}else{
			state=10;
		}
		
		return state;
	}
	
	@Transient
	@JsonProperty
	public List<Product> getZengpin_list() {
		
		return new ArrayList<Product>();
	}
	
}