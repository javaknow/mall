package com.igomall.wechat.entity;


public class TotalMember extends BaseEntity {

	private static final long serialVersionUID = -7754320977877124698L;
	
	//{"total":2,"count":2,"data":{"openid":["","OPENID1","OPENID2"]},"next_openid":"NEXT_OPENID"}
	private Integer total;
	private String count;
	private Data data;
	private String next_openid;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getNext_openid() {
		return next_openid;
	}
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
	@Override
	public String toString() {
		return "TotalMember [total=" + total + ", count=" + count + ", data=" + data + ", next_openid=" + next_openid + "]";
	}

	
	
}
