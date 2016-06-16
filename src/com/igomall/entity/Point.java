
package com.igomall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igomall.util.DateUtils;

/**
 * 积分记录表
 * @author blackboy2015
 *
 */
@Entity
@Table(name = "lx_point")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_point_sequence")
public class Point extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	public enum Type {

		register,

		login,

		sign,
	}

	private Type type;

	private Long balance;
	
	private String memo;

	private Member member;

	@JsonProperty
	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@JsonProperty
	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
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
	
	@Transient
	@JsonProperty
	public String getAddTime(){
		return DateUtils.formatDateToString(getCreateDate(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
}