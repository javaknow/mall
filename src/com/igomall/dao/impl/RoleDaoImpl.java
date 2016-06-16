/*
 * 
 * 
 * 
 */
package com.igomall.dao.impl;

import com.igomall.dao.RoleDao;
import com.igomall.entity.Role;

import org.springframework.stereotype.Repository;

@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

}