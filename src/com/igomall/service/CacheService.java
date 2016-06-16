/*
 * 
 * 
 * 
 */
package com.igomall.service;

public interface CacheService {

	String getDiskStorePath();

	int getCacheSize();

	void clear();

}