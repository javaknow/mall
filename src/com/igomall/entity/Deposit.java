
package com.igomall.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "lx_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_deposit_sequence")
public class Deposit extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	public enum Type {

		memberRecharge,

		memberPayment,

		adminRecharge,

		adminChargeback,

		adminPayment,

		adminRefunds,
		
		rechargeCard,//充值卡充值
		
		voucher, //代金券充值
		
		redpacket,//红包领取充值
	}

	private Type type;

	private BigDecimal credit;

	private BigDecimal debit;

	private BigDecimal balance;
	
	private BigDecimal freezeBalance;

	private String operator;

	private String memo;

	private Member member;

	private Order order;

	private Payment payment;

	@JsonProperty
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	@JsonProperty
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getFreezeBalance() {
		return freezeBalance;
	}

	public void setFreezeBalance(BigDecimal freezeBalance) {
		this.freezeBalance = freezeBalance;
	}

	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@JsonProperty
	@Length(max = 200)
	@Column(updatable = false)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}