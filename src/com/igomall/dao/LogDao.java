/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.Log;

public interface LogDao extends BaseDao<Log, Long> {

	void removeAll();

}