
package com.igomall.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "lx_specification")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_specification_sequence")
public class Specification extends OrderEntity {

	private static final long serialVersionUID = -6346775052811140926L;

	public enum Type {

		text,

		image
	};

	private String name;

	private Type type;

	private String memo;
	
	private Long taobaoId;//对应中的淘宝分类属性的id;

	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

	private Set<Product> products = new HashSet<Product>();

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	

	public Long getTaobaoId() {
		return taobaoId;
	}

	public void setTaobaoId(Long taobaoId) {
		this.taobaoId = taobaoId;
	}

	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "specification", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order asc")
	public List<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(List<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	@ManyToMany(mappedBy = "specifications", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
}