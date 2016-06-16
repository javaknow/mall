
package com.igomall.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.igomall.Setting;
import com.igomall.util.SettingUtils;

@Entity
@Table(name = "lx_cart_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_cart_item_sequence")
public class CartItem extends BaseEntity {

	public enum Type {//订单类型
		normal,//普通订单
		auction,//竞拍订单
		integration//积分订单
		
	}
	
	private static final long serialVersionUID = 2979296789363163144L;

	public static final Integer MAX_QUANTITY = 10000;

	private Integer quantity;

	private Product product;

	private Cart cart;

	private BigDecimal tempPrice;//
	
	private Long tempExpensePoint;//消费的积分
	
	private Long tempPoint;
	
	private Type type;
	
	private IntegralExchangeProduct integralExchangeProduct;

	private Member parent;
	
	private Boolean isBonuds;
	
	public Boolean getIsBonuds() {
		return isBonuds;
	}

	public void setIsBonuds(Boolean isBonuds) {
		this.isBonuds = isBonuds;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getParent() {
		return parent;
	}

	public void setParent(Member parent) {
		this.parent = parent;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Transient
	public BigDecimal getTempPrice() {
		if (tempPrice == null) {
			return getSubtotal();
		}
		return tempPrice;
	}

	@Transient
	public void setTempPrice(BigDecimal tempPrice) {
		this.tempPrice = tempPrice;
	}

	@Transient
	public Long getTempPoint() {
		if (tempPoint == null) {
			return getPoint();
		}
		return tempPoint;
	}

	@Transient
	public void setTempPoint(Long tempPoint) {
		this.tempPoint = tempPoint;
	}

	
	@Transient
	public Long getTempExpensePoint() {
		if (tempExpensePoint == null) {
			return getExpensePoint();
		}
		return tempExpensePoint;
	}

	@Transient
	public void setTempExpensePoint(Long tempExpensePoint) {
		this.tempExpensePoint = tempExpensePoint;
	}

	@Transient
	public long getPoint() {
		if (getProduct() != null && getProduct().getPoint() != null && getQuantity() != null&&(type==null||type==Type.normal)) {
			return getProduct().getPoint() * getQuantity();
		} else {
			return 0L;
		}
	}
	
	@Transient
	public long getExpensePoint() {
		if (getProduct() != null && getProduct().getPoint() != null && getQuantity() != null&&type==Type.integration) {
			return getProduct().getPoint() * getQuantity();
		} else {
			return 0L;
		}
	}

	@Transient
	public Double getWeight() {
		if (getProduct() != null && getProduct().getWeight() != null && getQuantity() != null) {
			return getProduct().getWeight() * getQuantity();
		} else {
			return 0.0;
		}
	}

	/**
	 * 计算折扣之后的价格
	 * @return
	 */
	@Transient
	public BigDecimal getPrice() {
		if (getProduct() != null && getProduct().getPrice() != null) {
			Setting setting = SettingUtils.get();
			if (getCart() != null && getCart().getMember() != null && getCart().getMember().getMemberRank() != null) {
				MemberRank memberRank = getCart().getMember().getMemberRank();
				Map<MemberRank, BigDecimal> memberPrice = getProduct().getMemberPrice();
				if (memberPrice != null && !memberPrice.isEmpty()) {
					if (memberPrice.containsKey(memberRank)) {
						return setting.setScale(memberPrice.get(memberRank));
					}
				}
				if (memberRank.getScale() != null) {
					return setting.setScale(getProduct().getPrice().multiply(new BigDecimal(memberRank.getScale())));
				}
			}
			return setting.setScale(getProduct().getPrice());
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * 计算折扣之后的总价
	 * @return
	 */
	@Transient
	public BigDecimal getSubtotal() {
		if (getQuantity() != null) {
			if(type==null||type==Type.normal){
				return getPrice().multiply(new BigDecimal(getQuantity()));
			}else{
				return new BigDecimal(getIntegralExchangeProduct().getPoint()).multiply(new BigDecimal(getQuantity()));
			}
		} else {
			return new BigDecimal(0);
		}
	}

	@Transient
	public boolean getIsLowStock() {
		if (getQuantity() != null && getProduct() != null && getProduct().getStock() != null && getQuantity() > getProduct().getAvailableStock()) {
			return true;
		} else {
			return false;
		}
	}

	@Transient
	public void add(int quantity) {
		if (quantity > 0) {
			if (getQuantity() != null) {
				setQuantity(getQuantity() + quantity);
			} else {
				setQuantity(quantity);
			}
		}
	}
	
	/**
	 * 计算折扣 这里是总的折扣
	 * @return
	 */
	@Transient
	public BigDecimal getDiscount() {
		isBonuds=true;
		BigDecimal originalPrice = new BigDecimal(0);
		BigDecimal currentPrice = new BigDecimal(0);
		for (Promotion promotion : getPromotions()) {
			if (promotion != null) {
				isBonuds=false;
				int promotionQuantity = getQuantity(promotion);
				BigDecimal originalPromotionPrice = getTempPrice(promotion);
				BigDecimal currentPromotionPrice = promotion.calculatePrice(promotionQuantity, originalPromotionPrice);
				originalPrice = originalPrice.add(originalPromotionPrice);
				currentPrice = currentPrice.add(currentPromotionPrice);
				Set<CartItem> cartItems = getCartItems(promotion);
				for (CartItem cartItem : cartItems) {
					if (cartItem != null && cartItem.getTempPrice() != null) {
						BigDecimal tempPrice;
						if (originalPromotionPrice.compareTo(new BigDecimal(0)) > 0) {
							tempPrice = currentPromotionPrice.divide(originalPromotionPrice, 50, RoundingMode.DOWN).multiply(cartItem.getTempPrice());
						} else {
							tempPrice = new BigDecimal(0);
						}
						cartItem.setTempPrice(tempPrice.compareTo(new BigDecimal(0)) > 0 ? tempPrice : new BigDecimal(0));
					}
				}
			}
		}
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null) {
					cartItem.setTempPrice(null);
				}
			}
		}
		Setting setting = SettingUtils.get();
		BigDecimal discount = setting.setScale(originalPrice.subtract(currentPrice));
		return discount.compareTo(new BigDecimal(0)) > 0 ? discount : new BigDecimal(0);
	}

	/**
	 * 计算折扣之后，每个产品应付的金额
	 * @return
	 */
	@Transient
	public BigDecimal getEffectivePrice() {
		BigDecimal effectivePrice = getPrice().subtract(getDiscount().divide(new BigDecimal(quantity)));
		return effectivePrice.compareTo(new BigDecimal(0)) > 0 ? effectivePrice : new BigDecimal(0);
	}
	
	
	@Transient
	public Set<Promotion> getPromotions() {
		Set<Promotion> allPromotions = new HashSet<Promotion>();
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null) {
					allPromotions.addAll(cartItem.getProduct().getValidPromotions());
				}
			}
		}
		Set<Promotion> promotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (isValid(promotion)) {
				promotions.add(promotion);
			}
		}
		return promotions;
	}

	@Transient
	private Set<CartItem> getCartItems(Promotion promotion) {
		Set<CartItem> cartItems = new HashSet<CartItem>();
		if (promotion != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().isValid(promotion)) {
					cartItems.add(cartItem);
				}
			}
		}
		return cartItems;
	}
	
	@Transient
	public Set<CartItem> getCartItems() {
		Set<CartItem> cartItems = new HashSet<CartItem>();
		cartItems.add(this);
		return cartItems;
	}
	
	@Transient
	private int getQuantity(Promotion promotion) {
		int quantity = 0;
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				quantity += cartItem.getQuantity();
			}
		}
		return quantity;
	}
	
	@Transient
	private BigDecimal getTempPrice(Promotion promotion) {
		BigDecimal tempPrice = new BigDecimal(0);
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getTempPrice() != null) {
				tempPrice = tempPrice.add(cartItem.getTempPrice());
			}
		}
		return tempPrice;
	}
	
	@Transient
	private boolean isValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		if (promotion.getMemberRanks() == null || promotion.getMemberRanks().isEmpty() || cart.getMember() == null || cart.getMember().getMemberRank() == null || !promotion.getMemberRanks().contains(cart.getMember().getMemberRank())) {
			return false;
		}
		Integer quantity = getQuantity(promotion);
		if ((promotion.getMinimumQuantity() != null && promotion.getMinimumQuantity() > quantity) || (promotion.getMaximumQuantity() != null && promotion.getMaximumQuantity() < quantity)) {
			return false;
		}
		BigDecimal price = getPrice(promotion);
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}

	@Transient
	private BigDecimal getPrice(Promotion promotion) {
		BigDecimal price = new BigDecimal(0);
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getSubtotal() != null) {
				price = price.add(cartItem.getSubtotal());
			}
		}
		return price;
	}

}