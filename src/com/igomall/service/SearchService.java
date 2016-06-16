
package com.igomall.service;

import java.math.BigDecimal;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Article;
import com.igomall.entity.Product;
import com.igomall.entity.Product.OrderType;
import com.igomall.entity.ProductCategory;
import com.igomall.entity.Shop;

public interface SearchService {

	void index();

	void index(Class<?> type);

	void index(Article article);

	void index(Product product);

	void purge();

	void purge(Class<?> type);

	void purge(Article article);

	void purge(Product product);

	Page<Article> search(String keyword, Pageable pageable);
	
	Page<Product> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable);

	Page<Product> search1(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable);

	Page<Product> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable, ProductCategory productCategory);

	Page<Product> search(Shop shop, String keywords, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable);

}