package com.igomall.wechat.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.igomall.entity.OrderEntity;

/**
 * 微信菜单
 * 
 * @author blackboy1987
 * 
 */
public class WeChatMenu extends OrderEntity {
	private static final long serialVersionUID = -8936850209290144742L;

	public enum Type {
		/*
		 * 点击推事件。 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event
		 * 的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互。
		 */
		CLICK,
		/*
		 * 跳转URL。
		 * 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
		 */
		VIEW,
		/*
		 * ：扫码推事件。
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者
		 * ，开发者可以下发消息。
		 */
		SCANCODE_PUSH,
		/*
		 * 扫码推事件。且弹出“消息接收中”提示框
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具
		 * ，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息
		 */
		SCANCODE_WAITMSG,
		/*
		 * 弹出系统拍照发图。
		 * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机
		 * ，随后可能会收到开发者下发的消息。
		 */
		PIC_SYSPHOTO,
		/*
		 * 弹出拍照或者相册发图 。用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
		 */
		PIC_PHOTO_OR_ALBUM,
		/*
		 * 弹出微信相册发图器。
		 * 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册
		 * ，随后可能会收到开发者下发的消息。
		 */
		PIC_WEIXIN,
		/*
		 * 弹出地理位置选择器。
		 * 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具
		 * ，随后可能会收到开发者下发的消息。
		 */
		LOCATION_SELECT,
		/*
		 * 下发消息（除文本消息）。
		 * 用户点击media_id类型按钮后，微信服务器会将开发者填写的永久素材id对应的素材下发给用户，永久素材类型可以是图片
		 * 、音频、视频、图文消息。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		MEDIA_ID,
		/*
		 * 跳转图文消息URL。
		 * 用户点击view_limited类型按钮后，微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，永久素材类型只支持图文消息
		 * 。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		VIEW_LIMITED
	}
	
	private Type type;
	
	private String name;
	
	private String key;
	
	private String url;
	
	private Set<WeChatMenu> children = new HashSet<WeChatMenu>();
	
	private WeChatMenu parent;
	
	private String media_id;
	
	private String treePath;

	private Integer grade;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<WeChatMenu> getChildren() {
		return children;
	}

	public void setChildren(Set<WeChatMenu> children) {
		this.children = children;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public WeChatMenu getParent() {
		return parent;
	}

	public void setParent(WeChatMenu parent) {
		this.parent = parent;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	

}
