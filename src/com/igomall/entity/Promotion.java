/*
 * 
 * 
 * 
 */
package com.igomall.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.igomall.util.FreemarkerUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import freemarker.template.TemplateException;

@Entity
@Table(name = "lx_promotion")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_promotion_sequence")
public class Promotion extends OrderEntity {

	private static final long serialVersionUID = 3536993535267962279L;

	private static final String PATH_PREFIX = "/promotion/content";

	private static final String PATH_SUFFIX = ".jhtml";

	private String name;

	private String title;

	private Date beginDate;

	private Date endDate;

	private Integer minimumQuantity;

	private Integer maximumQuantity;

	private BigDecimal minimumPrice;

	private BigDecimal maximumPrice;

	private String priceExpression;

	private String pointExpression;

	private Boolean isFreeShipping;

	private Boolean isCouponAllowed;

	private String introduction;

	private Set<MemberRank> memberRanks = new HashSet<MemberRank>();

	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

	private Set<Product> products = new HashSet<Product>();

	private Set<Brand> brands = new HashSet<Brand>();

	private Set<Coupon> coupons = new HashSet<Coupon>();

	private List<GiftItem> giftItems = new ArrayList<GiftItem>();
	
	private Integer rate;//推广比率
	
	@JsonProperty
	@Min(0)
	@Max(100)
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
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

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonProperty
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@JsonProperty
	@Min(0)
	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	@JsonProperty
	@Min(0)
	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getMaximumPrice() {
		return maximumPrice;
	}

	public void setMaximumPrice(BigDecimal maximumPrice) {
		this.maximumPrice = maximumPrice;
	}

	public String getPriceExpression() {
		return priceExpression;
	}

	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}

	public String getPointExpression() {
		return pointExpression;
	}

	public void setPointExpression(String pointExpression) {
		this.pointExpression = pointExpression;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsFreeShipping() {
		return isFreeShipping;
	}

	public void setIsFreeShipping(Boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	@JsonProperty
	@NotNull
	@Column(nullable = false)
	public Boolean getIsCouponAllowed() {
		return isCouponAllowed;
	}

	public void setIsCouponAllowed(Boolean isCouponAllowed) {
		this.isCouponAllowed = isCouponAllowed;
	}

	@Lob
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_promotion_member_rank")
	public Set<MemberRank> getMemberRanks() {
		return memberRanks;
	}

	public void setMemberRanks(Set<MemberRank> memberRanks) {
		this.memberRanks = memberRanks;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_promotion_product_category")
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_promotion_product")
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_promotion_brand")
	public Set<Brand> getBrands() {
		return brands;
	}

	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_promotion_coupon")
	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Valid
	@OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GiftItem> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(List<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	@Transient
	public boolean hasEnded() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	@Transient
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}

	@Transient
	public BigDecimal calculatePrice(Integer quantity, BigDecimal price) {
		if (price == null || StringUtils.isEmpty(getPriceExpression())) {
			return price;
		}
		BigDecimal result = new BigDecimal(0);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("price", price);
			result = new BigDecimal(FreemarkerUtils.process("#{(" + getPriceExpression() + ");M50}", model));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		if (result.compareTo(price) > 0) {
			return price;
		}
		return result.compareTo(new BigDecimal(0)) > 0 ? result : new BigDecimal(0);
	}

	@Transient
	public Long calculatePoint(Integer quantity, Long point) {
		if (point == null || StringUtils.isEmpty(getPointExpression())) {
			return point;
		}
		Long result = 0L;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("point", point);
			result = Double.valueOf(FreemarkerUtils.process("#{(" + getPointExpression() + ");M50}", model)).longValue();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (result < point) {
			return point;
		}
		return result > 0L ? result : 0L;
	}

	@Override
	public String toString() {
		return "Promotion [name=" + name + ", title=" + title + ", beginDate=" + beginDate + ", endDate=" + endDate + ", minimumQuantity=" + minimumQuantity + ", maximumQuantity=" + maximumQuantity + ", minimumPrice=" + minimumPrice + ", maximumPrice=" + maximumPrice + ", priceExpression=" + priceExpression + ", pointExpression=" + pointExpression + ", isFreeShipping=" + isFreeShipping + ", isCouponAllowed=" + isCouponAllowed + ", introduction=" + introduction + ", products=" + products + ", brands=" + brands + ", coupons=" + coupons + ", rate=" + rate + "]";
	}
	
	

}