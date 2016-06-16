
package com.igomall.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igomall.entity.CartItem.Type;

@Entity
@Table(name = "lx_order_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_order_item_sequence")
public class OrderItem extends BaseEntity {
	
	private static final long serialVersionUID = -4999926022604479334L;

	private String sn;

	private String name;

	private String fullName;

	private BigDecimal price;

	private Double weight;

	private String thumbnail;

	private Boolean isGift;

	private Integer quantity;

	private Integer shippedQuantity;

	private Integer returnQuantity;

	private Product product;

	private Order order;
	
	private Type type;
	
	private IntegralExchangeProduct integralExchangeProduct;
	
	private BigDecimal amountPaid;// 实际付款的金额
	
	private Member parent;
	
	private Promotion promotion;
	
	private Shop shop;
	
	private Boolean isNoeval;//是否评价过 false 未评价 true：已评价
	
	@Transient
	@JsonProperty
	public Long getProductId() {
		if(getProduct()!=null){
			return getProduct().getId();
		}else{
			return null;
		}
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
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

	@ManyToOne(fetch=FetchType.LAZY)
	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getParent() {
		return parent;
	}

	public void setParent(Member parent) {
		this.parent = parent;
	}

	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public IntegralExchangeProduct getIntegralExchangeProduct() {
		return integralExchangeProduct;
	}

	public void setIntegralExchangeProduct(
			IntegralExchangeProduct integralExchangeProduct) {
		this.integralExchangeProduct = integralExchangeProduct;
	}

	@JsonProperty
	@NotEmpty
	@Column(nullable = false, updatable = false)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@JsonProperty
	@Column(updatable = false)
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@JsonProperty
	@Column(updatable = false)
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false)
	public Boolean getIsGift() {
		return isGift;
	}

	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}

	@JsonProperty
	@NotNull
	@Min(1)
	@Max(10000)
	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(nullable = false)
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	@Column(nullable = false)
	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@JsonProperty
	@Transient
	public Double getTotalWeight() {
		if (getWeight() != null && getQuantity() != null) {
			return getWeight() * getQuantity();
		} else {
			return 0.0;
		}
	}

	// 总金额
	@JsonProperty
	@Transient
	public BigDecimal getSubtotal() {
		if (getPrice() != null && getQuantity() != null&&getIntegralExchangeProduct()==null) {
			return getPrice().multiply(new BigDecimal(getQuantity()));
		} else {
			return new BigDecimal(0);
		}
	}
	
	// 总积分
		@JsonProperty
		@Transient
		public BigDecimal getSubtotal1() {
			if (getPrice() != null && getQuantity() != null&&getIntegralExchangeProduct()==null&&getIntegralExchangeProduct()!=null) {
				return new BigDecimal(getIntegralExchangeProduct().getPoint()).multiply(new BigDecimal(getQuantity()));
			} else {
				return new BigDecimal(0);
			}
		}
		
		
		// 产品图片
		@JsonProperty
		@Transient
		public String getImage() {
			if (getProduct()!=null) {
				return getProduct().getImage();
			} else {
				return "";
			}
		}
		
}