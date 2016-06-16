package com.igomall.wechat.entity;

/**
 * 微信消息之连接消息
 * @author blackboy2015
 *
 */
public class LinkMessage extends BaseMessage {

	private static final long serialVersionUID = 383867470033766596L;

	//消息标题
	private String Title;

	//消息描述
	private String Description;

	//消息链接
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
	
	
}
