package com.igomall.wechat.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igomall.Setting;
import com.igomall.controller.wap.BaseController;
import com.igomall.entity.Goods;
import com.igomall.entity.Member;
import com.igomall.entity.Member.Gender;
import com.igomall.entity.WeChatMember;
import com.igomall.service.ArticleService;
import com.igomall.service.GoodsService;
import com.igomall.service.MemberRankService;
import com.igomall.service.MemberService;
import com.igomall.service.ProductService;
import com.igomall.service.WeChatMemberService;
import com.igomall.util.SHA1;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.WeChatSign;
import com.igomall.wechat.CommonWeChatAttributes.EventType;
import com.igomall.wechat.CommonWeChatAttributes.MsgType;
import com.igomall.wechat.MessageResponse;
import com.igomall.wechat.entity.Ticket;
import com.igomall.wechat.entity.WechatMedia;
import com.igomall.wechat.util.GetAccessTokenUtil;
import com.igomall.wechat.util.MessageUtil;
import com.igomall.wechat.util.QRCodeUtils;

/**
 * 微信的入口。注意这里只用来判断接口跟微信是不是通的。其他业务逻辑均不再这里执行。
 * @author blackboy
 *
 */
@Controller("wechatController")
@RequestMapping("/wechat")
public class WeChatController extends BaseController {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	
	@Resource(name = "weChatMemberServiceImpl")
	private WeChatMemberService weChatMemberService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	/**
	 * 检测网站是否跟微信服务器对接成功。
	 * 如果成功了，返回echostr。否则返回其他字符串
	 * @param request
	 * @param response
	 * @param weChatSign 参数组装成微信的签名对象
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String checkValidate(HttpServletRequest request, HttpServletResponse response, WeChatSign weChatSign) throws IOException {
System.out.println(weChatSign);
		if (SHA1.checkSignature(weChatSign)) {
			//请求成功之后，把echostr原样返回
			return weChatSign.getEchostr();
		}
		//请求失败，返回给服务器的字符串
		return "error";
	}

	/**
	 * 处理微信的POST请求
	 * @param request
	 * @param response
	 * @param weChat
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public void message(HttpServletRequest request,HttpServletResponse response, WeChatSign weChatSign) throws IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        String respMessage = processRequest(request,response);      
		response.getWriter().write(respMessage);
	}

	/**
	 * 处理消息
	 * @param request
	 * @param response
	 * @return
	 */
	public String processRequest(HttpServletRequest request, HttpServletResponse response) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			// 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			//获取消息类型
			MsgType msgType = null;
			if(requestMap.get("MsgType")!=null){
				msgType = MsgType.valueOf(requestMap.get("MsgType").toUpperCase());
			}
			
			//获取事件类型
			EventType eventType = null;
			if(requestMap.get("Event")!=null){
				eventType = EventType.valueOf(requestMap.get("Event").toUpperCase());
			}
			
			
			// 从HashMap中取出消息中的字段；
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			
			//针对于扫码注册的用户
			String eventKey = requestMap.get("EventKey");//获取eventKey的值。里面包含了推荐人的id
			// 公众帐号(商城的账号)
			String toUserName = requestMap.get("ToUserName");
			
			respContent="欢迎关注爱购网络商城微信号！欢迎加入商城官方群：432887669，获取最新信息。";
			
			if(msgType==MsgType.EVENT){//如果是event事件。进行下面的操作的
				if(eventType==EventType.SUBSCRIBE){//关注事件
					createInfo(fromUserName, eventKey);
					respContent = "欢迎关注爱购商城微信公众号";
				}else if(eventType==EventType.UNSUBSCRIBE){//取消关注事件
					respContent = "取消关注成功！";
				}else if(eventType==EventType.SCAN){//扫码事件
					respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
				}else if(eventType==EventType.LOCATION){//上报地理位置事件
					respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
				}else if(eventType==EventType.VIEW){//点击菜单跳转链接时的事件推送
					respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
				}else if(eventType==EventType.CLICK){//点击菜单拉取消息时的事件推送
					return getClickMsg(fromUserName,toUserName,eventKey);
				}
			}else if (msgType.equals(MsgType.TEXT)) {//文本消息
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.IMAGE)) {//图片
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.VOICE)) {//语音
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.VIDEO)) {//视频
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.SHORTVIDEO)) {//短视频
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.LOCATION)) {//位置
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}else if (msgType.equals(MsgType.LINK)) {//链接
				respContent = "欢迎关注爱购商城微信公众号！欢迎加入商城官方群：432887669，获取最新信息。";
			}
			return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

	/**
	 * 创建或更新对应微信账号的资料
	 * @param fromUserName 客户的微信号
	 * @param eventKey eventKey 事件类型。如果是扫码事件，这个是有数据的。可以根据这个来获取到推荐人的id
	 * @return
	 */
	private Member createInfo(String fromUserName, String eventKey) {
		//查找这个公众号对应的账户。如果没有找到需要创建一个
		WeChatMember weChatMember = weChatMemberService.findByOpenId(fromUserName);
		Member member = null;
		
		if(weChatMember==null){//当前进来的微信号没有在我们商城绑定账号，那么就自动给他生成一个账号
			//从eventKey里面获取到推荐人
			Member parent = getParent(eventKey);
			weChatMember = weChatMemberService.create(fromUserName,parent);
		}else{//如果存在更新资料(只要昵称和头像)
			weChatMember = weChatMemberService.update(weChatMember);
		}
		member = weChatMember.getMember();
		return member;
	}

	public Member createMember(WeChatMember weChatMember,Member parent) {
		
		Member member = memberService.findByOpenId(weChatMember.getOpenid());
		if(member==null){
			 member = new Member();
		}
		Setting setting = SettingUtils.get();
		member.setNumber(weChatMember.getOpenid());
		member.setPassword(DigestUtils.md5Hex(setting.getDefaultUserPassword()));
		member.setMemberRank(memberRankService.findDefault());
		member.setUsername(member.getNumber());
		member.setEmail("igmall@qq.com");
		member.setType(2);
		member.setParent(parent);
		if(weChatMember.getSex()!=null){
			member.setGender(Gender.values()[weChatMember.getSex()]);
		}else{
			member.setGender(Gender.unknown);
		}
		memberService.save(member);
		
		return member;
	}

	/**
	 * 从eventKey里面获取到推荐人的id，如果有的话。
	 * @param eventKey
	 * @return
	 */
	public Member getParent(String eventKey) {
		String[] keys = eventKey.split("_");
		Member parent = null;
		if(keys!=null&&keys.length==2){
			Long memberId = Long.valueOf(keys[1]);
			parent = memberService.find(memberId);
			
		}
		return parent;
	}

	/**
	 * 按钮是点击事件
	 * @param fromUserName
	 * @param toUserName
	 * @param eventKey
	 * @return
	 * @throws Exception
	 */
	public String getClickMsg(String fromUserName,String toUserName, String eventKey) throws Exception {
		if("getQrcode_34".equals(eventKey)){//获取自己的推广二维码
			WeChatMember weChatMember = weChatMemberService.findByOpenId(fromUserName);
			if(weChatMember==null){
				Member parent = memberService.find(337L);
				weChatMember = weChatMemberService.create(fromUserName,parent);
			}
			
			Ticket ticket = GetAccessTokenUtil.createQrcode(GetAccessTokenUtil.getAccessToken(), weChatMember.getMember().getId());
			
			if("0".equals(ticket.getErrcode())){
				weChatMember.setTicket(ticket.getTicket());
				String path = QRCodeUtils.get2CodeImage(ticket.getUrl(), CommonWeChatAttributes.IMAGE_PATH+weChatMember.getId()+".jpg", CommonWeChatAttributes.logoPath,960,960);
				//String path = ZXingCode.createQRCode(ticket.getUrl(), CommonWeChatAttributes.IMAGE_PATH+ticket.getTicket()+".jpg", CommonWeChatAttributes.logoPath);
				WechatMedia wechatMedia = GetAccessTokenUtil.uploadFile(path, "image");
				if("0".equals(wechatMedia.getErrcode())){
					weChatMember.setQrCodeMediaId(wechatMedia.getMedia_id());
					weChatMember.setQrCodeImgUrl(path);
					weChatMemberService.update(weChatMember);
					return MessageResponse.getImageMessage(fromUserName, toUserName, weChatMember.getQrCodeMediaId());
				}else{
					return MessageResponse.getTextMessage(fromUserName , toUserName , "获取推广码失败！请尝试重新获取，如多次获取失败请联系我们的客服。客服微信号：2429732001。欢迎加入商城官方群：432887669，获取最新信息。");
				}
			}else{
				return MessageResponse.getTextMessage(fromUserName , toUserName , "获取推广码失败！请尝试重新获取，如多次获取失败请联系我们的客服。客服微信号：2429732001。欢迎加入商城官方群：432887669，获取最新信息。");
			}
		}else if("hotProduct_21".equals(eventKey)){//热门产品
			List<Goods> goods = goodsService.findList(10, null, null,0);
			return MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
		}else if("newProduct_22".equals(eventKey)){//最新产品
			List<Goods> goods = goodsService.findList(10, null, null,2);
			return MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
		}else if("doublePoint_23".equals(eventKey)){//双倍积分
			List<Goods> goods = goodsService.findList(10, null, null,3);
			return MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
		}else if("hotProduct_21".equals(eventKey)){//热门产品
			List<Goods> goods = goodsService.findList(10, null, null,5);
			return MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
		}else if("promotionProduct_24".equals(eventKey)){//促销产品
			List<Goods> goods = goodsService.findList(10, null, null,7);
			return MessageResponse.getPicTextMessage(fromUserName , toUserName , goods);
		}else{
			return MessageResponse.getTextMessage(fromUserName , toUserName , "欢迎加入商城官方群：432887669，获取最新信息。");
		}
	}
	
	
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	public String scan(Long parentId,ModelMap model) throws Exception {
		String url = "/upload/image/qrCode/promotion/337.jpg";
		Member parent = memberService.find(parentId);
		if(parent==null){
			parent = memberService.find(337L);
		}
		
		WeChatMember weChatMember = parent.getWeChatMember();
		if(weChatMember==null||weChatMember.getQrCodeImgUrl()==null){
			url = weChatMember.getQrCodeImgUrl();
		}
		model.addAttribute("url",url);
		return "/wechat/scan";
	}
}