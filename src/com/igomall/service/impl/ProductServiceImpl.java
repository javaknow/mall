
package com.igomall.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.ProductDao;
import com.igomall.entity.Attribute;
import com.igomall.entity.Brand;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Promotion;
import com.igomall.entity.Shop;
import com.igomall.entity.Tag;
import com.igomall.entity.Product.OrderType;
import com.igomall.service.ProductService;
import com.igomall.service.StaticService;

@Service("productServiceImpl")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService, DisposableBean {

	private long viewHitsTime = System.currentTimeMillis();

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "productDaoImpl")
	public void setBaseDao(ProductDao productDao) {
		super.setBaseDao(productDao);
	}

	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return productDao.snExists(sn);
	}

	@Transactional(readOnly = true)
	public Product findBySn(String sn) {
		return productDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public boolean snUnique(String previousSn, String currentSn) {
		if (StringUtils.equalsIgnoreCase(previousSn, currentSn)) {
			return true;
		} else {
			if (productDao.snExists(currentSn)) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Product> search(String keyword, Boolean isGift, Integer count) {
		return productDao.search(keyword, isGift, count);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("product")
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,
			String cacheRegion) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		return productDao.findList(productCategory, beginDate, endDate, first, count);
	}

	@Transactional(readOnly = true)
	public List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count) {
		return productDao.findSalesList(beginDate, endDate, count);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(Member member, Pageable pageable) {
		return productDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert) {
		return productDao.count(favoriteMember, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert);
	}

	@Transactional(readOnly = true)
	public boolean isPurchased(Member member, Product product) {
		return productDao.isPurchased(member, product);
	}

	public long viewHits(Long id) {
		Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
		Element element = cache.get(id);
		Long hits;
		if (element != null) {
			hits = (Long) element.getObjectValue();
		} else {
			Product product = productDao.find(id);
			if (product == null) {
				return 0L;
			}
			hits = product.getHits();
		}
		hits++;
		cache.put(new Element(id, hits));
		long time = System.currentTimeMillis();
		if (time > viewHitsTime + Product.HITS_CACHE_INTERVAL) {
			viewHitsTime = time;
			updateHits();
			cache.removeAll();
		}
		return hits;
	}

	public void destroy() throws Exception {
		updateHits();
	}

	@SuppressWarnings("unchecked")
	private void updateHits() {
		Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
		List<Long> ids = cache.getKeys();
		for (Long id : ids) {
			Product product = productDao.find(id);
			if (product != null) {
				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				Element element = cache.get(id);
				long hits = (Long) element.getObjectValue();
				long increment = hits - product.getHits();
				Calendar nowCalendar = Calendar.getInstance();
				Calendar weekHitsCalendar = DateUtils.toCalendar(product.getWeekHitsDate());
				Calendar monthHitsCalendar = DateUtils.toCalendar(product.getMonthHitsDate());
				if (nowCalendar.get(Calendar.YEAR) != weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
					product.setWeekHits(increment);
				} else {
					product.setWeekHits(product.getWeekHits() + increment);
				}
				if (nowCalendar.get(Calendar.YEAR) != monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
					product.setMonthHits(increment);
				} else {
					product.setMonthHits(product.getMonthHits() + increment);
				}
				product.setHits(hits);
				product.setWeekHitsDate(new Date());
				product.setMonthHitsDate(new Date());
				productDao.merge(product);
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void save(Product product) {
		Assert.notNull(product);

		super.save(product);
		productDao.flush();
		staticService.build(product);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Product update(Product product) {
		Assert.notNull(product);

		Product pProduct = super.update(product);
		productDao.flush();
		staticService.build(pProduct);
		return pProduct;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Product update(Product product, String... ignoreProperties) {
		return super.update(product, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Product product) {
		if (product != null) {
			staticService.delete(product);
		}
		super.delete(product);
	}

	@Override
	public List<Product> findFavoriteProduct(Member member) {
		return productDao.findFavoriteProduct(member);
	}

	@Override
	public List<Product> findList(Promotion promotion, Integer first, Integer count) {
		return productDao.findList(promotion, first, count);
	}

	@Override
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, first, count, filters, orders);
	}

	@Override
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable, Boolean isParent) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, pageable,isParent);
	}

	@Override
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count, Boolean isParent) {
		return productDao.findList(productCategory, beginDate, endDate, first, count,isParent);
	}

	@Override
	public List<Product> findList(Promotion promotion, Integer first, Integer count, Boolean isParent) {
		return productDao.findList(promotion, first, count,isParent);
	}

	@Override
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent){
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders,isParent);
	}
	
	@Override
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders,Boolean isParent){
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, first, count, filters, orders,isParent);
	}

	@Override
	public List<Product> findList(Member member) {
		return productDao.findList(member);
	}

	@Override
	public Page<Product> findPage(Pageable pageable, Shop shop,Boolean flag) {
		return productDao.findPage(pageable, shop,flag);
	}

}