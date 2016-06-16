
package com.igomall.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Embeddable
public class ReviewImage implements Serializable, Comparable<ReviewImage> {

	private static final long serialVersionUID = -7038922245919945900L;

	private String title;

	private String source;

	private Integer order;

	private MultipartFile file;

	@Length(max = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Min(0)
	@Column(name = "orders")
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Transient
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Transient
	public boolean isEmpty() {
		return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getSource()));
	}

	public int compareTo(ReviewImage productImage) {
		return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
	}
}