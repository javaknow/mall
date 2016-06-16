package com.igomall.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 权限控制
 * 
 * @author 黎
 * 
 */
@Entity
@Table(name = "lx_roleControl")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_roleControl_sequence")
public class RoleControl extends OrderEntity {
	private static final long serialVersionUID = -1696088199491250183L;
	public String name; // 名称
	public String method; // 对应的方法(这里对应的是页面的上的方法，方便看到这个权限控制之后知道控制的是哪个方法)
	@ManyToOne
	public Menu menu;// 菜单
	
	public String roleControlName;//用来控制权限的名称
	
	private Boolean isEnabled;//启动和禁用  true:启用 false：禁用 

	
	
	public String getRoleControlName() {
		return roleControlName;
	}

	public void setRoleControlName(String roleControlName) {
		this.roleControlName = roleControlName;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}