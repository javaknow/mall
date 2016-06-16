
package com.igomall.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "lx_specification_value")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_specification_val_sequence")
public class SpecificationValue extends OrderEntity {

	private static final long serialVersionUID = -8624741017444123488L;

	private String name;

	private String image;

	private Specification specification;
	
	private Long taobaoId;//对应中的淘宝分类属性的id;

	private Set<Product> products = new HashSet<Product>();

	public Long getTaobaoId() {
		return taobaoId;
	}

	public void setTaobaoId(Long taobaoId) {
		this.taobaoId = taobaoId;
	}
	
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

	@Length(max = 200)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Specification getSpecification() {
		return specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
	}

	@ManyToMany(mappedBy = "specificationValues", fetch = FetchType.LAZY)
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "SpecificationValue [name=" + name + ", image=" + image
				+ ", taobaoId=" + taobaoId+ "]";
	}
	
	

}