/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "lx_member_rank")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_member_rank_sequence")
public class MemberRank extends BaseEntity {

	private static final long serialVersionUID = 3599029355500655209L;

	private String name;

	private Double scale;

	private BigDecimal amount;

	private Boolean isDefault;

	private Boolean isSpecial;

	private Set<Member> members = new HashSet<Member>();

	private Set<Promotion> promotions = new HashSet<Promotion>();
	
	private Map<MemberRank, BigDecimal> memberRate = new HashMap<MemberRank, BigDecimal>();

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "lx_memberRank_memberRate")
	public Map<MemberRank, BigDecimal> getMemberRate() {
		return memberRate;
	}

	public void setMemberRate(Map<MemberRank, BigDecimal> memberRate) {
		this.memberRate = memberRate;
	}
	
	

	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, unique = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	@Column(nullable = false, precision = 12, scale = 6)
	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(unique = true, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	@OneToMany(mappedBy = "memberRank", fetch = FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	@ManyToMany(mappedBy = "memberRanks", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@PreRemove
	public void preRemove() {
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getMemberRanks().remove(this);
			}
		}
	}

}