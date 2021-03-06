/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.igomall.Setting;
import com.igomall.util.SettingUtils;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "lx_shipping_method")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_shipping_method_sequence")
public class ShippingMethod extends OrderEntity {

	private static final long serialVersionUID = 5873163245980853245L;

	private String name;

	private Integer firstWeight;

	private Integer continueWeight;

	private BigDecimal firstPrice;

	private BigDecimal continuePrice;

	private String icon;

	private String description;

	private DeliveryCorp defaultDeliveryCorp;

	private Set<PaymentMethod> paymentMethods = new HashSet<PaymentMethod>();

	private Set<Order> orders = new HashSet<Order>();

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Min(0)
	@Column(nullable = false)
	public Integer getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}

	@NotNull
	@Min(1)
	@Column(nullable = false)
	public Integer getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(Integer continueWeight) {
		this.continueWeight = continueWeight;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	@Length(max = 200)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public DeliveryCorp getDefaultDeliveryCorp() {
		return defaultDeliveryCorp;
	}

	public void setDefaultDeliveryCorp(DeliveryCorp defaultDeliveryCorp) {
		this.defaultDeliveryCorp = defaultDeliveryCorp;
	}

	@ManyToMany(mappedBy = "shippingMethods", fetch = FetchType.LAZY)
	public Set<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	@OneToMany(mappedBy = "shippingMethod", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Transient
	public BigDecimal calculateFreight(Integer weight) {
		Setting setting = SettingUtils.get();
		BigDecimal freight = new BigDecimal(0);
		if (weight != null) {
			if (weight <= getFirstWeight() || getContinuePrice().compareTo(new BigDecimal(0)) == 0) {
				freight = getFirstPrice();
			} else {
				double contiuneWeightCount = Math.ceil((weight - getFirstWeight()) / (double) getContinueWeight());
				freight = getFirstPrice().add(getContinuePrice().multiply(new BigDecimal(contiuneWeightCount)));
			}
		}
		return setting.setScale(freight);
	}

	@PreRemove
	public void preRemove() {
		Set<PaymentMethod> paymentMethods = getPaymentMethods();
		if (paymentMethods != null) {
			for (PaymentMethod paymentMethod : paymentMethods) {
				paymentMethod.getShippingMethods().remove(this);
			}
		}
		Set<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setShippingMethod(null);
			}
		}
	}

}