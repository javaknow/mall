
package com.igomall.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
/**
 * 积分兑换
 * @author 黎
 *
 */
@Entity
@Table(name = "lx_integral_exchange_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_integral_exchange_product_sequence")
public class IntegralExchangeProduct extends OrderEntity {
	
	private static final long serialVersionUID = -3841509259858171035L;

	private Product product;

	private Date beginDate;

	private Date endDate;

	private Long point;//所需积分数额
	
	private Integer stock;//数量。可以用来积分兑换的数量
	
	private IntegralExchange integralExchange;
	
	private Long productId;
	
	@Transient
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public IntegralExchange getIntegralExchange() {
		return integralExchange;
	}

	public void setIntegralExchange(IntegralExchange integralExchange) {
		this.integralExchange = integralExchange;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Min(0)
	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}


	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	@PreRemove
	public void preRemove() {
		
	}

}