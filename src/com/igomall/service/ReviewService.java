/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Member;
import com.igomall.entity.Product;
import com.igomall.entity.Review;
import com.igomall.entity.Review.Type;

public interface ReviewService extends BaseService<Review, Long> {

	List<Review> findList(Member member, Product product, Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	List<Review> findList(Member member, Product product, Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	Page<Review> findPage(Member member, Product product, Type type, Boolean isShow, Pageable pageable);

	Long count(Member member, Product product, Type type, Boolean isShow);

	boolean isReviewed(Member member, Product product);

}