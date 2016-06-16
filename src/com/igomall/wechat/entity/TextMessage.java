package com.igomall.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 微信消息之文本消息
 * @author blackboy2015
 *
 */
@Entity
@Table(name = "lx_text_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "lx_text_message_sequence")
public class TextMessage extends BaseMessage {

	private static final long serialVersionUID = 2016653786257150388L;
	// 消息内容
    private String Content;  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }   
}
