
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
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "lx_menu")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_menu_sequence")
public class Menu extends OrderEntity {
	
	private static final long serialVersionUID = -1969124600328476736L;

	public static final String TREE_PATH_SEPARATOR = ",";

	private String name;//菜单名称

	private String url;//菜单路径

	private Boolean isEnabled;//启动和禁用  true:启用 false：禁用 

	private String target;//打开方式
	
	private String treePath;

	private Integer grade;

	private Menu parent;//上一级菜单

	private Set<Menu> children = new HashSet<Menu>();//子菜单
	
	private String roleName;//权限的名字
	
	private Set<RoleControl> roleControls = new HashSet<RoleControl>();//子菜单
	
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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

	@Length(max = 200)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Length(max = 200)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	@ManyToOne(fetch = FetchType.LAZY)
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}
	
	
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	public Set<RoleControl> getRoleControls() {
		return roleControls;
	}

	public void setRoleControls(Set<RoleControl> roleControls) {
		this.roleControls = roleControls;
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

	@PreRemove
	public void preRemove() {
		
	}
	
	@Transient
	public List<String> getChildrenRoleNames() {
		//["admin:setting", "admin:area", "admin:paymentMethod", "admin:shippingMethod", "admin:deliveryCorp", "admin:paymentPlugin", "admin:storagePlugin", "admin:admin", "admin:role", "admin:message", "admin:log"]
		List<String> childrenRoleNames=new ArrayList<String>();
		for(Menu child : getChildren()){
			childrenRoleNames.add(child.getRoleName());
			for (RoleControl roleControl : child.getRoleControls()) {
				childrenRoleNames.add(roleControl.getRoleControlName());
			}
			childrenRoleNames.addAll(child.getRoleControlNames());
		}	
		return childrenRoleNames;
	}
	
	@Transient
	public List<String> getRoleControlNames() {
		List<String> roleControlNames=new ArrayList<String>();
		for (RoleControl roleControl : getRoleControls()) {
			roleControlNames.add(roleControl.getRoleControlName());
		}
		return roleControlNames;
	}

}