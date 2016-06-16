/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "lx_consultation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_consultation_sequence")
public class Consultation extends BaseEntity {

	private static final long serialVersionUID = -3950317769006303385L;

	private static final String PATH_PREFIX = "/consultation/content";

	private static final String PATH_SUFFIX = ".jhtml";

	private String content;

	private Boolean isShow;

	private String ip;

	private Member member;

	private Product product;

	private Consultation forConsultation;

	private Set<Consultation> replyConsultations = new HashSet<Consultation>();

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(nullable = false)
	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	@Column(nullable = false, updatable = false)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	public Consultation getForConsultation() {
		return forConsultation;
	}

	public void setForConsultation(Consultation forConsultation) {
		this.forConsultation = forConsultation;
	}

	@OneToMany(mappedBy = "forConsultation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Consultation> getReplyConsultations() {
		return replyConsultations;
	}

	public void setReplyConsultations(Set<Consultation> replyConsultations) {
		this.replyConsultations = replyConsultations;
	}

	@Transient
	public String getPath() {
		if (getProduct() != null && getProduct().getId() != null) {
			return PATH_PREFIX + "/" + getProduct().getId() + PATH_SUFFIX;
		}
		return null;
	}

}