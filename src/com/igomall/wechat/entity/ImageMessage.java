package com.igomall.wechat.entity;

/**
 * 微信消息之图片消息
 * @author blackboy2015
 *
 */
public class ImageMessage extends BaseMessage {

	private static final long serialVersionUID = 2016653786257150388L;
    
	// 图片链接（由系统生成）
    private String PicUrl;
    
    //图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String MediaId;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
}
