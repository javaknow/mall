/*
 * 
 * 
 * 
 */
package com.igomall.service;

import com.igomall.entity.Log;

public interface LogService extends BaseService<Log, Long> {

	void clear();

}