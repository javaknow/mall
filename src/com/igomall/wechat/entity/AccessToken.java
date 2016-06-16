package com.igomall.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.igomall.entity.BaseEntity;

/**
 * 微信通用接口凭证
 * 
 * @author zhanglei
 */
@Entity
@Table(name = "lx_access_token")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_access_token_sequence")
public class AccessToken extends BaseEntity {
	private static final long serialVersionUID = -5136527456864223995L;
	// 获取到的凭证
	private String access_token;
	
	// 凭证有效时间，单位：s
	private int expires_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", expires_in="
				+ expires_in + "]";
	}

}