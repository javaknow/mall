
package com.igomall.plugin.dinpay;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.igomall.entity.Payment;
import com.igomall.entity.PluginConfig;
import com.igomall.plugin.PaymentPlugin;
import com.igomall.util.DateUtils;
import com.igomall.util.WebUtils;

@Component("dinpayPlugin")
public class DinpayPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "智付支付";
	}

	@Override
	public String getVersion() {
		return "V3.0";
	}

	@Override
	public String getAuthor() {
		return "igomall";
	}

	@Override
	public String getSiteUrl() {
		return "https://merchants.dinpay.com/languageChange?request_locale=zh_CN";
	}

	@Override
	public String getInstallUrl() {
		return "dinpay/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "dinpay/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "dinpay/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://pay.dinpay.com/gateway?input_charset=UTF-8";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		/*
		 * 商 家 号：merchant_code" value="1118002579">
			服务类型：service_type" value="direct_pay">
			参数编码字符集：input_charset" value="UTF-8"/>
			服务器异步通知地址：notify_url" value="http://www.i-gomall.com"/>
			接口版本：interface_version" value="V3.0"/>
			签名方式：sign_type" value="MD5"/>
			订单编号：order_no" value="1000201555"/>
			时间字段：order_time" value="2015-06-18 14:20:55"/>
			订单金额：order_amount" value="0.01"/>
			商品名称：product_name" value="图书"/>
			银行标识：bank_code" value="CMB">
			客户端IP：client_ip" value="192.168.1.1">
			商品编号：product_code" value="">
			商品描述：product_desc" value="">
			商品数量：product_num" value="">
			页面跳转同步通知地址：return_url" value="http://www.qiaomgj.com">
			商品展示URL：show_url" value="">
			是否允许重复订单：redo_flag" value="1">
		 */
		/*
		 * {
		 * merchant_code=1118002579,
		 *  key=igomall201552131719871006, 
		 *  service_type=direct_pay, 
		 *  input_charset=UTF-8, 
		 *  notify_url=http://www.i-gomall.com, 
		 *  interface_version=V3.0, 
		 *  sign_type=MD5, 
		 *  order_no=201506193535, 
		 *  order_time=2015-06-19 01:41:19, 
		 *  order_amount=111.10, 
		 *  product_name=订单支付, 
		 *  bank_code=ABC, 
		 *  client_ip=fe80:0:0:0:3538:f817:91b5:3fcb%24, 
		 *  product_code=1, 
		 *  product_desc=3573cf251dcbd90e393e5b29d1470011, 
		 *  product_num=1, 
		 *  return_url=1d107f6c09949c7251993399bde9fa02, 
		 *  show_url=http://www.i-gomall.com, redo_flag=0}

		 */
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("merchant_code", pluginConfig.getAttribute("partner"));
		parameterMap.put("key", pluginConfig.getAttribute("key"));
		parameterMap.put("service_type", "direct_pay");
		parameterMap.put("input_charset", getRequestCharset());
		parameterMap.put("notify_url", getNotifyUrl(sn, NotifyMethod.general));
		parameterMap.put("interface_version", getVersion());
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("order_no", sn);
		parameterMap.put("order_time", DateUtils.formatDateToString(payment.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		parameterMap.put("order_amount", payment.getAmount().setScale(2).toString());
		parameterMap.put("product_name", "订单支付");
		parameterMap.put("bank_code", payment.getBank().getBankCode());
		parameterMap.put("client_ip", "192.168.1.1");
		parameterMap.put("product_code", "1");
		parameterMap.put("product_desc", "订单支付");
		parameterMap.put("product_num", "1");
		parameterMap.put("return_url", "http://www.i-gomall.com");
		parameterMap.put("show_url", "http://www.i-gomall.com");
		parameterMap.put("redo_flag", 0);
		parameterMap.put("extra_return_param", "igomall_ok");
		parameterMap.put("extend_param", "");
		try {
			parameterMap.put("sign", DigestUtils.md5Hex(getSrcStr(parameterMap).getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("merchant_code", pluginConfig.getAttribute("partner"));
		parameterMap.put("key", pluginConfig.getAttribute("key"));
		parameterMap.put("service_type", "direct_pay");
		parameterMap.put("input_charset", getRequestCharset());
		parameterMap.put("notify_url", getNotifyUrl(sn, NotifyMethod.general));
		parameterMap.put("interface_version", getVersion());
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("order_no", sn);
		parameterMap.put("order_time", DateUtils.formatDateToString(payment.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		parameterMap.put("order_amount", payment.getAmount().setScale(2).toString());
		parameterMap.put("product_name", "订单支付");
		parameterMap.put("bank_code", payment.getBank().getBankCode());
		parameterMap.put("client_ip", "192.168.1.1");
		parameterMap.put("product_code", "1");
		parameterMap.put("product_desc", "订单支付");
		parameterMap.put("product_num", "1");
		parameterMap.put("return_url", "http://www.i-gomall.com");
		parameterMap.put("show_url", "http://www.i-gomall.com");
		parameterMap.put("redo_flag", 0);
		parameterMap.put("extra_return_param", "igomall_ok");
		parameterMap.put("extend_param", "");
		parameterMap.get("");
		try {
			parameterMap.put("sign", DigestUtils.md5Hex(getSrcStr(parameterMap).getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		if ("2".equals(WebUtils.getParameter(request.getQueryString(), "GBK", "r9_BType"))) {
			return "success";
		}
		return null;
	}

	@Override
	public Integer getTimeout() {
		return 21600;
	}
	
	public String getSrcStr(Map<String, Object> parameterMap){
		
		StringBuffer signSrc= new StringBuffer();
		String merchant_code = parameterMap.get("merchant_code").toString();
		String key = parameterMap.get("key").toString();
		String service_type = parameterMap.get("service_type").toString();
		String input_charset = parameterMap.get("input_charset").toString();
		String notify_url = parameterMap.get("notify_url").toString();
		String interface_version = parameterMap.get("interface_version").toString();
		String order_no = parameterMap.get("order_no").toString();
		String order_time = parameterMap.get("order_time").toString();
		String order_amount = parameterMap.get("order_amount").toString();
		String product_name = parameterMap.get("product_name").toString();
		String bank_code = parameterMap.get("bank_code").toString();
		String client_ip = parameterMap.get("client_ip").toString();
		String product_code = parameterMap.get("product_code").toString();
		String product_desc = parameterMap.get("product_desc").toString();
		String product_num = parameterMap.get("product_num").toString();
		String return_url = parameterMap.get("return_url").toString();
		String show_url = parameterMap.get("show_url").toString();
		String redo_flag = parameterMap.get("redo_flag").toString();
		String extra_return_param = parameterMap.get("extra_return_param").toString();
		String extend_param = parameterMap.get("extend_param").toString();
		
		//按照签名规则组织签名，按顺序排列
		if(!"".equals(bank_code)) {
			signSrc.append("bank_code=").append(bank_code).append("&");
		}
		if(!"".equals(client_ip)) {
			signSrc.append("client_ip=").append(client_ip).append("&");
		}
		if(!"".equals(extend_param)) {
			signSrc.append("extend_param=").append(extend_param).append("&");
		}
		if(!"".equals(extra_return_param)) {
			signSrc.append("extra_return_param=").append(extra_return_param).append("&");
		}
		if (!"".equals(input_charset)) {
			signSrc.append("input_charset=").append(input_charset).append("&");
		}
		if (!"".equals(interface_version)) {
			signSrc.append("interface_version=").append(interface_version).append("&");
		}
		if (!"".equals(merchant_code)) {
			signSrc.append("merchant_code=").append(merchant_code).append("&");
		}
		if(!"".equals(notify_url)) {
			signSrc.append("notify_url=").append(notify_url).append("&");
		}
		if(!"".equals(order_amount)) {
			signSrc.append("order_amount=").append(order_amount).append("&");
		}
		if(!"".equals(order_no)) {
			signSrc.append("order_no=").append(order_no).append("&");
		}
		if(!"".equals(order_time)) {
			signSrc.append("order_time=").append(order_time).append("&");
		}
		if(!"".equals(product_code)) {
			signSrc.append("product_code=").append(product_code).append("&");
		}
		if(!"".equals(product_desc)) {
			signSrc.append("product_desc=").append(product_desc).append("&");
		}
		if(!"".equals(product_name)) {
			signSrc.append("product_name=").append(product_name).append("&");
		}
		if(!"".equals(product_num)) {
			signSrc.append("product_num=").append(product_num).append("&");
		}
		if(!"".equals(redo_flag)) {
			signSrc.append("redo_flag=").append(redo_flag).append("&");
		}
		if(!"".equals(return_url)) {
			signSrc.append("return_url=").append(return_url).append("&");
		}
		if(!"".equals(service_type)) {
			signSrc.append("service_type=").append(service_type).append("&");
		}
		if(!"".equals(show_url)) {
			signSrc.append("show_url=").append(show_url).append("&");
		}
		
		signSrc.append("key=").append(key);
		return signSrc.toString();
	}

}