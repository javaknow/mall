
package com.igomall.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Shop;
import com.igomall.entity.Tag;
import com.igomall.entity.Product.OrderType;

public interface ProductService extends BaseService<Product, Long> {

	boolean snExists(String sn);

	Product findBySn(String sn);

	boolean snUnique(String previousSn, String currentSn);

	List<Product> search(String keyword, Boolean isGift, Integer count);

	/**
	 * 查询产品
	 * @param productCategory 产品分类
	 * @param brand 品牌
	 * @param promotion 促销活动
	 * @param tags 产品标签
	 * @param attributeValue 产品属性
	 * @param startPrice 起始价格
	 * @param endPrice 结束价格
	 * @param isMarketable 是否上市
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否是正品
	 * @param isOutOfStock 是否库存紧急
	 * @param isStockAlert 是否库存紧急时候提醒
	 * @param orderType 排序类型
	 * @param count 数量
	 * @param filters 过来条件
	 * @param orders 排序条件
	 * @return
	 */
	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,String cacheRegion);

	List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count);

	List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count);

	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable);

	Page<Product> findPage(Member member, Pageable pageable);
	
	/**
	 * 查看用户收藏的产品
	 * @param member
	 * @return
	 */
	List<Product> findList(Member member);

	/**
	 * 
	 * @param favoriteMember
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isGift
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @return
	 */
	Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert);

	/**
	 * 
	 * @param member
	 * @param product
	 * @return
	 */
	boolean isPurchased(Member member, Product product);

	/**
	 * 
	 * @param id
	 * @return
	 */
	long viewHits(Long id);

	List<Product> findFavoriteProduct(Member member);

	/**
	 * 
	 * @param promotion
	 * @param first
	 * @param count
	 * @return
	 */
	List<Product> findList(Promotion promotion, Integer first, Integer count);

	/**
	 * 
	 * @param productCategory
	 * @param brand
	 * @param promotion
	 * @param tags
	 * @param attributeValue
	 * @param startPrice
	 * @param endPrice
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isGift
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param orderType
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @return
	 */
	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 
	 * @param productCategory
	 * @param brand
	 * @param promotion
	 * @param tags
	 * @param attributeValue
	 * @param startPrice
	 * @param endPrice
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isGift
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param orderType
	 * @param pageable
	 * @param isParent
	 * @return
	 */
	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable,Boolean isParent);

	/**
	 * 
	 * @param productCategory
	 * @param beginDate
	 * @param endDate
	 * @param first
	 * @param count
	 * @param isParent
	 * @return
	 */
	List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count,Boolean isParent);

	/**
	 * 
	 * @param promotion
	 * @param first
	 * @param count
	 * @param isParent
	 * @return
	 */
	List<Product> findList(Promotion promotion, Integer first, Integer count, Boolean isParent);

	/**
	 * 
	 * @param productCategory
	 * @param brand
	 * @param promotion
	 * @param tags
	 * @param attributeValue
	 * @param startPrice
	 * @param endPrice
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isGift
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param orderType
	 * @param count
	 * @param filters
	 * @param orders
	 * @param isParent
	 * @return
	 */
	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent);

	/**
	 * 
	 * @param productCategory
	 * @param brand
	 * @param promotion
	 * @param tags
	 * @param attributeValue
	 * @param startPrice
	 * @param endPrice
	 * @param isMarketable
	 * @param isList
	 * @param isTop
	 * @param isGift
	 * @param isOutOfStock
	 * @param isStockAlert
	 * @param orderType
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @param isParent
	 * @return
	 */
	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent);

	Page<Product> findPage(Pageable pageable, Shop shop, Boolean flag);

}