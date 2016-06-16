package com.igomall.wechat.entity;

public class WechatMedia extends BaseEntity{

	private static final long serialVersionUID = 367223254804892089L;

	private String type="";
	
	private String media_id="";
	
	private String created_at="";
	
	private String path="";
	
	private String fileName="";
	
	private String url="";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "WechatMedia [type=" + type + ", media_id=" + media_id
				+ ", created_at=" + created_at + ", path=" + path
				+ ", fileName=" + fileName + "]";
	}
	
	
	
}
