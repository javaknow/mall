
package com.igomall.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 积分兑换
 * @author 黎
 *
 */
@Entity
@Table(name = "lx_integral_exchange")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_integral_exchange_sequence")
public class IntegralExchange extends OrderEntity {
	
	private static final long serialVersionUID = -3841509259858171035L;
	
	private List<IntegralExchangeProduct> integralExchangeProducts = new ArrayList<IntegralExchangeProduct>();
	
	@JsonProperty
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "integralExchange", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order asc")
	public List<IntegralExchangeProduct> getIntegralExchangeProducts() {
		return integralExchangeProducts;
	}

	public void setIntegralExchangeProducts(List<IntegralExchangeProduct> integralExchangeProducts) {
		this.integralExchangeProducts = integralExchangeProducts;
	}
	
	

}