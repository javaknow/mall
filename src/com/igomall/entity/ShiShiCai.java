package com.igomall.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "lx_shishicai")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_shishicai_sequence")
public class ShiShiCai extends BaseEntity {
	private static final long serialVersionUID = 5875389487981216011L;

	public enum Type{
		shishicai,
		baileshishicai,
		
	}
	
	private Type type;
	
	private String number;
	
	private Integer ge;
	
	private Integer shi;
	
	private Integer bai;
	
	private Integer qian;
	
	private Integer wan;
	
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getGe() {
		return ge;
	}

	public void setGe(Integer ge) {
		this.ge = ge;
	}

	public Integer getShi() {
		return shi;
	}

	public void setShi(Integer shi) {
		this.shi = shi;
	}

	public Integer getBai() {
		return bai;
	}

	public void setBai(Integer bai) {
		this.bai = bai;
	}

	public Integer getQian() {
		return qian;
	}

	public void setQian(Integer qian) {
		this.qian = qian;
	}

	public Integer getWan() {
		return wan;
	}

	public void setWan(Integer wan) {
		this.wan = wan;
	}
	
	
}
