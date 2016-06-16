package com.igomall.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "lx_weChatMember")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_weChatMember_sequence")
public class WeChatMember extends BaseEntity {

	private static final long serialVersionUID = -7754320977877124698L;
	
	public static final String PRINCIPAL_ATTRIBUTE_NAME = WeChatMember.class + ".PRINCIPAL";
	
	public static final String OPEN_COOKIE_NAME = "openid";
	
	/*
	 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	 */
	private Integer subscribe;
	
	/*
	 * 用户的标识，对当前公众号唯一
	 */
	private String openid;
	
	/*
	 * 用户的昵称
	 */
	private String nickname;
	
	/*
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	private Integer sex;
	
	/*
	 * 用户的语言，简体中文为zh_CN
	 */
	private String language;
	
	/*
	 * 用户所在城市
	 */
	private String city;
	
	/*
	 * 用户所在省份
	 */
	private String province;
	
	/*
	 * 用户所在国家
	 */
	private String country;
	
	/*
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	 */
	private Long subscribe_time;
	
	/*
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	 */
	private String unionid;
	
	/*
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	 */
	private String remark;
	
	/*
	 * 用户所在的分组ID
	 */
	private Long groupid;
	
	/*
	 * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
	 */
	private String ticket;//推广码的ticket
	
	/*
	 * 
	 */
	private String qrCodeMediaId;//推广码在微信服务器上对应的mediaId
	
	private String qrCodeImgUrl;//自己推广的二维码图片路径

	/*
	 * 微信头像
	 */
	private String headimgurl="http://www.i-gomall.com/resources/wap/images/default_user_portrait.gif";//头像的地址 这个地址是微信上面的
	
	/*
	 * 对应的我们平台的会员账号
	 */
	private Member member;
	
	private String errcode="";
	
	private String errmsg="";
	
	
	@Transient
	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	@Transient
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getQrCodeMediaId() {
		return qrCodeMediaId;
	}

	public void setQrCodeMediaId(String qrCodeMediaId) {
		this.qrCodeMediaId = qrCodeMediaId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Long getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getGroupid() {
		return groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public String getQrCodeImgUrl() {
		return qrCodeImgUrl;
	}

	public void setQrCodeImgUrl(String qrCodeImgUrl) {
		this.qrCodeImgUrl = qrCodeImgUrl;
	}

	@Override
	public String toString() {
		return "WeChatMember [subscribe=" + subscribe + ", openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", language=" + language + ", city=" + city + ", province=" + province + ", country=" + country + ", headimgurl=" + headimgurl + ", subscribe_time=" + subscribe_time + ", unionid=" + unionid + ", remark=" + remark + ", groupid=" + groupid + "]";
	}

	public void copyProperties(com.igomall.wechat.entity.WeChatMember weChatMember) {
		setCity(weChatMember.getCity());
		setCountry(weChatMember.getCountry());
		setGroupid(weChatMember.getGroupid());
		setHeadimgurl(weChatMember.getHeadimgurl());
		if(getHeadimgurl()==null||"".equals(getHeadimgurl())){
			setHeadimgurl(headimgurl);
		}
		setLanguage(weChatMember.getLanguage());
System.out.println("weChatMember.getNickname():"+weChatMember.getNickname());
		setNickname(dealChars(weChatMember.getNickname()));
		setOpenid(weChatMember.getOpenid());
		setProvince(weChatMember.getProvince());
		setRemark(weChatMember.getRemark());
		setSex(weChatMember.getSex());
		setSubscribe(weChatMember.getSubscribe());
		setSubscribe_time(weChatMember.getSubscribe_time());
		setUnionid(weChatMember.getUnionid());
		
	}
	
	public String dealChars(String str){
		if(str==null){
			return "unknown";
		}
		String result = str;
		result = result.replaceAll("\\?", "-");
		
		return result;
	}

}
