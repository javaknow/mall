package com.igomall.wechat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.igomall.Setting;
import com.igomall.entity.Goods;
import com.igomall.entity.Product;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.CommonWeChatAttributes.MsgType;
import com.igomall.wechat.entity.ImageMessage;
import com.igomall.wechat.entity.TextMessage;
import com.igomall.wechat.util.MessageUtil;

/**
 * 
 * 
 * @author jiangyin
 */
public class MessageResponse {
	
	/**
	 * 回复文本消息
	 * @param fromUserName
	 * @param toUserName
	 * @param respContent
	 * @return
	 */
	public static String getTextMessage(String fromUserName , String toUserName , String respContent) {
		
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MsgType.TEXT);
		textMessage.setContent(respContent);
		return MessageUtil.textMessageToXml(textMessage);
	}
	
	/**
	 * 回复图片消息
	 * @param fromUserName
	 * @param toUserName
	 * @param respContent
	 * @return
	 */
	public static String getImageMessage(String fromUserName , String toUserName , String mediaId) {
		
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(fromUserName);
		imageMessage.setFromUserName(toUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setMsgType(MsgType.IMAGE);
		imageMessage.setMediaId(mediaId);
		return MessageUtil.imageMessageToXml(imageMessage);
	}

	public static String getPicTextMessage(String fromUserName, String toUserName, List<Goods> products) {
		
		String xml="<xml>"+
						"<ToUserName><![CDATA[toUser]]></ToUserName>"+
						"<FromUserName><![CDATA[fromUser]]></FromUserName>"+
						"<CreateTime>createTime</CreateTime>"+
						"<MsgType><![CDATA[news]]></MsgType>"+
						"<ArticleCount>count</ArticleCount>"+
						"<Articles>";
		
		String item= "<item>"+
						"<Title><![CDATA[title]]></Title> "+
						"<Description><![CDATA[description]]></Description>"+
						"<PicUrl><![CDATA[picur]]></PicUrl>"+
						"<Url><![CDATA[url]]></Url>"+
					"</item>";
		
		String item1="";
		
		String result ="";
		
		String xml1="</Articles>"+
					"</xml> ";
		if(products==null||products.size()==0){
			return getTextMessage(fromUserName,toUserName,"暂无产品");
		}else{
			xml  = xml.replace("toUser", fromUserName);
			xml = xml.replace("fromUser", toUserName);
			xml = xml.replace("createTime", new Date().getTime()+"");
			xml = xml.replace("count", products.size()+"");
			Setting setting = SettingUtils.get();
			for (Goods goods : products) {
				Iterator<Product> iterator = goods.getProducts().iterator();
				if (iterator.hasNext()) {
					Product product = iterator.next();
item1 = item.replace("title", product.getName());
item1 = item1.replace("description", product.getFullName());
item1 = item1.replace("picur", product.getImage());
item1 = item1.replace("url",setting.getSiteUrl() + product.getPath());
					result = result + item1;
				}
			}
			return xml+result+xml1;
		}
	}
}