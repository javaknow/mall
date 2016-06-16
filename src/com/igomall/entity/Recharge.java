package com.igomall.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 充值记录表
 * @author blackboy
 *
 */
@Entity
@Table(name = "lx_recharge")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_recharge_sequence")
public class Recharge extends BaseEntity {

	private String sn;//单号

	public enum Status {
		wait,//待付款
		success,//充值成功
		failure//充值失败
	}
	
	public enum Type {//充值类型
		rechargeCard,//充值卡充值
		
		adminRecharege,//后台充值
		
		memberRecharge,//会员自己充值
	}

	private static final long serialVersionUID = 7771489648414525176L;

	private BigDecimal balance;// 充值金额

	private Admin operator;// 操作人

	private String memo;// 备注

	private Member member;// 充值的会员

	private BigDecimal fee;// 充值手续费

	private Status status;// 状态

	private BigDecimal realBalance;// 实到金额金额

	private Payment payment;
	
	
	@OneToOne(mappedBy = "recharge", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Column(precision = 21, scale = 6)
	public BigDecimal getRealBalance() {
		return realBalance;
	}

	public void setRealBalance(BigDecimal realBalance) {
		this.realBalance = realBalance;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Admin getOperator() {
		return operator;
	}

	public void setOperator(Admin operator) {
		this.operator = operator;
	}

	@Column(updatable = false,length=8000)
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

}