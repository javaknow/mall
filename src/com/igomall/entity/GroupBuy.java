
package com.igomall.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
/**
 * 团购
 * @author blackboy2015
 *
 */
@Entity
@Table(name = "lx_groupBuy")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_groupBuy_sequence")
public class GroupBuy extends BaseEntity {
	private static final long serialVersionUID = -1172800227311551625L;

	private String name;//团购名称
	
	private Date beginDate;//团购开始时间

	private Date endDate;//团购结束时间

	private Boolean isEnabled;//团购是否可用

	private String introduction;//团购介绍
	private Set<MemberRank> memberRanks = new HashSet<MemberRank>();//允许参与团购的会员级别
	
	private Set<GroupBuyProduct> groupBuyProducts = new HashSet<GroupBuyProduct>();//参与团购的产品


	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Lob
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_groupBuy_member_rank")
	public Set<MemberRank> getMemberRanks() {
		return memberRanks;
	}

	public void setMemberRanks(Set<MemberRank> memberRanks) {
		this.memberRanks = memberRanks;
	}
	
	@OneToMany(mappedBy = "groupBuy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<GroupBuyProduct> getGroupBuyProducts() {
		return groupBuyProducts;
	}

	public void setGroupBuyProducts(Set<GroupBuyProduct> groupBuyProducts) {
		this.groupBuyProducts = groupBuyProducts;
	}
	
	

}