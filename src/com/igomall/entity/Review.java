
package com.igomall.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 评论实体类
 * @author blackboy
 *
 */
@Entity
@Table(name = "lx_review")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_review_sequence")
public class Review extends BaseEntity {

	private static final long serialVersionUID = 8795901519290584100L;

	private static final String PATH_PREFIX = "/review/content";

	private static final String PATH_SUFFIX = ".jhtml";

	public enum Type {

		positive,

		moderate,

		negative
	}

	private Integer score;//评分

	private String content;//评论的内容

	private Boolean isShow;//是否显示

	private String ip;//评论的ip

	private Member member;//会员

	private Product product;//产品
	
	private List<ReviewImage> reviewImages = new ArrayList<ReviewImage>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "lx_review_review_image")
	public List<ReviewImage> getReviewImages() {
		return reviewImages;
	}

	public void setReviewImages(List<ReviewImage> reviewImages) {
		this.reviewImages = reviewImages;
	}
	
	@NotNull
	@Min(1)
	@Max(5)
	@Column(nullable = false, updatable = false)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

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

	@Transient
	public String getPath() {
		if (getProduct() != null && getProduct().getId() != null) {
			return PATH_PREFIX + "/" + getProduct().getId() + PATH_SUFFIX;
		}
		return null;
	}
}