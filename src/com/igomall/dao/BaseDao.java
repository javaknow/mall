/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;

public interface BaseDao<T, ID extends Serializable> {

	T find(ID id);

	T find(ID id, LockModeType lockModeType);

	List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<T> findPage(Pageable pageable);

	long count(Filter... filters);

	void persist(T entity);

	T merge(T entity);

	void remove(T entity);

	void refresh(T entity);

	void refresh(T entity, LockModeType lockModeType);

	ID getIdentifier(T entity);

	boolean isManaged(T entity);

	void detach(T entity);

	void lock(T entity, LockModeType lockModeType);

	void clear();

	void flush();
	
	/*************************************下面三个方法采用jdbc的查询***************************************************/
	/**
	 * 采用jdb的方式查询列表
	 * @param sql
	 * @return
	 */
	List<T> findListJdbc(String sql);

	/**
	 * 采用jdbc的方式进行分页查找
	 * @param sql 查询语句
	 * @param sql1 统计数量的查询语句
	 * @param pageable 分页对象
	 * @return
	 */
	Page<T> findPageJdbc(String sql,String sql1,Pageable pageable);


}