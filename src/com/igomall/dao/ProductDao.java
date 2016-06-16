
package com.igomall.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Goods;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Shop;
import com.igomall.entity.Tag;
import com.igomall.entity.Product.OrderType;

public interface ProductDao extends BaseDao<Product, Long> {

	boolean snExists(String sn);

	Product findBySn(String sn);

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

	List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count);

	List<Product> findList(Goods goods, Set<Product> excludes);

	List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count);

	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable);

	Page<Product> findPage(Member member, Pageable pageable);
	
	/**
	 * 查看用户收藏的产品
	 * @param member
	 * @return
	 */
	List<Product> findList(Member member);

	Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert);

	boolean isPurchased(Member member, Product product);

	List<Product> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders,Boolean isMarketable);

	List<Product> findFavoriteProduct(Member member);

	List<Product> findList(Promotion promotion, Integer first, Integer count);

	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable, Boolean isParent);

	List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count, Boolean isParent);

	List<Product> findList(Promotion promotion, Integer first, Integer count, Boolean isParent);

	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent);

	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent);

	Page<Product> findPage(Pageable pageable, Shop shop, Boolean flag);

}