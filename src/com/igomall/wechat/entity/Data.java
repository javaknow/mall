package com.igomall.wechat.entity;

import java.util.List;

public class Data {
	private List<String> openid;

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return "Data [openid=" + openid + "]";
	}
	
	
}
