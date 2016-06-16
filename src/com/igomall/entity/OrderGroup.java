
package com.igomall.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "lx_order_group")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_order_group_sequence")
public class OrderGroup extends BaseEntity {

	private static final long serialVersionUID = 604676725177721020L;
	
	private String sn;// 订单编号

	private Member member;

	private BigDecimal amountPaid;
	
	private List<Order> orders = new ArrayList<Order>();
	
	@JsonProperty
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@JsonProperty
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@JsonProperty
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "orderGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	
	@PreUpdate
	public void preUpdate() {
		
	}

	@PreRemove
	public void preRemove() {
		
	}

}