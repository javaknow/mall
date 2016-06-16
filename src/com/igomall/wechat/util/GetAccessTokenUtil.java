package com.igomall.wechat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.Setting;
import com.igomall.util.HttpUtils;
import com.igomall.util.JsonUtils;
import com.igomall.util.SettingUtils;
import com.igomall.wechat.entity.AccessTokenTemp;
import com.igomall.wechat.entity.Ticket;
import com.igomall.wechat.entity.TotalMember;
import com.igomall.wechat.entity.WeChatMember;
import com.igomall.wechat.entity.WechatMedia;


public class GetAccessTokenUtil {
	public static AccessTokenTemp getAccessToken(String appid , String appsecret){
		AccessTokenTemp accessTokenTemp = null;
		String requestUrl = CommonWeChatAttributes.GET_ACCESSTOKEN_URL.replace("APPID" , appid).replace("APPSECRET" , appsecret);
		String msg = HttpUtils.getRequest(requestUrl,"GBK");
		accessTokenTemp = JsonUtils.toObject(msg, AccessTokenTemp.class);
		Setting setting = SettingUtils.get();
		setting.setAccess_token(accessTokenTemp.getAccess_token());
		//获取jsapi_ticket
		AccessTokenTemp accessTokenTemp1 = getJsapiTicket(setting.getAccess_token());
		setting.setJsapi_ticket(accessTokenTemp1.getTicket());
		SettingUtils.set(setting);
		return accessTokenTemp;
	}
	
	public static AccessTokenTemp getJsapiTicket(String access_token) {
		String requestUrl = CommonWeChatAttributes.GET_JSAPI.replace("ACCESS_TOKEN" , access_token);
		String msg = HttpUtils.getRequest(requestUrl,"GBK");
		AccessTokenTemp accessTokenTemp1 = JsonUtils.toObject(msg, AccessTokenTemp.class);
		return accessTokenTemp1;
	}

	public static String httpRequest(String requestUrl , String requestMethod , String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
			sslContext.init(null , tm , new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream , "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
//			jsonObject = JSONObject.fromObject(buffer.toString());
			return buffer.toString();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return jsonObject;
		return null;
	}
	
	public static void main(String[] args) {
		createMenu("XKKShhkptpeKLVEiFpwh9ymQLViQ6TCUH8LpvQ8UxrlA4rJ0o5NIV4LB8snSzRwNAfuw4nXeHNKqQhM-s6zg4C_t7WaRsGOrCqIZz0YdQZHHsMvy4_iBkBNahS7waTlFKOTjAIATXC");
		
	}

	public static Ticket createQrcode(String access_token,Long memberId) {
		String body ="{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":"+memberId+"}}}";
		String url = CommonWeChatAttributes.CREATE_QRCODE.replace("ACCESS_TOKEN", access_token);
		String msg = HttpUtils.postRequest1(url,body);
		Ticket ticket = JsonUtils.toObject(msg, Ticket.class);
		return ticket;
	}

	public static void createMenu(String access_token) {
		String jsonStr="";
		File file = new File(CommonWeChatAttributes.MENU_PROPERTIES);
		if(file.isFile() && file.exists()){ //判断文件是否存在
            try {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					jsonStr=jsonStr+lineTxt;
				}
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String result = httpRequest(CommonWeChatAttributes.MENU_CREATE_URL.replace("ACCESS_TOKEN", access_token),"POST",jsonStr);
	System.out.println(result);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 文件上传
	 * @param path
	 * @param fileType
	 * @param type
	 * @return
	 */
	public static WechatMedia uploadFile(String path, String fileType) {
		WechatMedia wechatMedia = new WechatMedia();
		String access_token = SettingUtils.get().getAccess_token();
		
		String uploadurl = CommonWeChatAttributes.UPLOADUR1.replace("ACCESS_TOKEN", access_token).replace("TYPE", fileType);

		HttpClient client = new HttpClient();
		
		
		PostMethod post = new PostMethod(uploadurl);
		post.setRequestHeader("User-Agent", "Mozilla/. (Macintosh; Intel Mac OS X.; rv:.) Gecko/ Firefox/.");
		post.setRequestHeader("Host", "file.api.weixin.qq.com");
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Cache-Control", "no-cache");
		try {
			
			File file = new File(path);
			if (!file.exists() || !file.isFile()) {
				throw new IOException("上传的文件不存在");
			}
			
			if (file != null && file.exists()) {
				FilePart filepart = new FilePart("media", file, "image/jpeg","UTF-8");
				Part[] parts = new Part[] { filepart };
				MultipartRequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
				post.setRequestEntity(entity);
				int status = client.executeMethod(post);
				if (status == HttpStatus.SC_OK) {
					String responseContent = post.getResponseBodyAsString();
					wechatMedia = JsonUtils.toObject(responseContent, WechatMedia.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return wechatMedia;
	}

	/**
	 * 调用微信接口来获取用户的资料
	 * @param fromUserName
	 * @return
	 */
	public static WeChatMember getMemberInfo(String fromUserName) {
		WeChatMember weChatMember = null;
		Setting setting = SettingUtils.get();
		String requestUrl = CommonWeChatAttributes.GET_USER_INFO.replace("ACCESS_TOKEN" , setting.getAccess_token()).replace("OPENID" , fromUserName);
		String msg = HttpUtils.getRequest(requestUrl,"UTF-8");
		weChatMember = JsonUtils.toObject(msg, WeChatMember.class);
		return weChatMember;
	}

	public static AccessTokenTemp getAccessToken1(String appid, String appsecret,String code) {
		AccessTokenTemp accessTokenTemp = null;
		String requestUrl = CommonWeChatAttributes.GET_ACCESSTOKEN_URL_BY_WEB.replace("APPID" , appid).replace("SECRET" , appsecret).replace("CODE", code);
		System.out.println("requestUrl:"+requestUrl);
		String msg = HttpUtils.getRequest(requestUrl,"GBK");
		accessTokenTemp = JsonUtils.toObject(msg, AccessTokenTemp.class);
		
		return accessTokenTemp;
	}
	
	/**
	 * 从 setting缓存里面拿取accessToken
	 * @return
	 */
	public static String getAccessToken(){
		String accessToken = null;
		Setting setting = SettingUtils.get();
		accessToken = setting.getAccess_token();
		//用这个accessToken请求一下服务器看看过期了没。如果过期了 重新获取一次
		
		String requestUrl = CommonWeChatAttributes.GET_ALL_GROUP.replace("ACCESS_TOKEN" , accessToken);
		String msg = HttpUtils.getRequest(requestUrl,"GBK");
		if(msg.contains("errcode")){//accessToken过期了 重新获取一次
			for (int i = 0; i < 10; i++) {
				AccessTokenTemp accessTokenTemp = getAccessToken(CommonWeChatAttributes.APPID, CommonWeChatAttributes.APPSECRET);
				if("0".equals(accessTokenTemp.getErrcode())){//获取成功
					break;
				}else{//失败了，再次重新获取
					
				}
			}
		}
		return accessToken;
		
	}

	public static TotalMember getMemberAll() {
		TotalMember totalMember = null;
		Setting setting = SettingUtils.get();
		
		String requestUrl = CommonWeChatAttributes.GET_ALL_USER.replace("ACCESS_TOKEN" , setting.getAccess_token());
		System.out.println(requestUrl);
		String msg = HttpUtils.getRequest(requestUrl,"UTF-8");
		System.out.println(msg);
		totalMember = JsonUtils.toObject(msg, TotalMember.class);
		
		return totalMember;
	}
	
}
