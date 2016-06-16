
package com.igomall.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "lx_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_goods_sequence")
public class Goods extends BaseEntity {

	private static final long serialVersionUID = -6977025562650112419L;

	private Set<Product> products = new HashSet<Product>();

	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Transient
	public Set<SpecificationValue> getSpecificationValues() {
		Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
		if (getProducts() != null) {
			for (Product product : getProducts()) {
				specificationValues.addAll(product.getSpecificationValues());
			}
		}
		return specificationValues;
	}

}