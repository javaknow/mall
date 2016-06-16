
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
public class ProductImage implements Serializable, Comparable<ProductImage> {

	private static final long serialVersionUID = -673883300094536107L;

	private String title;

	private String source;

	private String large;

	private String medium;

	private String thumbnail;

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

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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
		return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getSource()) || StringUtils.isEmpty(getLarge()) || StringUtils.isEmpty(getMedium()) || StringUtils.isEmpty(getThumbnail()));
	}

	public int compareTo(ProductImage productImage) {
		return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
	}

	@Override
	public String toString() {
		return "ProductImage [title=" + title + ", source=" + source + ", large=" + large + ", medium=" + medium + ", thumbnail=" + thumbnail + ", order=" + order + ", file=" + file + "]";
	}

	
}