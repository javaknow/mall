/*
 * 
 * 
 * 
 */
package com.igomall.service;

import java.util.List;

import com.igomall.entity.Admin;

public interface AdminService extends BaseService<Admin, Long> {

	boolean usernameExists(String username);

	Admin findByUsername(String username);

	List<String> findAuthorities(Long id);

	boolean isAuthenticated();

	Admin getCurrent();

	String getCurrentUsername();

}