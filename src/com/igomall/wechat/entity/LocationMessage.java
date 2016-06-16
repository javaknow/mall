package com.igomall.wechat.entity;

public class LocationMessage extends BaseMessage {
	
	private static final long serialVersionUID = -9174158358040606433L;

	//地理位置纬度
	private Double Location_X;
	
	//地理位置经度
	private Double Location_Y;
	
	//地图缩放大小
	private Double Scale;
	
	//地理位置信息
	private String Label;

	public Double getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(Double location_X) {
		Location_X = location_X;
	}

	public Double getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(Double location_Y) {
		Location_Y = location_Y;
	}

	public Double getScale() {
		return Scale;
	}

	public void setScale(Double scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}
}
