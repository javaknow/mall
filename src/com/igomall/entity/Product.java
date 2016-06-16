
package com.igomall.entity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igomall.BigDecimalNumericFieldBridge;
import com.igomall.CommonAttributes;
import com.igomall.util.FreemarkerUtils;

import freemarker.template.TemplateException;

@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "lx_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_product_sequence")
public class Product extends BaseEntity {

	private static final long serialVersionUID = 2167830430439593293L;

	public static final String HITS_CACHE_NAME = "productHits";

	public static final int HITS_CACHE_INTERVAL = 600000;

	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	public static final String FULL_NAME_SPECIFICATION_PREFIX = "[";

	public static final String FULL_NAME_SPECIFICATION_SUFFIX = "]";

	public static final String FULL_NAME_SPECIFICATION_SEPARATOR = " ";

	private static String staticPath;
	
	public enum OrderType {

		topDesc,

		priceAsc,

		priceDesc,

		salesDesc,
		
		salesAsc,

		scoreDesc,

		dateDesc,
		
		dateAsc
	}

	private String sn;

	private String name;

	private String fullName;

	private BigDecimal price;

	private BigDecimal cost;

	private BigDecimal marketPrice;

	private String image;

	private String unit;

	private Double weight;//重量

	private Integer stock;

	private Integer allocatedStock;

	private String stockMemo;

	private Long point;

	private Boolean isMarketable;//是否上架，true：已上架  false 未上架

	private Boolean isList;//是否列出，true：已列出  false 未列出

	private Boolean isTop;//是否置顶，true：已上架  false 未置顶

	private Boolean isGift;//是否是赠品，true：已上架  false 未上架

	private String introduction;

	private String memo;

	private String keyword;

	private String seoTitle;

	private String seoKeywords;

	private String seoDescription;

	private Float score;

	private Long totalScore;

	private Long scoreCount;

	private Long hits;

	private Long weekHits;

	private Long monthHits;

	private Long sales;

	private Long weekSales;

	private Long monthSales;

	private Date weekHitsDate;

	private Date monthHitsDate;

	private Date weekSalesDate;

	private Date monthSalesDate;

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

	private ProductCategory productCategory;

	private Goods goods;

	private Brand brand;

	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	private Set<Review> reviews = new HashSet<Review>();

	private Set<Consultation> consultations = new HashSet<Consultation>();

	private Set<Tag> tags = new HashSet<Tag>();

	private Set<Member> favoriteMembers = new HashSet<Member>();

	private Set<Specification> specifications = new HashSet<Specification>();

	private Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();

	private Set<Promotion> promotions = new HashSet<Promotion>();

	private Set<CartItem> cartItems = new HashSet<CartItem>();

	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	private Set<GiftItem> giftItems = new HashSet<GiftItem>();

	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();

	private Map<MemberRank, BigDecimal> memberPrice = new HashMap<MemberRank, BigDecimal>();

	private Map<Parameter, String> parameterValue = new HashMap<Parameter, String>();
	
	private String proxyShop;//代理的店铺名字
	
	private String shopUrl;//代理店铺的网址
	
	private Shop shop;
	
	private String skuProps;
	
	private String cateProps;
	
	private String inputValues;
	
	private String originalUrl;//产品对应的代理商的链接；
	
	private Boolean isParent;//这个字段用来查询显示的 如果是true表明显示 如果是false 不显示。默认是false
	
	private Integer commentCount;//评论数量
	
	private Double nicePercent;//好评率
	
	
	@Transient
	@JsonProperty
	private Long getShopId(){
		return getShop().getId();
	}
	
	@Transient
	@JsonProperty
	private String getShopName(){
		return getShop().getName();
	}
	
	@Transient
	@JsonProperty
	public Boolean getIsOnLimitBuy() {
		Boolean isOnLimitBuy = true;
		return isOnLimitBuy;
	}

	@JsonProperty
	public Double getNicePercent() {
		nicePercent = 99.9;
		return nicePercent;
	}

	public void setNicePercent(Double nicePercent) {
		this.nicePercent = nicePercent;
	}

	@JsonProperty
	public Integer getCommentCount() {
		commentCount = 123;
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}


	@JsonProperty
	@Transient
	public Double getDiscount() {
		Double discount = getPrice().multiply(new BigDecimal(10)).divide(getMarketPrice(),1,RoundingMode.HALF_UP).doubleValue();
		return discount;
	}

	
	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		if(isParent==null){
			isParent=false;
		}
		this.isParent = isParent;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	@Length(max = 4000)
	public String getSkuProps() {
		return skuProps;
	}

	public void setSkuProps(String skuProps) {
		this.skuProps = skuProps;
	}

	@Length(max = 4000)
	public String getCateProps() {
		return cateProps;
	}

	public void setCateProps(String cateProps) {
		this.cateProps = cateProps;
	}

	@Length(max = 4000)
	public String getInputValues() {
		return inputValues;
	}

	public void setInputValues(String inputValues) {
		this.inputValues = inputValues;
	}

	@JsonProperty
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getProxyShop() {
		return proxyShop;
	}

	public void setProxyShop(String proxyShop) {
		this.proxyShop = proxyShop;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	static {
		try {
			//File igomallXmlFile = new ClassPathResource(CommonAttributes.LX_XML_PATH).getFile();
			File igomallXmlFile = new File(CommonAttributes.LX_XML_PATH);
			org.dom4j.Document document = new SAXReader().read(igomallXmlFile);
			org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/lx/template[@id='productContent']");
			staticPath = element.attributeValue("staticPath");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, unique = true, length = 100)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
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
	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Min(0)
	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	@Length(max = 200)
	public String getStockMemo() {
		return stockMemo;
	}

	public void setStockMemo(String stockMemo) {
		this.stockMemo = stockMemo;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Min(0)
	@Column(nullable = false)
	public Long getPoint() {
		return point;
	}
	
	@JsonProperty
	public void setPoint(Long point) {
		this.point = point;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsList() {
		return isList;
	}

	public void setIsList(Boolean isList) {
		this.isList = isList;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsGift() {
		return isGift;
	}

	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}

	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Length(max = 200)
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		if (keyword != null) {
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keyword = keyword;
	}

	@Length(max = 200)
	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Length(max = 200)
	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null) {
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.seoKeywords = seoKeywords;
	}

	@Length(max = 200)
	@JsonProperty
	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NumericField
	@Column(nullable = false, precision = 12, scale = 6)
	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Column(nullable = false)
	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = false)
	public Long getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = false)
	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getWeekHits() {
		return weekHits;
	}

	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getMonthHits() {
		return monthHits;
	}

	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = false)
	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	@Column(nullable = false)
	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	@Column(nullable = false)
	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	@Column(nullable = false)
	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	@Column(nullable = false)
	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
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

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Valid
	@ElementCollection
	@CollectionTable(name = "lx_product_product_image")
	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<Consultation> getConsultations() {
		return consultations;
	}

	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_product_tag")
	@OrderBy("order asc")
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@ManyToMany(mappedBy = "favoriteProducts", fetch = FetchType.LAZY)
	public Set<Member> getFavoriteMembers() {
		return favoriteMembers;
	}

	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_product_specification")
	@OrderBy("order asc")
	public Set<Specification> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lx_product_specification_value")
	@OrderBy("specification asc")
	public Set<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(Set<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	@ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "gift", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<GiftItem> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<ProductNotify> getProductNotifies() {
		return productNotifies;
	}

	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "lx_product_member_price")
	public Map<MemberRank, BigDecimal> getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Map<MemberRank, BigDecimal> memberPrice) {
		this.memberPrice = memberPrice;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "lx_product_parameter_value")
	public Map<Parameter, String> getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Map<Parameter, String> parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Transient
	public String getAttributeValue(Attribute attribute) {
		if (attribute != null && attribute.getPropertyIndex() != null) {
			try {
				String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
				return (String) PropertyUtils.getProperty(this, propertyName);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Transient
	public void setAttributeValue(Attribute attribute, String value) {
		if (attribute != null && attribute.getPropertyIndex() != null) {
			if (StringUtils.isEmpty(value)) {
				value = null;
			}
			if (value == null || (attribute.getOptions() != null && attribute.getOptions().contains(value))) {
				try {
					String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
					PropertyUtils.setProperty(this, propertyName, value);
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

	@Transient
	public List<Product> getSiblings() {
		List<Product> siblings = new ArrayList<Product>();
		if (getGoods() != null && getGoods().getProducts() != null) {
			for (Product product : getGoods().getProducts()) {
				if (!this.equals(product)) {
					siblings.add(product);
				}
			}
		}
		return siblings;
	}

	@JsonProperty
	@Transient
	public String getPath() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", getId());
		model.put("createDate", getCreateDate());
		model.put("modifyDate", getModifyDate());
		model.put("sn", getSn());
		model.put("name", getName());
		model.put("fullName", getFullName());
		model.put("seoTitle", getSeoTitle());
		model.put("seoKeywords", getSeoKeywords());
		model.put("seoDescription", getSeoDescription());
		model.put("productCategory", getProductCategory());
		try {
			return FreemarkerUtils.process(staticPath, model);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@JsonProperty
	@Transient
	public String getThumbnail() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getThumbnail();
		}
		return null;
	}

	@Transient
	public List<String> getThumbnails(){
		List<String> images = new ArrayList<String>();
		if(getProductImages()!=null&&!getProductImages().isEmpty()){
			for (ProductImage productImage : getProductImages()) {
				images.add(productImage.getThumbnail());
			}
		}
		
		return images;
	}
	
	@JsonProperty
	@Transient
	public String getMedium() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getMedium();
		}
		return null;
	}

	@Transient
	public List<String> getMediums(){
		List<String> images = new ArrayList<String>();
		if(getProductImages()!=null&&!getProductImages().isEmpty()){
			for (ProductImage productImage : getProductImages()) {
				images.add(productImage.getMedium());
			}
		}
		
		return images;
	}
	
	@JsonProperty
	@Transient
	public String getLarge() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getLarge();
		}
		return null;
	}
	
	@Transient
	public List<String> getLarges(){
		List<String> images = new ArrayList<String>();
		if(getProductImages()!=null&&!getProductImages().isEmpty()){
			for (ProductImage productImage : getProductImages()) {
				images.add(productImage.getLarge());
			}
		}
		
		return images;
	}
	
	@JsonProperty
	@Transient
	public String getSource() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getSource();
		}
		return null;
	}

	@Transient
	public List<String> getSources(){
		List<String> images = new ArrayList<String>();
		if(getProductImages()!=null&&!getProductImages().isEmpty()){
			for (ProductImage productImage : getProductImages()) {
				images.add(productImage.getSource());
			}
		}
		
		return images;
	}

	@Transient
	public Set<Promotion> getValidPromotions() {
		Set<Promotion> allPromotions = new HashSet<Promotion>();
		if (getPromotions() != null) {
			allPromotions.addAll(getPromotions());
		}
		if (getProductCategory() != null && getProductCategory().getPromotions() != null) {
			allPromotions.addAll(getProductCategory().getPromotions());
		}
		if (getBrand() != null && getBrand().getPromotions() != null) {
			allPromotions.addAll(getBrand().getPromotions());
		}
		Set<Promotion> validPromotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (promotion != null && promotion.hasBegun() && !promotion.hasEnded() && promotion.getMemberRanks() != null && !promotion.getMemberRanks().isEmpty()) {
				validPromotions.add(promotion);
			}
		}
		return validPromotions;
	}

	@Transient
	public Integer getAvailableStock() {
		Integer availableStock = null;
		if (getStock() != null && getAllocatedStock() != null) {
			availableStock = getStock() - getAllocatedStock();
			if (availableStock < 0) {
				availableStock = 0;
			}
		}
		return availableStock;
	}

	@Transient
	public Boolean getIsOutOfStock() {
		return getStock() != null && getAllocatedStock() != null && getAllocatedStock() >= getStock();
	}

	@Transient
	public boolean isValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded() || promotion.getMemberRanks() == null || promotion.getMemberRanks().isEmpty()) {
			return false;
		}
		if (getValidPromotions().contains(promotion)) {
			return true;
		}
		return false;
	}

	@PreRemove
	public void preRemove() {
		Set<Member> favoriteMembers = getFavoriteMembers();
		if (favoriteMembers != null) {
			for (Member favoriteMember : favoriteMembers) {
				favoriteMember.getFavoriteProducts().remove(this);
			}
		}
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getProducts().remove(this);
			}
		}
		Set<OrderItem> orderItems = getOrderItems();
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				orderItem.setProduct(null);
			}
		}
	}

	@PrePersist
	public void prePersist() {
		if (getStock() == null) {
			setAllocatedStock(0);
		}
		setScore(0F);
	}

	@PreUpdate
	public void preUpdate() {
		if (getStock() == null) {
			setAllocatedStock(0);
		}
		if (getTotalScore() != null && getScoreCount() != null && getScoreCount() != 0) {
			setScore((float) getTotalScore() / getScoreCount());
		} else {
			setScore(0F);
		}
	}

	@JsonProperty
	@Transient
	public BigDecimal getEffectivePrice() {
		BigDecimal effectivePrice = getPrice();
		Set<Promotion> promotions = getPromotions();
		for (Promotion promotion : promotions) {
			if(promotion!=null){
				effectivePrice = promotion.calculatePrice(1, effectivePrice);
				break;
			}
		}
		
		return effectivePrice;
	}
	
	@Transient
	@JsonProperty
	public Boolean getIsPresell(){
		return false;
	}
	
	@Transient
	@JsonProperty
	public Boolean getIsFcode(){
		return false;
	}
	
	@Transient
	@JsonProperty
	public Boolean getIsVirtual(){
		return false;
	}
	
	
	@Transient
	@JsonProperty
	public Map<String,String> getSpec_name(){
		Set<Specification> specifications = getSpecifications();
		Map<String,String> spec_name = new HashMap<String,String>();
		for (Specification specification : specifications) {
			spec_name.put(specification.getId()+"", specification.getName());
		}
		return spec_name;
	}
	
	@Transient
	@JsonProperty
	public Map<String, Map<String, String>> getSpec_value(){
		Set<SpecificationValue> specificationValues = getGoods().getSpecificationValues();
		Map<String, Map<String, String>> spec_value = new HashMap<String,Map<String,String>>();
		for (SpecificationValue specificationValue : specificationValues) {
			String specificationId = specificationValue.getSpecification().getId()+"";
			if(spec_value.containsKey(specificationId)){
				spec_value.get(specificationId).put(""+specificationValue.getId(),specificationValue.getName());
			}else{
				Map<String, String> list = new HashMap<String,String>();
				list.put(""+specificationValue.getId(),specificationValue.getName());
				spec_value.put(specificationId, list);
			}
		}
		return spec_value;
	}
}