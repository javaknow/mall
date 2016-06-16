package com.igomall.wechat.entity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.igomall.entity.BaseEntity;
import com.igomall.listener.EntityListener;
import com.igomall.wechat.CommonWeChatAttributes.MsgType;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
@EntityListeners(EntityListener.class)
@MappedSuperclass
public class BaseMessage extends BaseEntity{
	
	private static final long serialVersionUID = 7721463892163165646L;
	
	// 开发者微信号
    private String ToUserName;  
    // 发送方帐号（一个OpenID）
    private String FromUserName;  
    // 消息创建时间 （整型）
    private long CreateTime;  
    // 消息类型
    private MsgType MsgType;  
    
    //消息id，64位整型
    private String MsgId;
    
    private String encrypt;
  
    public String getToUserName() {  
        return ToUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        ToUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return FromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
        FromUserName = fromUserName;  
    }  
  
    public long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(long createTime) {  
        CreateTime = createTime;  
    }  
  
    public MsgType getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(MsgType msgType) {  
        MsgType = msgType;  
    }

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
}
