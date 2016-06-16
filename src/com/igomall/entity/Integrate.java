package com.igomall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 积分表。用来保存用户获取的积分记录
 * @author blackboy2015
 *
 */
@Entity
@Table(name = "lx_integrate")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_integrate_sequence")
public class Integrate extends BaseEntity {

	private static final long serialVersionUID = 7289311070215332869L;
	
	public enum Type {
		order,//订单
		login,//登陆
		register,//注册
		draw,//抽奖
	}
	
	private Type type;
	
	private Long point;
	
	private String memo;
	
	private Member member;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Column(nullable = false)
	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
