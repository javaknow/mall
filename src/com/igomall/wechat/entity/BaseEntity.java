package com.igomall.wechat.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 74229134646636058L;

	private String errcode="0";
	private String errmsg="";
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	@Override
	public String toString() {
		return "BaseEntity [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}
	
	
}
