package com.igomall.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "lx_groupBuyProduct")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_groupBuyProduct_sequence")
public class GroupBuyProduct extends BaseEntity {
	
	private static final long serialVersionUID = -4767220496234291722L;


	private Integer minimumQuantity;//每人最少需要购买多少件

	private Integer maximumQuantity;//每人最多可购买多少件
	
	private Integer minimumGroupQuantity;//团购的最少数量。只有当数量满足这个数量时，才是成功来的。否则团购活动失败
	
	private Integer maximumGroupQuantity;//团购的最大数量。
	
	private Product product;//产品对象
	
	private GroupBuy groupBuy;//
	
	
	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	public GroupBuy getGroupBuy() {
		return groupBuy;
	}

	public void setGroupBuy(GroupBuy groupBuy) {
		this.groupBuy = groupBuy;
	}

	@Min(0)
	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	@Min(0)
	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}
	
	@Min(0)
	public Integer getMinimumGroupQuantity() {
		return minimumGroupQuantity;
	}

	public void setMinimumGroupQuantity(Integer minimumGroupQuantity) {
		this.minimumGroupQuantity = minimumGroupQuantity;
	}
	@Min(0)
	public Integer getMaximumGroupQuantity() {
		return maximumGroupQuantity;
	}

	public void setMaximumGroupQuantity(Integer maximumGroupQuantity) {
		this.maximumGroupQuantity = maximumGroupQuantity;
	}

}
