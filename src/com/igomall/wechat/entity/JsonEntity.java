package com.igomall.wechat.entity;

public class JsonEntity {

	private Long id;
	
	private String name;
	
	private Boolean isParent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public JsonEntity(Long id, String name, Boolean isParent) {
		super();
		this.id = id;
		this.name = name;
		this.isParent = isParent;
	}

	public JsonEntity() {

	}
	
	
	
	
	
}
