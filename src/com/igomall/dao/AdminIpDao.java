
package com.igomall.dao;

import com.igomall.entity.AdminIp;

public interface AdminIpDao extends BaseDao<AdminIp, Long> {

	boolean ipExists(String ip);

	AdminIp findByIp(String ip);

}