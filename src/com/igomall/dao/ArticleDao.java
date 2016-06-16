/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.Date;
import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.entity.Article;
import com.igomall.entity.ArticleCategory;
import com.igomall.entity.Tag;

public interface ArticleDao extends BaseDao<Article, Long> {

	List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders);

	List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count);

	Page<Article> findPage(ArticleCategory articleCategory, List<Tag> tags, Pageable pageable);

}