package com.igomall.wechat.entity;

/**
 * 微信消息之视频消息
 * @author blackboy2015
 *
 */
public class VideoMessage extends BaseMessage {

	private static final long serialVersionUID = 3535202078448584923L;

	//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String MediaId;
	
	//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String ThumbMediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	
}
