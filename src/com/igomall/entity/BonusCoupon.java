
package com.igomall.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

/**
 * 奖金券
 * @author blackboy1987
 *
 */
@Entity
@Table(name = "lx_bonus_coupon")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_bonus_coupon_sequence")
public class BonusCoupon extends BaseEntity {
	
	public enum Type{
		member,
		shop
	}
	
	private static final long serialVersionUID = 2618141197280004148L;

	private Date endDate;//有效期

	private BigDecimal rate;//出局倍数

	private BigDecimal balance1;//已分配的奖金

	private BigDecimal balance;//奖金券的金额
	
	private BigDecimal balance2;//出局金额

	private Member member;

	private Shop shop;
	
	private Type type;
	
	private String memo;
	
	private Order orders;
	
	private Boolean isAlloted;//是否已经分配过了,false 未分配  true 已分配
	
	private BonusCoupon parent;//如果该奖金券是商家发放的。那么这个字段就表明其关联的奖金券对象
	
	public Boolean getIsAlloted() {
		return isAlloted;
	}

	public void setIsAlloted(Boolean isAlloted) {
		this.isAlloted = isAlloted;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public BonusCoupon getParent() {
		return parent;
	}

	public void setParent(BonusCoupon parent) {
		this.parent = parent;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Order getOrders() {
		return orders;
	}

	public void setOrders(Order orders) {
		this.orders = orders;
	}

	@Length(max=800)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getBalance1() {
		return balance1;
	}

	public void setBalance1(BigDecimal balance1) {
		this.balance1 = balance1;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getBalance2() {
		return balance2;
	}

	public void setBalance2(BigDecimal balance2) {
		this.balance2 = balance2;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && new Date().after(getEndDate());
	}
	
	@Transient
	public boolean hasOut() {
		if(balance1.compareTo(balance2)>=0){
			return true;
		}else{
			return false;
		}
	}
	
	@Transient
	public Boolean getIsOut() {
		return hasOut();
	}

	@Transient
	public Boolean getHasExpired() {
		return hasExpired();
	}

	@PreRemove
	public void preRemove() {
		
	}

}