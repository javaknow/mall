/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "lx_order_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_order_log_sequence")
public class OrderLog extends BaseEntity {

	private static final long serialVersionUID = -2704154761295319939L;

	public enum Type {

		create,

		modify,

		confirm,

		payment,

		refunds,

		shipping,

		returns,

		complete,

		cancel,

		other
	};

	private Type type;

	private String operator;

	private String content;

	private Order order;

	public OrderLog() {
	}

	public OrderLog(Type type, String operator) {
		this.type = type;
		this.operator = operator;
	}

	public OrderLog(Type type, String operator, String content) {
		this.type = type;
		this.operator = operator;
		this.content = content;
	}

	@Column(nullable = false, updatable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(updatable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}