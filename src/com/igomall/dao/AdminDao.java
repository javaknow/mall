/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.Admin;

public interface AdminDao extends BaseDao<Admin, Long> {

	boolean usernameExists(String username);

	Admin findByUsername(String username);

}