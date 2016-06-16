
package com.igomall.service;

import java.util.Map;

import com.igomall.entity.Article;
import com.igomall.entity.Product;

public interface StaticService {

	int build(String templatePath, String staticPath, Map<String, Object> model);

	int build(String templatePath, String staticPath);

	int build(Article article);

	int build(Product product);

	int buildIndex();

	int buildSitemap();

	int buildOther();

	int buildAll();

	int delete(String staticPath);

	int delete(Article article);

	int delete(Product product);

	int deleteIndex();

	int deleteOther();

}