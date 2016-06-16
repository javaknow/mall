
package com.igomall.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 流水记录表
 * @author 黎
 *
 */
@Entity
@Table(name = "lx_MoneyFlowingWater")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_MoneyFlowingWater_sequence")
public class MoneyFlowingWater extends BaseEntity {

	private static final long serialVersionUID = 5173064746543010067L;
	
	public enum Type{//流水类型
		
		balance,//资金账户
		
		balance1,//奖金券账户
		
		withdraw,//提现
		
		recharge//充值
	}

	private String content;//描述

	private String ip;//操作ip

	private BigDecimal beforeBalance;//变动之前的金额

	private BigDecimal balance;//变动的金额

	private BigDecimal afterBalance;//变动之后的金额

	private Member member;//会员
	
	private Type type;//类型

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotEmpty
	@Length(max = 1000)
	@Column(nullable = false, updatable = false, length = 1000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBeforeBalance() {
		return beforeBalance;
	}

	public void setBeforeBalance(BigDecimal beforeBalance) {
		this.beforeBalance = beforeBalance;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getAfterBalance() {
		return afterBalance;
	}

	public void setAfterBalance(BigDecimal afterBalance) {
		this.afterBalance = afterBalance;
	}
	
	

}