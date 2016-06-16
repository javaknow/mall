package com.igomall.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 充值卡
 * @author blackboy
 *
 */
@Entity
@Table(name = "lx_red_packet")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_red_packet_sequence")
public class RedPacket extends BaseEntity {
	
	private static final long serialVersionUID = -4246928131694877878L;


	private Boolean isUsed;//是否使用

	private Date usedDate;//使用时间

	private String code;//编号
	
	private BigDecimal balance;// 充值卡的金额

	private String memo;// 备注

	private Member member;// 充值的会员
	
	private Date startDate;//开始时间
	
	private Date endDate;//结束时间
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(nullable = false)
	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Date getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}
	
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Column(updatable = false,length=8000)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
	@Transient
	public Boolean getIsExperid(){
		return new Date().compareTo(getEndDate())>0||new Date().compareTo(getStartDate())<0;
	}

}