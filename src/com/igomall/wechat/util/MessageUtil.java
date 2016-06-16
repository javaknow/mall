package com.igomall.wechat.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.igomall.wechat.entity.ImageMessage;
import com.igomall.wechat.entity.PicTextMessage;
import com.igomall.wechat.entity.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {
	
	  
  
    /** 
     * 解析微信发来的请求（XML） 
     *  
     * @param request 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("unchecked")  
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {  
        // 将解析结果存储在HashMap中  
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流  
        InputStream inputStream = request.getInputStream();
        try {
            // 读取输入流  
            SAXReader reader = new SAXReader();  
            Document document = reader.read(inputStream);  
            // 得到xml根元素  
            Element root = document.getRootElement();  
            // 得到根元素的所有子节点  
            List<Element> elementList = root.elements();  
            // 遍历所有子节点  
            for (Element e : elementList){
            	 map.put(e.getName(), e.getText());
System.out.println(e.getName()+":"+e.getText());
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 释放资源  
            inputStream.close();  
		}
        return map;  
    }
	
	  /** 
     * 文本消息对象转换成xml 
     *  
     * @param textMessage 文本消息对象 
     * @return xml 
     */  
    public static String textMessageToXml(TextMessage textMessage) {  
        xstream.alias("xml", textMessage.getClass()); 
       
        return xstream.toXML(textMessage);  
    }  
    
    /** 
     * 扩展xstream，使其支持CDATA块 
     *  
     */  
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
  
                @SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);
//                	super.startNode(name);  
                }  
  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });

	public static String picTextMessageToXml(PicTextMessage textMessage) {
		 xstream.alias("xml", textMessage.getClass()); 
	     return xstream.toXML(textMessage); 
	}

	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass()); 
        return xstream.toXML(imageMessage); 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public String getTextMsg1(String fromUserName,String toUserName, String content) {
		Setting setting = SettingUtils.get();
		String respContent="";
		String msg="";
		if(content==null||"".equals(content.trim())){
			respContent="输入内容有误。请回复1获取更多内容";
			msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		}else if("1".equals(content)){//获取帮助内容
			respContent = "10：绑定账号11:解绑账号。12：查看余额。13：查看积分。14：查看消费金额。15：查看业绩。16：查看奖金券。17：获取最新商品。";
			msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		}else if(content.startsWith("1")){//有效的信息
			if("10".equals(content)){//绑定账号
				respContent="请按如下格式输入：bd账户名";
				respContent="<a href=\""+setting.getSiteUrl()+"/wap/bund.jhtml?openId="+fromUserName+"\">点我绑定账号</a>";
				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("11".equals(content)){//解绑账号
				respContent="请按如下格式输入：jb账户名";
				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("12".equals(content)){//查看余额
				Member member = memberService.findByOpenId(fromUserName);
				if(member==null||!member.getBounded()){
					respContent="账户不存在，或账号未绑定该微信号";
				}else{
					respContent="您当前余额："+member.getBalance().setScale(2);
				}

				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("13".equals(content)){//查看积分
				Member member = memberService.findByOpenId(fromUserName);
				if(member==null||!member.getBounded()){
					respContent="账户不存在，或账号未绑定该微信号";
				}else{
					respContent="您当前积分："+member.getPoint();
				}

				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("14".equals(content)){//查看消费金额
				Member member = memberService.findByOpenId(fromUserName);
				if(member==null||!member.getBounded()){
					respContent="账户不存在，或账号未绑定该微信号";
				}else{
					respContent="您在我们商城消费金额为："+member.getAmount().setScale(2);
				}
				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("15".equals(content)){//查看业绩
				Member member = memberService.findByOpenId(fromUserName);
				if(member==null||!member.getBounded()){
					respContent="账户不存在，或账号未绑定该微信号";
				}else{
					List<Member> children = memberService.findChildren(member);
					respContent="您当前已推荐："+children.size();
				}
				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("16".equals(content)){//查看奖金券
				Member member = memberService.findByOpenId(fromUserName);
				if(member==null||!member.getBounded()){
					respContent="账户不存在，或账号未绑定该微信号";
				}else{
					respContent="您当前奖金券金额："+member.getBalance1().setScale(2);
				}
				msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			}else if("17".equals(content)){//查看最新商品（最近上传的10个产品）
				List<Goods> goods = goodsService.findList(5, null, null);
				msg = MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
			}
		}else if(content.startsWith("bd")){//绑定账号
			String username = content.substring(2);
			Member member = memberService.findByUsername(username);
			Member member1 = memberService.findByOpenId(fromUserName);
			if(member1!=member){//该微信号绑定了其他账号
				respContent="该微信号已经绑定了其他账号，本次绑定失败。";
			}else{
				if(member!=null){
					if(member.getOpenId()==null){
						member.setOpenId(fromUserName);
						member.setBounded(true);
						memberService.update(member);
						respContent="用户："+username+"绑定成功。";
					}else if(member.getOpenId().equals(fromUserName)){//已经绑定了的
						member.setBounded(true);
						memberService.update(member);
						respContent="用户："+username+"绑定成功。";
					}else{//已经绑定了 但是不是当前的微信号
						respContent="用户："+username+"已经绑定了其他微信好，如需更换微信号请联系商城客服。";
					}
				}else{
					respContent="用户："+username+"不存在，绑定失败。";
				}
			}

			msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		}else if(content.startsWith("jb")){//解绑账号
			String username = content.substring(2);
			Member member = memberService.findByUsername(username);
			if(member!=null){
				if(member.getOpenId()==null){
					
					respContent="用户："+username+"没有绑定微信，解绑失败。";
				}else if(member.getOpenId().equals(fromUserName)){//已经绑定了的
					member.setBounded(false);
					memberService.update(member);
					respContent="用户："+username+"解绑成功。";
				}else{//已经绑定了 但是不是当前的微信号
					respContent="用户："+username+"已经绑定了其他微信好，解绑失败。";
				}
			}else{
				respContent="用户："+username+"不存在，解绑失败。";
			}
			msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		}else{
			respContent="输入内容有误。请回复1获取更多内容";
			msg = MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		}
		
		return msg;
	}*/

	
}
