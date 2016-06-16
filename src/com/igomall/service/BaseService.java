
package com.igomall.service;

import java.io.Serializable;
import java.util.List;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;

public interface BaseService<T, ID extends Serializable> {

	T find(ID id);

	List<T> findAll();

	@SuppressWarnings("unchecked")
	List<T> findList(ID... ids);

	List<T> findList(Integer count, List<Filter> filters, List<Order> orders);

	List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<T> findPage(Pageable pageable);

	long count();

	long count(Filter... filters);

	boolean exists(ID id);

	boolean exists(Filter... filters);

	void save(T entity);

	T update(T entity);

	T update(T entity, String... ignoreProperties);

	void delete(ID id);

	@SuppressWarnings("unchecked")
	void delete(ID... ids);

	void delete(T entity);
	
	/*************************************下面三个方法采用jdbc的查询***************************************************/
	List<T> findListJdbc(String sql);

	Page<T> findPageJdbc(String sql,String sql1,Pageable pageable);


}