package com.igomall.controller.wap;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.igomall.service.ProductService;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.WechatShare;

@Controller("wapProductController")
@RequestMapping("/wap/product")
public class ProductController extends BaseController {
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Resource(name="productServiceImpl")
	private ProductService productService;
	
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public String index(ModelMap model) throws Exception {
		return "/wap/tmpl/product/list";
	}
	
	@RequestMapping(value="/detail",method = RequestMethod.GET)
	public String detail(ModelMap model,HttpServletRequest request,Long goods_id) throws Exception {
		StringBuffer parameter = new StringBuffer();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap != null) {
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String parameterName = entry.getKey();
				if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
					String[] parameterValues = entry.getValue();
					if (parameterValues != null) {
						for (String parameterValue : parameterValues) {
							if(parameter.length()==0){
								parameter.append(parameterName + "=" + parameterValue);
							}else{
								parameter.append("&"+parameterName + "=" + parameterValue);
							}
						}
					}
				}
			}
		}
		String url= request.getRequestURL()+"?"+parameter;
		model.addAllAttributes(WechatShare.sign(url));
		model.addAttribute("appId", CommonWeChatAttributes.APPID);
		model.addAttribute("product", productService.find(goods_id));
		return "/wap/tmpl/product/detail";
	}
	
	@RequestMapping(value="/info",method = RequestMethod.GET)
	public String info(ModelMap model) throws Exception {
		return "/wap/tmpl/product/info";
	}
}
