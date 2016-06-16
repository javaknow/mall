
package com.igomall.service;

import com.igomall.entity.AdminIp;

public interface AdminIpService extends BaseService<AdminIp, Long> {

	boolean ipExists(String ip);

	AdminIp findByIp(String ip);

}