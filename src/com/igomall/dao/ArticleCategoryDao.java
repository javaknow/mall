/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.util.List;

import com.igomall.entity.ArticleCategory;

public interface ArticleCategoryDao extends BaseDao<ArticleCategory, Long> {

	List<ArticleCategory> findRoots(Integer count);

	List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count);

	List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count);

}