
package com.igomall.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 后台允许登陆的ip
 * @author blackboy2015
 *
 */
@Entity
@Table(name = "lx_admin_ip")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_admin_ip_sequence")
public class AdminIp extends OrderEntity {

	private static final long serialVersionUID = -6109590619136943215L;

	private String ip;//登陆的ip

	private String hostName;//主机名称

	private String username;//允许登陆的用户名。这里是指admin账号
	
	private Boolean isEnabled;//

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	
}