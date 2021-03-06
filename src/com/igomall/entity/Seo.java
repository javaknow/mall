/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "lx_seo")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_seo_sequence")
public class Seo extends BaseEntity {

	private static final long serialVersionUID = -3503657242384822672L;

	public enum Type {

		index,

		productList,

		productSearch,

		productContent,

		articleList,

		articleSearch,

		articleContent,

		brandList,

		brandContent
	}

	private Type type;

	private String title;

	private String keywords;

	private String description;

	@Column(nullable = false, updatable = false, unique = true)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(max = 200)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		if (keywords != null) {
			keywords = keywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keywords = keywords;
	}

	@Length(max = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}