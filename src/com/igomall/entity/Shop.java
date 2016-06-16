
package com.igomall.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igomall.entity.ShopAttribute.Type;
import com.igomall.util.JsonUtils;

/**
 * 店铺实体
 * @author black
 *
 */
@Entity
@Table(name = "lx_shop")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_shop_sequence")
public class Shop extends BaseEntity {
	
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 50;
	
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";
	
	private static final long serialVersionUID = 6818593958172120569L;

	public static String[] NOT_COPY_PROPERTIES = new String[] {"name","member"};
	
	private String name;//店铺名字
	
	private String logo;//店铺logo

	private String address;
	
	private String phone;

	private String mobile;
	
	private String content;
	
	private BigDecimal balance;//店铺的奖金池金额

	private BigDecimal rate1;//进入奖池的比率。用户每确认一笔订单之后，就有订单金额的相应比例进入该店铺的奖金账户
	
	private BigDecimal rate2;//每天定时11点分发给会员的奖金比率。
	
	private BigDecimal buyerRate;//买家确认订单之后，所返的奖金券的比率
	
	private BigDecimal buyerParentRate;//买家确认订单之后，其推荐人所返的奖金券的比率。
	
	private BigDecimal buyerParentParentRate;//买家确认订单之后，其推荐人的推荐人所返的奖金券的比率。
	
	private BigDecimal rate3;//奖金券出局比率
	
	private Integer days;//只对于在days天之内在本店铺消费的买家才发放奖金券
	
	private String attributeValue0;

	private String attributeValue1;

	private String attributeValue2;

	private String attributeValue3;

	private String attributeValue4;

	private String attributeValue5;

	private String attributeValue6;

	private String attributeValue7;

	private String attributeValue8;

	private String attributeValue9;
	
	private String attributeValue10;

	private String attributeValue11;

	private String attributeValue12;
	
	private String attributeValue13;

	private String attributeValue14;

	private String attributeValue15;
	
	private String attributeValue16;

	private String attributeValue17;

	private String attributeValue18;
	
	private String attributeValue19;

	private String attributeValue20;

	private String attributeValue21;
	
	private String attributeValue22;
	
	private String attributeValue23;

	private String attributeValue24;

	private String attributeValue25;
	
	private String attributeValue26;

	private String attributeValue27;

	private String attributeValue28;
	
	private String attributeValue29;

	private String attributeValue30;
	
	private String attributeValue31;
	
	private String attributeValue32;
	
	private String attributeValue33;

	private String attributeValue34;

	private String attributeValue35;
	
	private String attributeValue36;

	private String attributeValue37;

	private String attributeValue38;
	
	private String attributeValue39;

	private String attributeValue40;
	
	private String attributeValue41;
	
	private String attributeValue42;
	
	private String attributeValue43;

	private String attributeValue44;

	private String attributeValue45;
	
	private String attributeValue46;

	private String attributeValue47;

	private String attributeValue48;
	
	private String attributeValue49;

	private String attributeValue50;

	private Member member;
	
	private ShopRank shopRank;

	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();
	
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	private Set<Product> products = new HashSet<Product>();
	
	private Boolean isEnabled;
	
	private Double productAndDescription;
	
	private Double sellerServiceAttitude;
	
	private Double sellerDeliverySpeed;
	
	private Set<Member> favoriteMembers = new HashSet<Member>();
	
	
	@Transient
	@JsonProperty
	public Integer getStore_collect(){
		return favoriteMembers.size();
	}
	
	@Transient
	@JsonProperty
	public Integer getGoods_count(){
		return products.size();
	}
	
	@Lob
	@JsonProperty
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonProperty
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@ManyToMany(mappedBy = "favoriteShops", fetch = FetchType.LAZY)
	public Set<Member> getFavoriteMembers() {
		return favoriteMembers;
	}

	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
	}
	
	@JsonProperty
	public Double getProductAndDescription() {//商品描述符合度
		productAndDescription=5.0;
		return productAndDescription;
	}
	@JsonProperty
	public Double getSellerServiceAttitude() {//服务态度
		sellerServiceAttitude = 4.0;
		return sellerServiceAttitude;
	}
	@JsonProperty
	public Double getSellerDeliverySpeed() {//发货速度
		sellerDeliverySpeed = 3.0;
		return sellerDeliverySpeed;
	}

	public void setProductAndDescription(Double productAndDescription) {
		this.productAndDescription = productAndDescription;
	}
	public void setSellerServiceAttitude(Double sellerServiceAttitude) {
		this.sellerServiceAttitude = sellerServiceAttitude;
	}
	public void setSellerDeliverySpeed(Double sellerDeliverySpeed) {
		this.sellerDeliverySpeed = sellerDeliverySpeed;
	}
	@Transient
	@JsonProperty
	public Integer getProductCount() {
		Integer productCount = getProducts().size();
		return productCount;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public ShopRank getShopRank() {
		return shopRank;
	}

	public void setShopRank(ShopRank shopRank) {
		this.shopRank = shopRank;
	}

	@JsonProperty
	@Length(max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(max = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(max = 200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(max = 200)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(max = 200)
	public String getAttributeValue0() {
		return attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	@Length(max = 200)
	public String getAttributeValue1() {
		return attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	@Length(max = 200)
	public String getAttributeValue2() {
		return attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	@Length(max = 200)
	public String getAttributeValue3() {
		return attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	@Length(max = 200)
	public String getAttributeValue4() {
		return attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	@Length(max = 200)
	public String getAttributeValue5() {
		return attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	@Length(max = 200)
	public String getAttributeValue6() {
		return attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	@Length(max = 200)
	public String getAttributeValue7() {
		return attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	@Length(max = 200)
	public String getAttributeValue8() {
		return attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	@Length(max = 200)
	public String getAttributeValue9() {
		return attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}
	
	@Length(max = 200)
	public String getAttributeValue10() {
		return attributeValue10;
	}
	
	@Length(max = 200)
	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}

	@Length(max = 200)
	public String getAttributeValue11() {
		return attributeValue11;
	}

	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}

	@Length(max = 200)
	public String getAttributeValue12() {
		return attributeValue12;
	}

	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}

	@Length(max = 200)
	public String getAttributeValue13() {
		return attributeValue13;
	}

	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}

	@Length(max = 200)
	public String getAttributeValue14() {
		return attributeValue14;
	}

	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}

	@Length(max = 200)
	public String getAttributeValue15() {
		return attributeValue15;
	}

	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}

	@Length(max = 200)
	public String getAttributeValue16() {
		return attributeValue16;
	}

	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}

	@Length(max = 200)
	public String getAttributeValue17() {
		return attributeValue17;
	}

	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}

	@Length(max = 200)
	public String getAttributeValue18() {
		return attributeValue18;
	}

	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}

	@Length(max = 200)
	public String getAttributeValue19() {
		return attributeValue19;
	}

	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}

	@Length(max = 200)
	public String getAttributeValue20() {
		return attributeValue20;
	}

	public void setAttributeValue20(String attributeValue20) {
		this.attributeValue20 = attributeValue20;
	}

	@Length(max = 200)
	public String getAttributeValue21() {
		return attributeValue21;
	}

	public void setAttributeValue21(String attributeValue21) {
		this.attributeValue21 = attributeValue21;
	}

	@Length(max = 200)
	public String getAttributeValue22() {
		return attributeValue22;
	}

	public void setAttributeValue22(String attributeValue22) {
		this.attributeValue22 = attributeValue22;
	}

	@Length(max = 200)
	public String getAttributeValue23() {
		return attributeValue23;
	}

	public void setAttributeValue23(String attributeValue23) {
		this.attributeValue23 = attributeValue23;
	}

	@Length(max = 200)
	public String getAttributeValue24() {
		return attributeValue24;
	}

	public void setAttributeValue24(String attributeValue24) {
		this.attributeValue24 = attributeValue24;
	}

	@Length(max = 200)
	public String getAttributeValue25() {
		return attributeValue25;
	}

	public void setAttributeValue25(String attributeValue25) {
		this.attributeValue25 = attributeValue25;
	}

	@Length(max = 200)
	public String getAttributeValue26() {
		return attributeValue26;
	}

	public void setAttributeValue26(String attributeValue26) {
		this.attributeValue26 = attributeValue26;
	}

	@Length(max = 200)
	public String getAttributeValue27() {
		return attributeValue27;
	}

	public void setAttributeValue27(String attributeValue27) {
		this.attributeValue27 = attributeValue27;
	}

	@Length(max = 200)
	public String getAttributeValue28() {
		return attributeValue28;
	}

	public void setAttributeValue28(String attributeValue28) {
		this.attributeValue28 = attributeValue28;
	}

	@Length(max = 200)
	public String getAttributeValue29() {
		return attributeValue29;
	}

	public void setAttributeValue29(String attributeValue29) {
		this.attributeValue29 = attributeValue29;
	}

	@Length(max = 200)
	public String getAttributeValue30() {
		return attributeValue30;
	}

	public void setAttributeValue30(String attributeValue30) {
		this.attributeValue30 = attributeValue30;
	}
	
	@Length(max = 200)
	public String getAttributeValue31() {
		return attributeValue31;
	}

	public void setAttributeValue31(String attributeValue31) {
		this.attributeValue31 = attributeValue31;
	}
	
	@Length(max = 200)
	public String getAttributeValue32() {
		return attributeValue32;
	}

	public void setAttributeValue32(String attributeValue32) {
		this.attributeValue32 = attributeValue32;
	}

	@Length(max = 200)
	public String getAttributeValue33() {
		return attributeValue33;
	}

	public void setAttributeValue33(String attributeValue33) {
		this.attributeValue33 = attributeValue33;
	}

	@Length(max = 200)
	public String getAttributeValue34() {
		return attributeValue34;
	}

	public void setAttributeValue34(String attributeValue34) {
		this.attributeValue34 = attributeValue34;
	}

	@Length(max = 200)
	public String getAttributeValue35() {
		return attributeValue35;
	}

	public void setAttributeValue35(String attributeValue35) {
		this.attributeValue35 = attributeValue35;
	}

	@Length(max = 200)
	public String getAttributeValue36() {
		return attributeValue36;
	}

	public void setAttributeValue36(String attributeValue36) {
		this.attributeValue36 = attributeValue36;
	}

	@Length(max = 200)
	public String getAttributeValue37() {
		return attributeValue37;
	}

	public void setAttributeValue37(String attributeValue37) {
		this.attributeValue37 = attributeValue37;
	}

	@Length(max = 200)
	public String getAttributeValue38() {
		return attributeValue38;
	}

	public void setAttributeValue38(String attributeValue38) {
		this.attributeValue38 = attributeValue38;
	}

	@Length(max = 200)
	public String getAttributeValue39() {
		return attributeValue39;
	}

	public void setAttributeValue39(String attributeValue39) {
		this.attributeValue39 = attributeValue39;
	}

	@Length(max = 200)
	public String getAttributeValue40() {
		return attributeValue40;
	}

	public void setAttributeValue40(String attributeValue40) {
		this.attributeValue40 = attributeValue40;
	}

	@Length(max = 200)
	public String getAttributeValue41() {
		return attributeValue41;
	}

	public void setAttributeValue41(String attributeValue41) {
		this.attributeValue41 = attributeValue41;
	}

	@Length(max = 200)
	public String getAttributeValue42() {
		return attributeValue42;
	}

	public void setAttributeValue42(String attributeValue42) {
		this.attributeValue42 = attributeValue42;
	}

	@Length(max = 200)
	public String getAttributeValue43() {
		return attributeValue43;
	}

	public void setAttributeValue43(String attributeValue43) {
		this.attributeValue43 = attributeValue43;
	}

	@Length(max = 200)
	public String getAttributeValue44() {
		return attributeValue44;
	}

	public void setAttributeValue44(String attributeValue44) {
		this.attributeValue44 = attributeValue44;
	}

	@Length(max = 200)
	public String getAttributeValue45() {
		return attributeValue45;
	}

	public void setAttributeValue45(String attributeValue45) {
		this.attributeValue45 = attributeValue45;
	}

	@Length(max = 200)
	public String getAttributeValue46() {
		return attributeValue46;
	}

	public void setAttributeValue46(String attributeValue46) {
		this.attributeValue46 = attributeValue46;
	}

	@Length(max = 200)
	public String getAttributeValue47() {
		return attributeValue47;
	}

	public void setAttributeValue47(String attributeValue47) {
		this.attributeValue47 = attributeValue47;
	}

	@Length(max = 200)
	public String getAttributeValue48() {
		return attributeValue48;
	}

	public void setAttributeValue48(String attributeValue48) {
		this.attributeValue48 = attributeValue48;
	}

	@Length(max = 200)
	public String getAttributeValue49() {
		return attributeValue49;
	}

	public void setAttributeValue49(String attributeValue49) {
		this.attributeValue49 = attributeValue49;
	}

	@Length(max = 200)
	public String getAttributeValue50() {
		return attributeValue50;
	}

	public void setAttributeValue50(String attributeValue50) {
		this.attributeValue50 = attributeValue50;
	}

	@OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	
	@OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}
	
	
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getRate1() {
		return rate1;
	}

	public void setRate1(BigDecimal rate1) {
		this.rate1 = rate1;
	}
	
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getRate2() {
		return rate2;
	}

	public void setRate2(BigDecimal rate2) {
		this.rate2 = rate2;
	}

	
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getRate3() {
		return rate3;
	}

	public void setRate3(BigDecimal rate3) {
		this.rate3 = rate3;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Column(nullable = false)
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(BigDecimal buyerRate) {
		this.buyerRate = buyerRate;
	}

	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBuyerParentRate() {
		return buyerParentRate;
	}

	public void setBuyerParentRate(BigDecimal buyerParentRate) {
		this.buyerParentRate = buyerParentRate;
	}
	
	@NotNull(groups = Save.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	public BigDecimal getBuyerParentParentRate() {
		return buyerParentParentRate;
	}

	public void setBuyerParentParentRate(BigDecimal buyerParentParentRate) {
		this.buyerParentParentRate = buyerParentParentRate;
	}

	@Transient
	public Object getAttributeValue(ShopAttribute shopAttribute) {
		if (shopAttribute != null) {
			if (shopAttribute.getType() == Type.checkbox) {
				if (shopAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + shopAttribute.getPropertyIndex();
						String propertyValue = (String) PropertyUtils.getProperty(this, propertyName);
						if (propertyValue != null) {
							return JsonUtils.toObject(propertyValue, List.class);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (shopAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + shopAttribute.getPropertyIndex();
						return (String) PropertyUtils.getProperty(this, propertyName);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	@Transient
	public void setAttributeValue(ShopAttribute shopAttribute, Object attributeValue) {
		if (shopAttribute != null) {
			if (attributeValue instanceof String && StringUtils.isEmpty((String) attributeValue)) {
				attributeValue = null;
			}
			if (shopAttribute.getType() == Type.checkbox && (attributeValue instanceof List || attributeValue == null)) {
				if (shopAttribute.getPropertyIndex() != null) {
					if (attributeValue == null || (shopAttribute.getOptions() != null && shopAttribute.getOptions().containsAll((List<?>) attributeValue))) {
						try {
							String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + shopAttribute.getPropertyIndex();
							PropertyUtils.setProperty(this, propertyName, JsonUtils.toJson(attributeValue));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				if (shopAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + shopAttribute.getPropertyIndex();
						PropertyUtils.setProperty(this, propertyName, attributeValue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Transient
	public void removeAttributeValue() {
		for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
			try {
				PropertyUtils.setProperty(this, propertyName, null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
	
	@JsonProperty
	@Transient
	public Boolean getIs_own_shop(){
		return true;
	}
	
	@Transient
	@JsonProperty
	public Map<String,Object> getStore_desccredit(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("text", "描述");
		data.put("credit", "5.0");
		data.put("percent", "--");
		data.put("percent_class", "equal");
		data.put("percent_text", "平");
		data.put("", "");
		
		return data;
	}
	
	@Transient
	@JsonProperty
	public Map<String,Object> getStore_deliverycredit(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("text", "物流");
		data.put("credit", "5.0");
		data.put("percent", "--");
		data.put("percent_class", "equal");
		data.put("percent_text", "平");
		data.put("", "");
		
		return data;
	}
	
	@Transient
	@JsonProperty
	public Map<String,Object> getStore_servicecredit(){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("text", "服务");
		data.put("credit", "5.0");
		data.put("percent", "--");
		data.put("percent_class", "equal");
		data.put("percent_text", "平");
		data.put("", "");
		
		return data;
	}
	
	public Shop (){
		
	}

}