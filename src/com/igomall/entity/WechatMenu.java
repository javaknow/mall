package com.igomall.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 微信菜单
 * @author blackboy2015
 *
 */
@Entity
@Table(name="lx_wechat_menu")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_wechat_menu_sequence")
public class WechatMenu extends OrderEntity {
	
	public enum Type {
		/*
		 * 点击事件
		 * 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	
		 * 的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，
		 * 开发者可以通过自定义的key值与用户进行交互；
		 */
		click,
		
		/**
		 * 跳转URL 
		 * 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，
		 * 可与网页授权获取用户基本信息接口结合，获得用户基本信息。
		 */
		view,
		
		/**
		 * 扫码推事件 
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），
		 * 且会将扫码的结果传给开发者，开发者可以下发消息。
		 */
		scancode_push,
		
		/**
		 * 扫码推事件且弹出“消息接收中”提示框 
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，
		 * 完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，
		 * 然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
		 */
		scancode_waitmsg,
		
		/**
		 * 弹出系统拍照发图 
		 * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，
		 * 会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，
		 * 随后可能会收到开发者下发的消息。
		 */
		pic_sysphoto,
		
		/**
		 * 弹出拍照或者相册发图 
		 * 用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者
		 * “从手机相册选择”。用户选择后即走其他两种流程。
		 */
		pic_photo_or_album,
		
		/**
		 * 弹出微信相册发图器 
		 * 用户点击按钮后，微信客户端将调起微信相册，
		 * 完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，
		 * 同时收起相册，随后可能会收到开发者下发的消息。
		 */
		pic_weixin,
		
		/**
		 * 弹出地理位置选择器 
		 * 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，
		 * 将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，
		 * 随后可能会收到开发者下发的消息。
		 */
		location_select,
		
		/**
		 * 下发消息（除文本消息） 
		 * 用户点击media_id类型按钮后，
		 * 微信服务器会将开发者填写的永久素材id对应的素材下发给用户，
		 * 永久素材类型可以是图片、音频、视频、图文消息。
		 * 请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		media_id,
		
		/**
		 * 跳转图文消息URL 
		 * 用户点击view_limited类型按钮后，
		 * 微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，
		 * 永久素材类型只支持图文消息。
		 * 请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		view_limited,
	}
	
    private static final long serialVersionUID = -8884421813738583303L;

	public static final String TREE_PATH_SEPARATOR = ",";
	
	 private Type type;//菜单类型 
	 
     private String name ;//菜单名称
     
     private String key;//click等点击类型必须.菜单KEY值，用于消息接口推送，不超过128字节
     
     private Set<WechatMenu> children = new HashSet<WechatMenu>();//子菜单
     
     private WechatMenu parent;//上一级菜单
     
     private String url;//view类型必须.网页链接，用户点击菜单可打开链接，不超过1024字节
     
     private String media_id;//media_id类型和view_limited类型必须.调用新增永久素材接口返回的合法media_id
     
     private Boolean isEnabled;//启动和禁用  true:启用 false：禁用 
     
     private String treePath;

 	private Integer grade;

 	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="keyss")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<WechatMenu> getChildren() {
		return children;
	}

	public void setChildren(Set<WechatMenu> children) {
		this.children = children;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public WechatMenu getParent() {
		return parent;
	}

	public void setParent(WechatMenu parent) {
		this.parent = parent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@Column(nullable = false)
	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@Column(nullable = false)
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Transient
	public List<Long> getTreePaths() {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				treePaths.add(Long.valueOf(id));
			}
		}
		return treePaths;
	}

	@Transient
	public Set<WechatMenu> getChildrens() {
		Set<WechatMenu> wechatMenus = getChildren();
		Set<WechatMenu> children = new HashSet<WechatMenu>();
		for (WechatMenu wechatMenu : wechatMenus) {
			if(wechatMenu.getIsEnabled()){
				children.add(wechatMenu);
			}
		}
		return children;
	}
}
